package me.jishuna.spigotorigins.api;

import java.util.function.BiConsumer;

import org.bukkit.event.Event;

public class EventWrapper<T extends Event> {

	private BiConsumer<T, OriginPlayer> handler;
	private Class<T> eventClass;

	public EventWrapper(Class<T> eventClass, BiConsumer<T, OriginPlayer> handler) {
		this.handler = handler;
		this.eventClass = eventClass;
	}

	public void consume(Event event, OriginPlayer player) {
		if (this.eventClass.isAssignableFrom(event.getClass())) {
			handler.accept(this.eventClass.cast(event), player);
		}
	}
}
