package net.cathienova.havengrowth;

import net.cathienova.havengrowth.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.particles.ParticleTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;

import java.util.*;

@Mod.EventBusSubscriber(modid = HavenGrowth.MODID)
public class HavenGrowthEvent
{
    private static final Map<UUID, Integer> crouchCount = new HashMap<>();
    private static final Map<UUID, Boolean> prevSneaking = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.PlayerTickEvent.Phase.START)
        {
            Player player = event.player;
            UUID uuid = player.getUUID();
            prevSneaking.putIfAbsent(uuid, player.isCrouching());
            crouchCount.putIfAbsent(uuid, 0);

            handleMovementModes(player, uuid);
        }
    }

    private static void handleMovementModes(Player player, UUID uuid) {
        // Check if the player is sprinting but not crouching
        if (player.isSprinting() && !player.isCrouching()) {
            processPlantGrowth(player, CommonConfig.CONFIG.sprintGrowthChance.get());
        }
        // Otherwise, check if the player has started twerking and is not sprinting
        else if (hasStartedCrouching(player, uuid) && !player.isSprinting()) {
            processPlantGrowth(player, CommonConfig.CONFIG.crouchGrowthChance.get());
        }
    }

    private static boolean hasStartedCrouching(Player player, UUID uuid)
    {
        // Twerking is determined by consecutive crouch actions
        boolean wasCrouching = prevSneaking.get(uuid);
        boolean isCrouching = player.isCrouching();
        prevSneaking.put(uuid, isCrouching);

        if (isCrouching && !wasCrouching)
        {
            crouchCount.put(uuid, crouchCount.get(uuid) + 1);
        }

        return isCrouching && crouchCount.get(uuid) >= 2;
    }

    private static void processPlantGrowth(Player player, double growthChance)
    {
        List<BlockPos> nearbyBlocks = findNearbyBlocks(player);
        nearbyBlocks.forEach(pos -> growPlants(player.level(), pos, growthChance, player));
    }

    private static List<BlockPos> findNearbyBlocks(Player player)
    {
        // Calculate blocks within a configurable distance around the player
        List<BlockPos> nearbyBlocks = new ArrayList<>();
        BlockPos playerPos = player.blockPosition();
        int playerDistance = CommonConfig.CONFIG.playerDistance.get();
        for (int x = -playerDistance; x <= playerDistance; x++)
        {
            for (int y = -1; y <= 2; y++)
            {
                for (int z = -playerDistance; z <= playerDistance; z++)
                {
                    BlockPos pos = playerPos.offset(x, y, z);
                    nearbyBlocks.add(pos);
                }
            }
        }
        return nearbyBlocks;
    }

    private static void growPlants(Level world, BlockPos pos, double growthChance, Player player)
    {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();

        if (block == Blocks.AIR || block == Blocks.WATER || block == Blocks.LAVA || !isPlantGrowable(blockState))
            return;

        // Execute growth only if conditions defined in white/blacklist are met
        if (canGrow(blockState, growthChance, world, pos, player))
        {
            if (CommonConfig.CONFIG.showParticles.get()) spawnGrowthParticles(world, pos);
        }
    }

    private static boolean isPlantGrowable(BlockState state)
    {
        // Simple check to filter growable plant types
        return state.is(BlockTags.CROPS) || state.is(BlockTags.SAPLINGS) || state.getBlock() instanceof BonemealableBlock;
    }

    private static boolean canGrow(BlockState state, double growthChance, Level world, BlockPos pos, Player player)
    {
        // Determine if the plant can grow based on whitelist and blacklist logic
        if (isBlacklisted(state))
        {
            return false;
        }
        if (CommonConfig.CONFIG.useWhitelistOnly.get() && isWhitelisted(state))
        {
            return applyGrowth(world, state, pos, growthChance, player);
        }
        else if (!CommonConfig.CONFIG.useWhitelistOnly.get())
        {
            return applyGrowth(world, state, pos, growthChance, player);
        }
        return false;
    }

    private static boolean isWhitelisted(BlockState state)
    {
        String blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
        // Advanced tag handling in whitelist check
        for (String id : CommonConfig.CONFIG.whiteList.get())
        {
            if (id.startsWith("#"))
            {
                String tagId = id.substring(1);
                if (state.is(TagKey.create(Registries.BLOCK, new ResourceLocation(tagId))))
                {
                    return true;
                }
            }
            else if (id.equals(blockId))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean isBlacklisted(BlockState state)
    {
        String blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
        // Advanced tag handling in blacklist check
        for (String id : CommonConfig.CONFIG.blackList.get())
        {
            if (id.startsWith("#"))
            {
                String tagId = id.substring(1);
                if (state.is(TagKey.create(Registries.BLOCK, new ResourceLocation(tagId))))
                {
                    return true;
                }
            }
            else if (id.equals(blockId))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean applyGrowth(Level world, BlockState state, BlockPos pos, double chance, Player player) {
        // Determine the effective growth chance based on the block's mod origin, reason being Mystical Agriculture requires higher growth chance to grow
        double effectiveChance = chance;
        String blockRegistryName = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();

        // If the block is from mysticalagriculture, modify the effective chance
        if (blockRegistryName.startsWith("mysticalagriculture:")) {
            effectiveChance *= 2;  // Double the chance if the block is from mysticalagriculture
        }

        // Actual growth application if the random factor and modified conditions allow
        if (world.random.nextFloat() <= effectiveChance) {
            if (state.getBlock() instanceof CropBlock cropBlock) {
                return growCrop(world, pos, cropBlock, state);
            } else if (state.getBlock() instanceof BonemealableBlock) {
                BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), world, pos, player);
                return true;
            }
        }
        return false;
    }

    private static boolean growCrop(Level world, BlockPos pos, CropBlock crop, BlockState state)
    {
        int age = crop.getAge(state);
        if (age < crop.getMaxAge())
        {
            crop.growCrops(world, pos, state);
            return true;
        }
        return false;
    }

    private static void spawnGrowthParticles(Level world, BlockPos pos)
    {
        // Particle effects for visual feedback on growth
        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 0.5;
        double centerZ = pos.getZ() + 0.5;
        Random random = new Random();
        for (int i = 0; i < 5; i++)
        {
            double offsetX = random.nextFloat() * 0.6 - 0.3;
            double offsetY = random.nextFloat() * 0.6 - 0.3;
            double offsetZ = random.nextFloat() * 0.6 - 0.3;
            world.addParticle(ParticleTypes.HAPPY_VILLAGER, centerX + offsetX, centerY + offsetY, centerZ + offsetZ, 0.0D, 0.0D, 0.0D);
        }
    }
}
