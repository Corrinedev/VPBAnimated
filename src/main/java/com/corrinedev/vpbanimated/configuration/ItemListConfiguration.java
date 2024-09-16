package com.corrinedev.vpbanimated.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ItemListConfiguration {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> PISTOLS;
	static {
		BUILDER.push("Guns");
		PISTOLS = BUILDER.comment("add the modid and gunid, so something like pointblank:example").defineList("Pistols", List.of("pointblank:glock_17", "minecraft:bow"), entry -> true);
		BUILDER.pop();

		SPEC = BUILDER.build();
	}

}
