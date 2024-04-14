package net.cathienova.havengrowth.config;

import net.cathienova.havengrowth.HavenGrowth;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

public class HavenGrowthConfig {
    public static boolean showParticles;
    public static boolean useWhitelistOnly;
    public static List<String> whiteList;
    public static List<String> blackList;
    public static boolean onlySaplingsAndCrops;
    public static int playerDistance;
    public static double sprintGrowChance;
    public static double crouchGrowChance;

    public static void bake(ModConfig config) {
        showParticles = HavenGrowth.c_config.showParticles.get();
        useWhitelistOnly = HavenGrowth.c_config.useWhitelistOnly.get();
        whiteList = HavenGrowth.c_config.whiteList.get();
        blackList = HavenGrowth.c_config.blackList.get();
        onlySaplingsAndCrops = HavenGrowth.c_config.onlySaplingsAndCrops.get();
        playerDistance = HavenGrowth.c_config.playerDistance.get();
        sprintGrowChance = HavenGrowth.c_config.sprintGrowthChance.get();
        crouchGrowChance = HavenGrowth.c_config.crouchGrowthChance.get();
    }
}
