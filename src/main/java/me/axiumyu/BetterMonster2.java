package me.axiumyu;

import me.axiumyu.config.ConfigurationReader;
import me.axiumyu.event.MobGetTarget;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getPluginManager;

public final class BetterMonster2 extends JavaPlugin {

    public static NamespacedKey axiumyuKey(String name) {
        return new NamespacedKey("axiumyu", name);
    }

    public static final NamespacedKey STATE_TAG = axiumyuKey("state_tag");

    public static final NamespacedKey SOURCE = axiumyuKey("source");

    public static final NamespacedKey ATTACK = axiumyuKey("origin_attack");

    public static final NamespacedKey MAX_HEALTH = axiumyuKey("origin_max_health");

    public static final int ARMOR_CRITERIA = 12;

    public static final int HEALTH_CRITERIA = 24;

    public static final int DAMAGE_CRITERIA = 10;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        new ConfigurationReader(this.getConfig());
        getPluginManager().registerEvents(new MobGetTarget(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
