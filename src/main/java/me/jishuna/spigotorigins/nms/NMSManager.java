package me.jishuna.spigotorigins.nms;

import org.bukkit.Bukkit;

import me.jishuna.commonlib.Version;
import me.jishuna.spigotorigins.SpigotOrigins;

public class NMSManager {

	private static NMSAdapter adapter;

	public static void initAdapater(SpigotOrigins plugin) {
		String version = Version.getServerVersion();

		try {
			adapter = (NMSAdapter) Class.forName("me.jishuna.spigotorigins.nms." + version + ".NMSAdapter")
					.getDeclaredConstructor().newInstance();
			plugin.getLogger().info("Supported server version detected: " + version);
		} catch (ReflectiveOperationException e) {
			plugin.getLogger().severe("Server version \"" + version + "\" is unsupported! Please check for updates!");
			Bukkit.getPluginManager().disablePlugin(plugin);
		}
	}

	public static NMSAdapter getAdapter() {
		return adapter;
	}

}
