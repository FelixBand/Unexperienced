package net.smileycorp.unexperienced;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.smileycorp.unexperienced.network.SyncBottlesMessage;

import java.util.Collection;

public class EventHandler {

	@SubscribeEvent(priority= EventPriority.LOWEST)
	public void onXPDrop(LivingExperienceDropEvent event) {
		if (!CommonConfigHandler.directXP.get() || (event.getEntity() instanceof Player)) return;
		Player player = event.getAttackingPlayer();
		Unexperienced.addExperience(player, event.getDroppedExperience());
		event.setCanceled(true);
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onEntityJoinWorld(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();
		if (!(entity instanceof ExperienceOrb || (CommonConfigHandler.disableXP.get() && CommonConfigHandler.directXP.get()))) return;
		event.setCanceled(true);
		if (entity.level().isClientSide) return;
		Player player = entity.level().getNearestPlayer(entity, 8.0D);
		if (player != null) Unexperienced.addExperience(player, ((ExperienceOrb)entity).getValue());
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onEntityDeath(LivingDeathEvent event) {
		LivingEntity entity = event.getEntity();
		if (entity == null) return;
		if (entity.level().isClientSide) return;
		if (!CommonConfigHandler.directXP.get() | !(entity instanceof EnderDragon)) return;
		EndDragonFight fightManager = ((EnderDragon) entity).getDragonFight();
		if (fightManager == null) return;
		Collection<ServerPlayer> players = fightManager.dragonEvent.getPlayers();
		int amount = (int) Math.ceil(((double)(fightManager.hasPreviouslyKilledDragon() ? 500 : 12000)) / (double)players.size());
		for (ServerPlayer player : players) Unexperienced.addExperience(player, amount);
	}

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		Player player = event.getEntity();
		if ( player instanceof ServerPlayer) PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncBottlesMessage(CommonConfigHandler.drinkBottles.get()));
	}

	@SubscribeEvent
	public void onTooltip(ItemTooltipEvent event) {
		if (event.getItemStack() == null) return;
		if (event.getItemStack().getItem() != Items.EXPERIENCE_BOTTLE |! CommonConfigHandler.canDrinkBottles()) return;
		event.getToolTip().add(1, Component.translatable("tooltip.unexperienced.ExperienceBottle"));
	}
	
}
