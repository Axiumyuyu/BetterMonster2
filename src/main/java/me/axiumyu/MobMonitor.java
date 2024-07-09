package me.axiumyu;

import me.axiumyu.config.ConfigurationReader;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Iterator;

import static java.lang.Math.abs;
import static me.axiumyu.config.ConfigurationReader.canBreak;
import static me.axiumyu.config.ConfigurationReader.canPlace;

public class MobMonitor extends BukkitRunnable {

    private final Mob mob;
    private double mobY;
    private Material mat;

    public MobMonitor(Mob mob) {
        this.mob = mob;
        mat = Material.STONE;
        if (mob.getWorld().isPiglinSafe()) {
            mat = Material.NETHERRACK;
        } else if (!mob.getWorld().isBedWorks()) {
            mat = Material.END_STONE;
        }
        if (mob instanceof Zombie || mob instanceof Skeleton) mobY = mob.getY();
    }

    @Override
    public void run() {
        if (mob.isValid() && !mob.isDead() && mob.getTarget() != null && mob.getTarget() instanceof Player && !mob.getTarget().isInvulnerable()) {
            LivingEntity mobTarget = null;
            if (ConfigurationReader.chainEnabled) {
                Iterator<Entity> iterator = mob.getWorld()
                        .getNearbyEntities(mob.getLocation(), ConfigurationReader.range, ConfigurationReader.range, ConfigurationReader.range, mob.getClass()::isInstance)
                        .stream().limit(ConfigurationReader.count).iterator();
                mobTarget = mob.getTarget();
                while (iterator.hasNext()) {
                    Mob mob2 = (Mob) iterator.next();
                    mob2.setTarget(mobTarget);
                }
            }
            mob.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 40, 0, false, false, false));
            mob.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 40, 0, false, false, false));
            mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 0, false, false, false));
            if ((mob instanceof Zombie || mob instanceof PiglinAbstract)) {
                if (abs(mob.getY() - mobY) <= 0.5 && mobTarget.getY() - mob.getY() >= 2) {
                    Location onHead = mob.getLocation().add(0, 2, 0);
                    if (canBreak && onHead.getBlock().getType().isSolid()) {
                        onHead.getBlock().breakNaturally(new ItemStack(Material.NETHERITE_PICKAXE), true, true);
                    }
                    if (canPlace && onHead.getBlock().getType().isSolid()) {
                        mob.setVelocity(new Vector(0, 0.5, 0));
                        mob.getLocation().getBlock().setType(mat);
                    }
                }
                mobY = mob.getY();
            }
        } else {
            this.cancel();
        }
    }
}
