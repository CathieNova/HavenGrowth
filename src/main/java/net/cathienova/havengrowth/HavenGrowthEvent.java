package net.cathienova.havengrowth;

import net.cathienova.havengrowth.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = HavenGrowth.MODID)
public class HavenGrowthEvent {
    private static final Map<UUID, Boolean> prevSneaking = new HashMap<>();
    private static final Map<UUID, Boolean> hasCrouched = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        if (player.level().isClientSide()) {
            return;
        }

        UUID uuid = player.getUUID();
        prevSneaking.putIfAbsent(uuid, player.isCrouching());
        hasCrouched.putIfAbsent(uuid, false);
        handleMovementModes(player, uuid);
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID uuid = event.getEntity().getUUID();
        prevSneaking.remove(uuid);
        hasCrouched.remove(uuid);
    }

    private static void handleMovementModes(Player player, UUID uuid) {
        if (player.isSprinting() && !player.isCrouching()) {
            processPlantGrowth(player, CommonConfig.CONFIG.sprintGrowthChance.get());
        } else if (player.isCrouching() && !prevSneaking.get(uuid)) {
            if (!hasCrouched.get(uuid)) {
                processPlantGrowth(player, CommonConfig.CONFIG.crouchGrowthChance.get());
                hasCrouched.put(uuid, true);
            }
        } else if (!player.isCrouching()) {
            hasCrouched.put(uuid, false);
        }

        prevSneaking.put(uuid, player.isCrouching());
    }

    private static void processPlantGrowth(Player player, double growthChance) {
        Level world = player.level();
        BlockPos playerPos = player.blockPosition();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int playerDistance = CommonConfig.CONFIG.playerDistance.get();

        for (int x = -playerDistance; x <= playerDistance; x++) {
            for (int y = -1; y <= 2; y++) {
                for (int z = -playerDistance; z <= playerDistance; z++) {
                    pos.set(playerPos.getX() + x, playerPos.getY() + y, playerPos.getZ() + z);
                    growPlants(world, pos, growthChance, player);
                }
            }
        }
    }

    private static void growPlants(Level world, BlockPos pos, double growthChance, Player player) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();

        if (block != Blocks.AIR && block != Blocks.WATER && block != Blocks.LAVA && isPlantGrowable(blockState)) {
            if (canGrow(blockState, growthChance, world, pos, player) && CommonConfig.CONFIG.showParticles.get()) {
                spawnGrowthParticles(world, pos);
            }
        }
    }

    private static boolean isPlantGrowable(BlockState state) {
        return state.is(BlockTags.CROPS) || state.is(BlockTags.SAPLINGS) || state.getBlock() instanceof BonemealableBlock;
    }

    private static boolean canGrow(BlockState state, double growthChance, Level world, BlockPos pos, Player player) {
        if (CommonConfig.CONFIG.useWhitelistOnly.get()) {
            return isWhitelisted(state) && applyGrowth(world, state, pos, growthChance, player);
        }

        if (isBlacklisted(state)) {
            return false;
        }

        if (CommonConfig.CONFIG.onlySaplingsAndCrops.get() && !state.is(BlockTags.CROPS) && !state.is(BlockTags.SAPLINGS)) {
            return false;
        }

        return applyGrowth(world, state, pos, growthChance, player);
    }

    private static boolean isWhitelisted(BlockState state) {
        String blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
        for (String id : CommonConfig.CONFIG.whiteList.get()) {
            if (id.startsWith("#")) {
                if (state.is(TagKey.create(Registries.BLOCK, ResourceLocation.parse(id.substring(1))))) {
                    return true;
                }
            } else if (id.equals(blockId)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isBlacklisted(BlockState state) {
        String blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
        for (String id : CommonConfig.CONFIG.blackList.get()) {
            if (id.startsWith("#")) {
                if (state.is(TagKey.create(Registries.BLOCK, ResourceLocation.parse(id.substring(1))))) {
                    return true;
                }
            } else if (id.equals(blockId)) {
                return true;
            }
        }
        return false;
    }

    private static boolean applyGrowth(Level world, BlockState state, BlockPos pos, double chance, Player player) {
        double effectiveChance = chance;
        String blockRegistryName = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();

        if (blockRegistryName.startsWith("mysticalagriculture:")) {
            effectiveChance *= 2;
        }

        if (world.random.nextFloat() <= effectiveChance) {
            if (state.getBlock() instanceof CropBlock cropBlock) {
                return growCrop(world, pos, cropBlock, state);
            } else if (state.getBlock() instanceof BonemealableBlock) {
                return BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), world, pos, player);
            }
        }
        return false;
    }

    private static boolean growCrop(Level world, BlockPos pos, CropBlock crop, BlockState state) {
        int age = crop.getAge(state);
        if (age < crop.getMaxAge()) {
            crop.growCrops(world, pos, state);
            return true;
        }
        return false;
    }

    private static void spawnGrowthParticles(Level world, BlockPos pos) {
        if (!(world instanceof ServerLevel serverLevel)) {
            return;
        }

        serverLevel.sendParticles(
                ParticleTypes.HAPPY_VILLAGER,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                5,
                0.3,
                0.3,
                0.3,
                0.0
        );
    }
}
