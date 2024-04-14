package net.cathienova.havengrowth.config;

import net.cathienova.havengrowth.HavenGrowth;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = HavenGrowth.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigSubscriber {
    @SubscribeEvent
    public static void onModConfigEvent(final ModConfigEvent event) {
        HavenGrowthConfig.bake(event.getConfig());
    }
}