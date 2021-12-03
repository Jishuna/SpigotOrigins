package me.jishuna.spigotorigins.api;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class OriginRegistry {

	private final Map<String, Origin> originMap = new TreeMap<>();
	
	public void registerOrigin(Origin origin) {
		this.originMap.put(origin.getName(), origin);
	}

	public Origin getOrigin(String name) {
		return this.originMap.get(name);
	}	
	
	public Collection<Origin> getOrigins() {
		return this.originMap.values();
	}
}