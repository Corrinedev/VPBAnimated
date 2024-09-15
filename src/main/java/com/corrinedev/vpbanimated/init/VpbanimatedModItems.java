
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.corrinedev.vpbanimated.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;

import com.corrinedev.vpbanimated.item.CleaverItem;
import com.corrinedev.vpbanimated.VpbanimatedMod;

public class VpbanimatedModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, VpbanimatedMod.MODID);
	public static final RegistryObject<Item> CLEAVER = REGISTRY.register("cleaver", () -> new CleaverItem());
	// Start of user code block custom items
	// End of user code block custom items
}
