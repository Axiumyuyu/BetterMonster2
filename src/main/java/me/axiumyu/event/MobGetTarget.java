package me.axiumyu.event;

import com.destroystokyo.paper.event.entity.EntityPathfindEvent;
import me.axiumyu.BetterMonster2;
import me.axiumyu.MobMonitor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static me.axiumyu.BetterMonster2.*;
import static me.axiumyu.DefalutAttackNumber.ATTACK_NUMBER;
import static me.axiumyu.MobEqualizer.BALANCE_AMOUNT;

public class MobGetTarget implements Listener {

    public static final String modified = "modified";
    public static final String recovered = "recovered";

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMobFindPath(EntityPathfindEvent pe) {
        try {
            final Entity targetEntity = pe.getTargetEntity();
            final Entity entity = pe.getEntity();
            PersistentDataContainer entityIc = entity.getPersistentDataContainer();
            if ((targetEntity instanceof Player && entity instanceof Mob && !(entity instanceof Boss)) && !(targetEntity.getUniqueId().toString().equals(entityIc.get(SOURCE, PersistentDataType.STRING)))) {
                final Mob mob = (Mob) entity;
                final Player pl = (Player) targetEntity;
                if (!entityIc.has(STATE_TAG, PersistentDataType.STRING) || Objects.equals(entityIc.get(STATE_TAG, PersistentDataType.STRING), recovered)) {
                    final double originMaxHealth = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    mob.getPersistentDataContainer().set(MAX_HEALTH, PersistentDataType.DOUBLE, originMaxHealth);
                    final double originAttack = mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue();
                    mob.getPersistentDataContainer().set(ATTACK, PersistentDataType.DOUBLE, originAttack);

                    final double armorVal = pl.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                    final double health = pl.getHealth();
                    if (health == 0) return;
                    final double absorption = pl.getAttribute(Attribute.GENERIC_MAX_ABSORPTION).getValue();
                    if (armorVal != 0) {
                        final double healthRate = Math.max(armorVal / ARMOR_CRITERIA, (health + (absorption / 2)) / HEALTH_CRITERIA);
                        final double attackRate = getMaxAttackValue(pl) / DAMAGE_CRITERIA;

                        AttributeInstance mobAttack = mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
                        mobAttack.setBaseValue(mobAttack.getBaseValue() * healthRate * BALANCE_AMOUNT.getOrDefault(mob.getType(), 1.0));

                        AttributeInstance mobHealth = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                        final double finalHealth = mobHealth.getBaseValue() * attackRate * BALANCE_AMOUNT.getOrDefault(mob.getType(), 1.0);
                        mobHealth.setBaseValue(finalHealth);
                        mob.setHealth(finalHealth);

                        mob.getPersistentDataContainer().set(STATE_TAG, PersistentDataType.STRING, modified);
                        mob.getPersistentDataContainer().set(SOURCE, PersistentDataType.STRING, pl.getUniqueId().toString());
                        pl.sendMessage("Health: " + healthRate + " Attack: " + attackRate);
                        if (healthRate >= 1 || attackRate >= 1 && !(mob instanceof Warden)) {
                            pl.sendMessage("start task");
                            new MobMonitor(mob).runTaskTimer(getPlugin(BetterMonster2.class), 0, 40);
                        }
                    }
                } else if (mob.getPersistentDataContainer().get(STATE_TAG, PersistentDataType.STRING) != null && mob.getPersistentDataContainer().get(STATE_TAG, PersistentDataType.STRING).equals(modified)) {

                    Double maxHealth = mob.getPersistentDataContainer().get(MAX_HEALTH, PersistentDataType.DOUBLE) == null ? 0.0 : mob.getPersistentDataContainer().get(MAX_HEALTH, PersistentDataType.DOUBLE);
                    mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
                    mob.setHealth(maxHealth);

                    Double attack = mob.getPersistentDataContainer().get(ATTACK, PersistentDataType.DOUBLE) == null ? 0.0 : mob.getPersistentDataContainer().get(ATTACK, PersistentDataType.DOUBLE);
                    mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(attack);
                    mob.getPersistentDataContainer().set(SOURCE, PersistentDataType.STRING, recovered);
                }
//                else {
//                    entity.getWorld().sendMessage(text("发生了未知错误。。。"));
//                }
            }
        } catch (IllegalArgumentException | IllegalStateException | NullPointerException ignore) {
        }
    }

    //获得热键栏中攻击力最大的物品值
    private static double getMaxAttackValue(Player pl) {
        double maxValue = 0;
        for (int i = 0; i < 8; i++) {
            ItemStack item = pl.getInventory().getItem(i);
            if (item == null) continue;
            final Collection<AttributeModifier> attributeModifiers = item.getItemMeta().getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE);
            if (attributeModifiers == null) continue;
            double value = getSingleFinalValue(item.getType(), pl.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue(), attributeModifiers);
            Map<Enchantment, Integer> enchantments = item.getEnchantments();
            if (enchantments.containsKey(Enchantment.SHARPNESS)) {
                value += enchantments.get(Enchantment.SHARPNESS) * 0.5 + 0.5;
            }

            if (value > maxValue) maxValue = value;
        }
        return maxValue;
    }

    //计算单个物品的Attribute总值
    private static double getSingleFinalValue(Material mat, double attackDefault, Collection<AttributeModifier> attributeModifiers) {
        double value = ATTACK_NUMBER.getOrDefault(mat, attackDefault).doubleValue();
        double addValue = 0;
        double multiplyBase = 0;
        double multiplyFinalBase = 0;
        for (AttributeModifier attributeModifier : attributeModifiers) {
            switch (attributeModifier.getOperation()) {
                case ADD_NUMBER:
                    addValue += attributeModifier.getAmount();
                    break;
                case ADD_SCALAR:
                    multiplyBase += attributeModifier.getAmount();
                    break;
                case MULTIPLY_SCALAR_1:
                    multiplyFinalBase *= 1 + attributeModifier.getAmount();
                    break;
            }
            value = (value + addValue) * (1 + multiplyBase) * multiplyFinalBase;
        }
        return value;
    }
}
