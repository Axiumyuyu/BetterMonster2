package me.axiumyu;

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

public class MobMonitor extends BukkitRunnable {

    private final Mob mob;
    private double mobY;
    private Material mat;

    public MobMonitor(Mob mob) {
        this.mob = mob;
        mat = Material.STONE;
        if (mob.getWorld().isPiglinSafe()) {
            mat = Material.NETHERRACK;
        }else if (!mob.getWorld().hasCeiling()) {
            mat = Material.END_STONE;
        }
        if (mob instanceof Zombie || mob instanceof Skeleton) mobY = mob.getY();
    }

    @Override
    public void run() {
        if (mob.isValid() && !mob.isDead() && mob.getTarget() != null && mob.getTarget() instanceof Player && !mob.getTarget().isInvulnerable()) {
            Iterator<Entity> iterator = mob.getWorld().getNearbyEntities(mob.getLocation(), 5, 5, 5, mob.getClass()::isInstance).iterator();
            LivingEntity mobTarget = mob.getTarget();
            while (iterator.hasNext()) {
                Mob mob2 = (Mob) iterator.next();
                mob2.setTarget(mobTarget);
            }
            mob.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60, 0, false, false, false));
            mob.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 60, 0, false, false, false));
            mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 0, false, false, false));
            mobTarget.sendMessage("Y: " + mob.getY() + " Target Y: " + mobTarget.getY()+"Last Y: " + mobY);
            if ((mob instanceof Zombie || mob instanceof PiglinAbstract)) {
                if (abs(mob.getY() - mobY) <= 0.5 && mobTarget.getY()-mob.getY() >= 2) {
                    Location underFoot = mob.getLocation().add(0, -1, 0);
                    Location onHead = mob.getLocation().add(0, 3, 0);
                    if (!onHead.getBlock().getType().isSolid()) {
                        onHead.getBlock().breakNaturally(new ItemStack(Material.NETHERITE_PICKAXE),true,true);
                    }
                    mob.setVelocity(new Vector(0, 0.5, 0));
                    underFoot.getBlock().setType(mat);
                }
                mobY = mob.getY();
            }
        } else {
            this.cancel();
        }
    }
}
