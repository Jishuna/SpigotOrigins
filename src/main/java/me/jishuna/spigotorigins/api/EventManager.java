package me.jishuna.spigotorigins.api;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import me.jishuna.commonlib.events.EventConsumer;
import me.jishuna.spigotorigins.SpigotOrigins;

public class EventManager {
	private final SpigotOrigins plugin;

	private Map<Class<? extends Event>, EventConsumer<? extends Event>> listenerMap = new HashMap<>();

	public EventManager(SpigotOrigins plugin) {
		this.plugin = plugin;
		registerBaseEvents();
	}

	public <T extends Event> boolean registerListener(Class<T> eventClass, Consumer<T> handler) {
		return registerListener(eventClass, handler, EventPriority.NORMAL);
	}

	public <T extends Event> boolean registerListener(Class<T> eventClass, Consumer<T> handler,
			EventPriority priority) {
		if (isListenerRegistered(eventClass))
			return false;

		EventConsumer<? extends Event> consumer = new EventConsumer<>(eventClass, handler);
		consumer.register(this.plugin, priority);

		this.listenerMap.put(eventClass, consumer);
		return true;
	}

	public boolean isListenerRegistered(Class<? extends Event> eventClass) {
		return this.listenerMap.containsKey(eventClass);
	}

	public <T extends EntityEvent> void processEvent(T event, Class<T> eventClass) {
		if (event.getEntityType() != EntityType.PLAYER)
			return;

		handlePlayer((Player) event.getEntity(), event, eventClass);
	}

	public <T extends PlayerEvent> void processEvent(T event, Class<T> eventClass) {
		handlePlayer(event.getPlayer(), event, eventClass);
	}

	private <T extends Event> void handlePlayer(Player player, T event, Class<T> eventClass) {
		OriginPlayer originPlayer = plugin.getPlayerRegistry().getOriginPlayer(player);
		if (originPlayer == null)
			return;

		originPlayer.getOrigin().ifPresent(origin -> origin.handleAbilities(eventClass, event, originPlayer));
	}

	private void registerBaseEvents() {
		registerListener(PlayerSwapHandItemsEvent.class, event -> processEvent(event, PlayerSwapHandItemsEvent.class));
		registerListener(PlayerDeathEvent.class, event -> processEvent(event, PlayerDeathEvent.class));
		registerListener(PlayerMoveEvent.class, event -> processEvent(event, PlayerMoveEvent.class));
		registerListener(PlayerInteractEvent.class, event -> processEvent(event, PlayerInteractEvent.class));
		registerListener(EntityAirChangeEvent.class, event -> processEvent(event, EntityAirChangeEvent.class));
		registerListener(PlayerRespawnEvent.class, event -> processEvent(event, PlayerRespawnEvent.class));

		registerListener(EntityTargetLivingEntityEvent.class, event -> {
			if (event.getTarget() == null || event.getTarget().getType() != EntityType.PLAYER)
				return;

			handlePlayer((Player) event.getTarget(), event, EntityTargetLivingEntityEvent.class);
		});
	}
}
