package net.cathienova.havengrowth.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import java.util.List;

public class CommonConfig {
    public static final Pair<CommonConfig, ModConfigSpec> SPEC_PAIR = new ModConfigSpec.Builder().configure(CommonConfig::new);
    public static final CommonConfig CONFIG = SPEC_PAIR.getLeft();
    public static final ModConfigSpec SPEC = SPEC_PAIR.getRight();

    public final ModConfigSpec.ConfigValue<Boolean> showParticles;
    public final ModConfigSpec.ConfigValue<Boolean> useWhitelistOnly;
    public final ModConfigSpec.ConfigValue<List<String>> whiteList;
    public final ModConfigSpec.ConfigValue<List<String>> blackList;
    public final ModConfigSpec.ConfigValue<Boolean> onlySaplingsAndCrops;
    public final ModConfigSpec.ConfigValue<Integer> playerDistance;
    public final ModConfigSpec.ConfigValue<Double> sprintGrowthChance;
    public final ModConfigSpec.ConfigValue<Double> crouchGrowthChance;

    public CommonConfig(ModConfigSpec.Builder builder) {
        showParticles = builder.comment("Enable growth particles").define("showParticles", true);
        useWhitelistOnly = builder.comment("Use whitelist only. (will override blacklist and onlySaplingsAndCrops)").define("useWhitelistOnly", true);
        whiteList = builder.comment("Whitelist of blocks that will grow.").define("whiteList", defaultWhitelist());
        blackList = builder.comment("Blacklist of blocks that will not grow.").define("blackList", defaultBlacklist());
        onlySaplingsAndCrops = builder.comment("Only grow sapling tags and crop tags. (blacklist can exclude these)").define("onlySaplingsAndCrops", true);
        playerDistance = builder.comment("The distance from the player to check for growth in blocks. (N S E W)").defineInRange("playerDistance", 5, 1, 20);
        sprintGrowthChance = builder.comment("The chance of growth applied by sprinting. (1 = 100%)").defineInRange("sprintGrowthChance", 0.075, 0, 1);
        crouchGrowthChance = builder.comment("The chance of growth applied by crouching. (1 = 100%)").defineInRange("crouchGrowthChance", 0.15, 0, 1);
    }

