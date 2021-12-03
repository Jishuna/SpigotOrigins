package me.jishuna.spigotorigins.api;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerEvent;
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

		OriginPlayer player = plugin.getPlayerRegistry().getOriginPlayer((Player) event.getEntity());
		if (player == null)
			return;

		player.getOrigin().ifPresent(origin -> origin.handleAbilities(eventClass, event, player));
	}

	public <T extends PlayerEvent> void processEvent(T event, Class<T> eventClass) {
		OriginPlayer player = plugin.getPlayerRegistry().getOriginPlayer(event.getPlayer());
		if (player == null)
			return;

		player.getOrigin().ifPresent(origin -> origin.handleAbilities(eventClass, event, player));
	}

	private void registerBaseEvents() {
		registerListener(PlayerSwapHandItemsEvent.class, event -> processEvent(event, PlayerSwapHandItemsEvent.class));
		registerListener(PlayerDeathEvent.class, event -> processEvent(event, PlayerDeathEvent.class));
	}
}
