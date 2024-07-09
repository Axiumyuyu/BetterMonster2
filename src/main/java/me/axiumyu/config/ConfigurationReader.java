package me.axiumyu.config;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConfigurationReader {

    public static final Map<EntityType,Double> BALANCE_AMOUNT = new HashMap<>();

    private final Configuration config;
    public static boolean chainEnabled ;
    public static int count;
    public static int range;

    public static boolean canBreak ;
    public static boolean canPlace;
    public static int armorRate;
    public static int healthRate;
    public static double absorptionRate;

    public ConfigurationReader(Configuration config) {
        this.config = config;
        final Iterator<EntityType> iterator = Arrays.stream(EntityType.values()).iterator();
        while (iterator.hasNext()) {
            if (!config.contains(iterator.next().name())) continue;
            BALANCE_AMOUNT.put(iterator.next(),config.getDouble(iterator.next().name()));
        }
        readConfig();
    }

    private void readConfig() {
        chainEnabled = config.getBoolean("chain_enabled");
        if (chainEnabled) {
            count = config.getInt("count");
            range = config.getInt("range");
        }

        canBreak = config.getBoolean("can-break");

        canPlace = config.getBoolean("can-place");

        armorRate = config.getInt("armor-rate");

        healthRate = config.getInt("health-rate");

        absorptionRate = config.getDouble("absorption-rate");

    }
}
