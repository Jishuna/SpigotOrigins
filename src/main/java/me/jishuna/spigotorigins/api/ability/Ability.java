package me.jishuna.spigotorigins.api.ability;

import java.util.Collection;
import java.util.function.BiConsumer;

import org.bukkit.event.Event;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import me.jishuna.spigotorigins.api.EventWrapper;
import me.jishuna.spigotorigins.api.OriginPlayer;

public abstract class Ability {

	private final Multimap<Class<? extends Event>, EventWrapper<? extends Event>> handlerMap = ArrayListMultimap
			.create();

	public <T extends Event> void addEventHandler(Class<T> type, BiConsumer<T, OriginPlayer> consumer) {
		this.handlerMap.put(type, new EventWrapper<>(type, consumer));
	}

	public <T extends Event> Collection<EventWrapper<? extends Event>> getEventHandlers(Class<T> type) {
		return this.handlerMap.get(type);
	}
}
