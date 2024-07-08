package me.axiumyu;

import org.bukkit.entity.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static me.axiumyu.BetterMonster2.config;

public final class MobEqualizer {
    public static final  Map<EntityType,Double> BALANCE_AMOUNT = new HashMap<>();
    public MobEqualizer() {
        final Iterator<EntityType> iterator = Arrays.stream(EntityType.values()).iterator();
        EntityType type;
        while (iterator.hasNext()) {
            type = iterator.next();
            if (!config.contains(type.name())) continue;
            BALANCE_AMOUNT.put(type,config.getDouble(type.name()));
        }
    }
}
