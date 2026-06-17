package net.cathienova.havengrowth;

import net.cathienova.havengrowth.config.CommonConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(HavenGrowth.MODID)
public class HavenGrowth
{
    public static final String MODID = "havengrowth";

    public HavenGrowth(IEventBus bus, ModContainer modContainer)
    {
        bus.addListener(this::commonSetup);

        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);

        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

}