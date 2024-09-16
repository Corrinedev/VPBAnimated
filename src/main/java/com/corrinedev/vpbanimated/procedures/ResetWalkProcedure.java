package com.corrinedev.vpbanimated.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import com.corrinedev.vpbanimated.network.VpbanimatedModVariables;
import com.corrinedev.vpbanimated.VpbanimatedMod;

public class ResetWalkProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		VpbanimatedMod.queueServerWork(10, () -> {
			{
				boolean _setval = false;
				entity.getCapability(VpbanimatedModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.PistolWalk = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		});
	}
}
