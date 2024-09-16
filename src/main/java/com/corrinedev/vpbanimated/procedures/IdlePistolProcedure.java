package com.corrinedev.vpbanimated.procedures;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
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
import com.corrinedev.vpbanimated.configuration.ItemListConfiguration;
import com.corrinedev.vpbanimated.VpbanimatedMod;

@Mod.EventBusSubscriber
public class IdlePistolProcedure {
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
		for (String stringiterator : ItemListConfiguration.PISTOLS.get()) {
			if ((ForgeRegistries.ITEMS.getKey((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem()).toString()).equals(stringiterator)) {
				if (!(entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D)) {
					if (world.isClientSide()) {
						if (entity instanceof AbstractClientPlayer player) {
							var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player).get(new ResourceLocation("vpbanimated", "player_animation"));
							if (animation != null) {
								animation.setAnimation(new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation("vpbanimated", "pistolidle"))));
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
										VpbanimatedMod.PACKET_HANDLER.sendTo(new SetupAnimationsProcedure.VpbanimatedModAnimationMessage(Component.literal("pistolidle"), entity.getId(), true), connection, NetworkDirection.PLAY_TO_CLIENT);
								}
							}
						}
					}
					{
						boolean _setval = true;
						entity.getCapability(VpbanimatedModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
							capability.PistolIdle = _setval;
							capability.syncPlayerVariables(entity);
						});
					}
				} else {
					{
						boolean _setval = false;
						entity.getCapability(VpbanimatedModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
							capability.PistolIdle = _setval;
							capability.syncPlayerVariables(entity);
						});
					}
				}
			}
		}
	}
}