    private static List<String> defaultWhitelist() {
        return List.of(
                "minecraft:wheat", "minecraft:beetroots", "minecraft:carrots", "minecraft:potatoes", "minecraft:melon_stem",
                "minecraft:pumpkin_stem", "minecraft:bamboo_sapling", "minecraft:oak_sapling", "minecraft:spruce_sapling",
                "minecraft:birch_sapling", "minecraft:jungle_sapling", "minecraft:acacia_sapling", "minecraft:dark_oak_sapling",

                "mysticalagriculture:air_crop", "mysticalagriculture:earth_crop", "mysticalagriculture:water_crop",
                "mysticalagriculture:fire_crop", "mysticalagriculture:inferium_crop", "mysticalagriculture:stone_crop",
                "mysticalagriculture:dirt_crop", "mysticalagriculture:wood_crop", "mysticalagriculture:ice_crop",
                "mysticalagriculture:deepslate_crop", "mysticalagriculture:allthemodium_crop", "mysticalagriculture:niter_crop",
                "mysticalagriculture:plastic_crop", "mysticalagriculture:ultimate_ingot_crop", "mysticalagriculture:unobtainium_crop",
                "mysticalagriculture:vibranium_crop", "mysticalagriculture:nature_crop", "mysticalagriculture:dye_crop",
                "mysticalagriculture:nether_crop", "mysticalagriculture:coal_crop", "mysticalagriculture:coral_crop",
                "mysticalagriculture:honey_crop", "mysticalagriculture:amethyst_crop", "mysticalagriculture:pig_crop",
                "mysticalagriculture:chicken_crop", "mysticalagriculture:cow_crop", "mysticalagriculture:sheep_crop",
                "mysticalagriculture:squid_crop", "mysticalagriculture:fish_crop", "mysticalagriculture:slime_crop",
                "mysticalagriculture:turtle_crop", "mysticalagriculture:rubber_crop", "mysticalagriculture:silicon_crop",
                "mysticalagriculture:sulfur_crop", "mysticalagriculture:aluminum_crop", "mysticalagriculture:saltpeter_crop",
                "mysticalagriculture:apatite_crop", "mysticalagriculture:limestone_crop", "mysticalagriculture:menril_crop",
                "mysticalagriculture:iron_crop", "mysticalagriculture:copper_crop", "mysticalagriculture:nether_quartz_crop",
                "mysticalagriculture:glowstone_crop", "mysticalagriculture:redstone_crop", "mysticalagriculture:obsidian_crop",
                "mysticalagriculture:prismarine_crop", "mysticalagriculture:zombie_crop", "mysticalagriculture:skeleton_crop",
                "mysticalagriculture:creeper_crop", "mysticalagriculture:spider_crop", "mysticalagriculture:rabbit_crop",
                "mysticalagriculture:tin_crop", "mysticalagriculture:bronze_crop", "mysticalagriculture:zinc_crop",
                "mysticalagriculture:brass_crop", "mysticalagriculture:silver_crop", "mysticalagriculture:lead_crop",
                "mysticalagriculture:graphite_crop", "mysticalagriculture:blizz_crop", "mysticalagriculture:blitz_crop",
                "mysticalagriculture:basalz_crop", "mysticalagriculture:quartz_enriched_iron_crop", "mysticalagriculture:gold_crop",
                "mysticalagriculture:lapis_lazuli_crop", "mysticalagriculture:end_crop", "mysticalagriculture:experience_crop",
                "mysticalagriculture:blaze_crop", "mysticalagriculture:ghast_crop", "mysticalagriculture:enderman_crop",
                "mysticalagriculture:steel_crop", "mysticalagriculture:nickel_crop", "mysticalagriculture:constantan_crop",
                "mysticalagriculture:electrum_crop", "mysticalagriculture:invar_crop", "mysticalagriculture:mithril_crop",
                "mysticalagriculture:tungsten_crop", "mysticalagriculture:titanium_crop", "mysticalagriculture:uranium_crop",
                "mysticalagriculture:chrome_crop", "mysticalagriculture:ruby_crop", "mysticalagriculture:sapphire_crop",
                "mysticalagriculture:peridot_crop", "mysticalagriculture:soulium_crop", "mysticalagriculture:signalum_crop",
                "mysticalagriculture:lumium_crop", "mysticalagriculture:flux_infused_ingot_crop", "mysticalagriculture:osmium_crop",
                "mysticalagriculture:fluorite_crop", "mysticalagriculture:refined_glowstone_crop", "mysticalagriculture:refined_obsidian_crop",
                "mysticalagriculture:energized_steel_crop", "mysticalagriculture:blazing_crystal_crop", "mysticalagriculture:diamond_crop",
                "mysticalagriculture:emerald_crop", "mysticalagriculture:netherite_crop", "mysticalagriculture:wither_skeleton_crop",
                "mysticalagriculture:platinum_crop", "mysticalagriculture:iridium_crop", "mysticalagriculture:enderium_crop",
                "mysticalagriculture:flux_infused_gem_crop", "mysticalagriculture:yellorium_crop", "mysticalagriculture:cyanite_crop",
                "mysticalagriculture:niotic_crystal_crop", "mysticalagriculture:spirited_crystal_crop", "mysticalagriculture:uraninite_crop",
                "mysticalagriculture:nether_star_crop", "mysticalagriculture:dragon_egg_crop", "mysticalagriculture:nitro_crystal_crop",

                "supplementaries:flax",

                "thermal:flax", "thermal:rubberwood_sapling", "thermal:amaranth", "thermal:barley", "thermal:corn", "thermal:onion",
                "thermal:radish", "thermal:rice", "thermal:sadiroot", "thermal:spinach", "thermal:bell_pepper", "thermal:eggplant",
                "thermal:green_bean", "thermal:peanut", "thermal:strawberry", "thermal:tomato", "thermal:coffee", "thermal:hops",
                "thermal:tea", "thermal:frost_melon",

                "ars_elemental:yellow_archwood_sapling", "ars_nouveau:blue_archwood_sapling", "ars_nouveau:red_archwood_sapling",
                "ars_nouveau:purple_archwood_sapling", "ars_nouveau:green_archwood_sapling",

                "biomesoplenty:origin_sapling", "biomesoplenty:flowering_oak_sapling", "biomesoplenty:snowblossom_sapling",
                "biomesoplenty:rainbow_birch_sapling", "biomesoplenty:yellow_autumn_sapling", "biomesoplenty:orange_autumn_sapling",
                "biomesoplenty:maple_sapling", "biomesoplenty:fir_sapling", "biomesoplenty:redwood_sapling", "biomesoplenty:mahogany_sapling",
                "biomesoplenty:jacaranda_sapling", "biomesoplenty:palm_sapling", "biomesoplenty:willow_sapling", "biomesoplenty:dead_sapling",
                "biomesoplenty:magic_sapling", "biomesoplenty:umbran_sapling", "biomesoplenty:hellbark_sapling",

                "create_dd:rubber_sapling",

                "croptopia:almond_sapling", "croptopia:apple_sapling", "croptopia:apricot_sapling", "croptopia:avocado_sapling",
                "croptopia:banana_sapling", "croptopia:cashew_sapling", "croptopia:cherry_sapling", "croptopia:coconut_sapling",
                "croptopia:date_sapling", "croptopia:dragonfruit_sapling", "croptopia:fig_sapling", "croptopia:grapefruit_sapling",
                "croptopia:kumquat_sapling", "croptopia:lemon_sapling", "croptopia:lime_sapling", "croptopia:mango_sapling",
                "croptopia:nectarine_sapling", "croptopia:nutmeg_sapling", "croptopia:orange_sapling", "croptopia:peach_sapling",
                "croptopia:pear_sapling", "croptopia:pecan_sapling", "croptopia:persimmon_sapling", "croptopia:plum_sapling",
                "croptopia:starfruit_sapling", "croptopia:walnut_sapling", "croptopia:cinnamon_sapling",

                "ecologics:walnut_sapling",

                "forbidden_arcanus:aurum_sapling",

                "integrateddynamics:menril_sapling",

                "quark:acnient_sapling", "quark:blue_blossom_sapling", "quark:lavender_blossom_sapling",
                "quark:orange_blossom_sapling", "quark:yellow_blossom_sapling", "quark:red_blossom_sapling",

                "croptopia:artichoke_crop", "croptopia:asparagus_crop", "croptopia:bareley_crop", "croptopia:basil_crop",
                "croptopia:bellpepper_crop", "croptopia:blackbean_crop", "croptopia:blackberry_crop", "croptopia:blueberry_crop",
                "croptopia:broccoli_crop", "croptopia:cabbage_crop", "croptopia:cantaloupe_crop", "croptopia:cauliflower_crop",
                "croptopia:celery_crop", "croptopia:chile_pepper_crop", "croptopia:coffee_crop", "croptopia:corn_crop",
                "croptopia:cranberry_crop", "croptopia:cucumber_crop", "croptopia:currant_crop", "croptopia:eggplant_crop",
                "croptopia:elderberry_crop", "croptopia:garlic_crop", "croptopia:ginger_crop", "croptopia:grape_crop",
                "croptopia:greenbean_crop", "croptopia:greenonion_crop", "croptopia:honeydew_crop", "croptopia:hops_crop",
                "croptopia:kale_crop", "croptopia:kiwi_crop", "croptopia:leek_crop", "croptopia:lettuce_crop", "croptopia:mustard_crop",
                "croptopia:oats_crop", "croptopia:olive_crop", "croptopia:onion_crop", "croptopia:peanut_crop", "croptopia:pepper_crop",
                "croptopia:pineapple_crop", "croptopia:radish_crop", "croptopia:raspberry_crop", "croptopia:rhubarb_crop",
                "croptopia:rice_crop", "croptopia:rutabaga_crop", "croptopia:saguaro_crop", "croptopia:soybean_crop", "croptopia:spinach_crop",
                "croptopia:squash_crop", "croptopia:strawberry_crop", "croptopia:sweetpotato_crop", "croptopia:tea_crop",
                "croptopia:tomatillo_crop", "croptopia:tomato_crop", "croptopia:turmeric_crop", "croptopia:turnip_crop",
                "croptopia:vanilla_crop", "croptopia:yam_crop", "croptopia:zucchini_crop", "croptopia:roasted_pumpkin_crop",
                "croptopia:roasted_sunflower_crop",

                "createcafe:cassava_crop",

                "farmersdelight:cabbages", "farmersdelight:budding_tomatoes", "farmersdelight:onions"
        );
    }

    private static List<String> defaultBlacklist() {
        return List.of(
                "minecraft:grass_block", "minecraft:netherrack", "minecraft:grass_block", "minecraft:warped_nylium", "minecraft:crimson_nylium"
        );
    }
}
