package me.jishuna.spigotorigins.api;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import me.jishuna.spigotorigins.SpigotOrigins;

public enum PluginKeys {
	ORIGIN("origin");

	private final NamespacedKey key;

	private PluginKeys(String name) {
		this.key = new NamespacedKey(JavaPlugin.getPlugin(SpigotOrigins.class), name);
	}

	public NamespacedKey getKey() {
		return this.key;
	}

}
