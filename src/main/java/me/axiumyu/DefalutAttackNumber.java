package me.axiumyu;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class DefalutAttackNumber {
    public static Map<Material,Number> ATTACK_NUMBER = new HashMap<>();

    static {

        ATTACK_NUMBER.put(Material.DIAMOND_SWORD, 7);

        ATTACK_NUMBER.put(Material.IRON_SWORD, 6);

        ATTACK_NUMBER.put(Material.GOLDEN_SWORD, 4);

        ATTACK_NUMBER.put(Material.STONE_SWORD, 5);

        ATTACK_NUMBER.put(Material.WOODEN_SWORD, 4);

        ATTACK_NUMBER.put(Material.NETHERITE_SWORD, 8);


        ATTACK_NUMBER.put(Material.TRIDENT, 9);


        ATTACK_NUMBER.put(Material.WOODEN_AXE,7);

        ATTACK_NUMBER.put(Material.STONE_AXE, 9);

        ATTACK_NUMBER.put(Material.IRON_AXE, 9);

        ATTACK_NUMBER.put(Material.GOLDEN_AXE, 7);

        ATTACK_NUMBER.put(Material.DIAMOND_AXE, 9);

        ATTACK_NUMBER.put(Material.NETHERITE_AXE, 10);


        ATTACK_NUMBER.put(Material.WOODEN_HOE, 1);

        ATTACK_NUMBER.put(Material.STONE_HOE, 1);

        ATTACK_NUMBER.put(Material.IRON_HOE, 1);

        ATTACK_NUMBER.put(Material.GOLDEN_HOE, 1);

        ATTACK_NUMBER.put(Material.DIAMOND_HOE, 1);

        ATTACK_NUMBER.put(Material.NETHERITE_HOE, 1);


        ATTACK_NUMBER.put(Material.WOODEN_PICKAXE, 2);

        ATTACK_NUMBER.put(Material.STONE_PICKAXE, 3);

        ATTACK_NUMBER.put(Material.IRON_PICKAXE, 4);

        ATTACK_NUMBER.put(Material.GOLDEN_PICKAXE, 2);

        ATTACK_NUMBER.put(Material.DIAMOND_PICKAXE, 5);

        ATTACK_NUMBER.put(Material.NETHERITE_PICKAXE, 6);


        ATTACK_NUMBER.put(Material.WOODEN_SHOVEL, 2.5);

        ATTACK_NUMBER.put(Material.STONE_SHOVEL, 3.5);

        ATTACK_NUMBER.put(Material.IRON_SHOVEL, 4.5);

        ATTACK_NUMBER.put(Material.GOLDEN_SHOVEL, 2.5);

        ATTACK_NUMBER.put(Material.DIAMOND_SHOVEL, 5.5);

        ATTACK_NUMBER.put(Material.NETHERITE_SHOVEL, 6.5);


    }
}
