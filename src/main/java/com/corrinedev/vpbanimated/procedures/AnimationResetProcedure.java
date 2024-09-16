package com.corrinedev.vpbanimated.procedures;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.Connection;
import net.minecraft.client.player.AbstractClientPlayer;

import javax.annotation.Nullable;

import java.util.List;
import java.util.Iterator;

import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.IAnimation;

import com.corrinedev.vpbanimated.network.VpbanimatedModVariables;
import com.corrinedev.vpbanimated.VpbanimatedMod;

@Mod.EventBusSubscriber
public class AnimationResetProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player.level(), event.player);
		}
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (!((entity.getCapability(VpbanimatedModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new VpbanimatedModVariables.PlayerVariables())).PistolSprint
				&& (entity.getCapability(VpbanimatedModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new VpbanimatedModVariables.PlayerVariables())).PistolIdle
				&& (entity.getCapability(VpbanimatedModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new VpbanimatedModVariables.PlayerVariables())).PistolAim
				&& (entity.getCapability(VpbanimatedModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new VpbanimatedModVariables.PlayerVariables())).PistolWalk)) {
			if (world.isClientSide()) {
				if (entity instanceof AbstractClientPlayer player) {
					var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player).get(new ResourceLocation("vpbanimated", "player_animation"));
					if (animation != null && !animation.isActive()) {
						animation.setAnimation(new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation("vpbanimated", "empty"))));
					}
				}
			}
			if (!world.isClientSide()) {
				if (entity instanceof Player && world instanceof ServerLevel srvLvl_) {
					List<Connection> connections = srvLvl_.getServer().getConnection().getConnections();
					synchronized (connections) {
						Iterator<Connection> iterator = connections.iterator();
						while (iterator.hasNext()) {
							Connection connection = iterator.next();
							if (!connection.isConnecting() && connection.isConnected())
								VpbanimatedMod.PACKET_HANDLER.sendTo(new SetupAnimationsProcedure.VpbanimatedModAnimationMessage(Component.literal("empty"), entity.getId(), false), connection, NetworkDirection.PLAY_TO_CLIENT);
						}
					}
				}
			}
		}
	}
}
