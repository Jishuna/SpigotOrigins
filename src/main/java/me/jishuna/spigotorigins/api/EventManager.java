package me.jishuna.spigotorigins.api;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.jishuna.actionconfiglib.ActionContext;
import me.jishuna.actionconfiglib.triggers.TriggerRegistry;
import me.jishuna.commonlib.events.EventConsumer;
import me.jishuna.spigotorigins.SpigotOrigins;

public class EventManager {
	private final SpigotOrigins plugin;

	public EventManager(SpigotOrigins plugin) {
		this.plugin = plugin;
		registerListeners();
	}

	public void registerListeners() {
		registerListener(BlockBreakEvent.class, this::onBlockBreak);
		registerListener(EntityDamageEvent.class, this::onDamage);
		registerListener(EntityTargetLivingEntityEvent.class, this::onTarget);
		registerListener(PlayerSwapHandItemsEvent.class, this::onHandSwap);
		registerListener(EntityPotionEffectEvent.class, this::onEffectGained);
		registerListener(EntityAirChangeEvent.class, this::onAirChange);
		registerListener(PlayerTeleportEvent.class, this::onTeleport);
		registerListener(PlayerInteractEntityEvent.class, this::onInteractEntity);
	}

	private void onDamage(EntityDamageEvent event) {
		if (event.getCause() == DamageCause.ENTITY_ATTACK || event.getCause() == DamageCause.ENTITY_SWEEP_ATTACK
				|| event.getCause() == DamageCause.PROJECTILE)
			return;

		if (!(event.getEntity()instanceof Player player))
			return;

		OriginPlayer originPlayer = plugin.getPlayerRegistry().getOriginPlayer(player);
		if (originPlayer == null)
			return;

		ActionContext context = new ActionContext.Builder(TriggerRegistry.DAMAGED_BY_OTHER).event(event).user(player)
				.build();
		originPlayer.handleContext(context);
	}

	private void onEffectGained(EntityPotionEffectEvent event) {
		if (!(event.getEntity()instanceof Player player) || event.getNewEffect() == null)
			return;

		OriginPlayer originPlayer = plugin.getPlayerRegistry().getOriginPlayer(player);
		if (originPlayer == null)
			return;

		ActionContext context = new ActionContext.Builder(TriggerRegistry.EFFECT_GAINED).event(event).user(player)
				.build();
		originPlayer.handleContext(context);
	}

	private void onAirChange(EntityAirChangeEvent event) {
		if (!(event.getEntity()instanceof Player player))
			return;

		OriginPlayer originPlayer = plugin.getPlayerRegistry().getOriginPlayer(player);
		if (originPlayer == null)
			return;

		ActionContext context = new ActionContext.Builder(SpigotOrigins.AIR_LEVEL_CHANGE).event(event).user(player)
				.build();
		originPlayer.handleContext(context);
	}

	private void onTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		OriginPlayer originPlayer = plugin.getPlayerRegistry().getOriginPlayer(player);
		if (originPlayer == null)
			return;

		ActionContext context = new ActionContext.Builder(SpigotOrigins.PLAYER_TELEPORT).event(event).user(player)
				.targetLocation(event.getTo()).build();
		originPlayer.handleContext(context);
	}

	private void onInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		OriginPlayer originPlayer = plugin.getPlayerRegistry().getOriginPlayer(player);
		if (originPlayer == null)
			return;

		ActionContext context = new ActionContext.Builder(TriggerRegistry.ENTITY_INTERACT).event(event).user(player)
				.opponent(event.getRightClicked()).build();
		originPlayer.handleContext(context);
	}

	private void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		OriginPlayer originPlayer = plugin.getPlayerRegistry().getOriginPlayer(player);
		if (originPlayer == null)
			return;

		ActionContext context = new ActionContext.Builder(TriggerRegistry.BREAK_BLOCK).event(event).user(player)
				.targetLocation(event.getBlock().getLocation()).build();
		originPlayer.handleContext(context);
	}

	private void onTarget(EntityTargetLivingEntityEvent event) {
		if (!(event.getTarget()instanceof Player player))
			return;

		OriginPlayer originPlayer = plugin.getPlayerRegistry().getOriginPlayer(player);
		if (originPlayer == null)
			return;

		ActionContext context = new ActionContext.Builder(TriggerRegistry.ENTITY_TARGET).event(event).user(player)
				.opponent(event.getEntity()).build();
		originPlayer.handleContext(context);
	}

	private void onHandSwap(PlayerSwapHandItemsEvent event) {
		Player player = event.getPlayer();

		OriginPlayer originPlayer = plugin.getPlayerRegistry().getOriginPlayer(player);
		if (originPlayer == null)
			return;

		ActionContext context = new ActionContext.Builder(TriggerRegistry.HAND_ITEM_SWAPPED).event(event).user(player)
				.build();
		originPlayer.handleContext(context);
	}

	public <T extends Event> void registerListener(Class<T> eventClass, Consumer<T> handler) {
		registerListener(eventClass, handler, EventPriority.NORMAL);
	}

	public <T extends Event> void registerListener(Class<T> eventClass, Consumer<T> handler, EventPriority priority) {

		EventConsumer<? extends Event> consumer = new EventConsumer<>(eventClass, handler);
		consumer.register(this.plugin, priority);
	}
}
