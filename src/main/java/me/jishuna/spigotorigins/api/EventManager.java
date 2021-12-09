package me.jishuna.spigotorigins.api;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

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

	private void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		OriginPlayer originPlayer = plugin.getPlayerRegistry().getOriginPlayer(player);
		if (originPlayer == null)
			return;

		ActionContext context = new ActionContext.Builder(TriggerRegistry.BREAK_BLOCK).event(event).user(player)
				.targetLocation(event.getBlock().getLocation()).build();
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
