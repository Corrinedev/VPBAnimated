package com.corrinedev.vpbanimated.init;

import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import com.corrinedev.vpbanimated.configuration.ItemListConfiguration;
import com.corrinedev.vpbanimated.VpbanimatedMod;

@Mod.EventBusSubscriber(modid = VpbanimatedMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VpbanimatedModConfigs {
	@SubscribeEvent
	public static void register(FMLConstructModEvent event) {
		event.enqueueWork(() -> {
			ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ItemListConfiguration.SPEC, "vpbanimated-itemlist-config.toml");
		});
	}
}
