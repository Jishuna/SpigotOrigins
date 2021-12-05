package me.jishuna.spigotorigins;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.jishuna.commonlib.inventory.CustomInventoryManager;
import me.jishuna.commonlib.language.MessageConfig;
import me.jishuna.commonlib.utils.FileUtils;
import me.jishuna.spigotorigins.api.EventManager;
import me.jishuna.spigotorigins.api.InvalidOriginException;
import me.jishuna.spigotorigins.api.Origin;
import me.jishuna.spigotorigins.api.OriginPlayerManager;
import me.jishuna.spigotorigins.api.OriginRegistry;
import me.jishuna.spigotorigins.api.ability.AbilityRegistry;

public class SpigotOrigins extends JavaPlugin {
	private static final String PATH = "Origins";

	private OriginRegistry originRegistry;
	private OriginPlayerManager playerManager;
	private AbilityRegistry abilityRegistry;
	private MessageConfig messageConfig;

	private CustomInventoryManager inventoryManager;

	@Override
	public void onEnable() {
		loadConfiguration();

		new EventManager(this);

		this.abilityRegistry = new AbilityRegistry();
		this.originRegistry = new OriginRegistry();
		this.playerManager = new OriginPlayerManager(this);

		this.inventoryManager = new CustomInventoryManager();

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this.playerManager, this);
		pm.registerEvents(this.inventoryManager, this);
		
		new TickingAbilityRunnable(this.playerManager).runTaskTimer(this, 0, 10);

		loadOrigins();
	}

	public void loadOrigins() {
		File originFolder = new File(this.getDataFolder(), PATH);
		if (!originFolder.exists()) {
			originFolder.mkdirs();
			FileUtils.copyDefaults(this, PATH, name -> FileUtils.loadResourceFile(this, name));
		}

		this.loadRecursively(originFolder);
	}

	private void loadRecursively(File folder) {
		for (File file : folder.listFiles()) {
			String name = file.getName();

			if (file.isDirectory())
				loadRecursively(file);

			if (!name.endsWith(".yml"))
				continue;

			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			try {
				Origin origin = new Origin(this, config);
				this.originRegistry.registerOrigin(origin);
			} catch (InvalidOriginException ex) {
				String originName = config.getString("name", "Unknown");
				ex.addAdditionalInfo("Error while parsing origin \"" + originName + "\":");
				ex.log(getLogger());
			}
		}
	}

	public void loadConfiguration() {
		if (!this.getDataFolder().exists())
			this.getDataFolder().mkdirs();

		FileUtils.loadResourceFile(this, "messages.yml")
				.ifPresent(file -> this.messageConfig = new MessageConfig(file));
	}

	public AbilityRegistry getAbilityRegistry() {
		return abilityRegistry;
	}

	public OriginRegistry getOriginRegistry() {
		return originRegistry;
	}

	public OriginPlayerManager getPlayerRegistry() {
		return playerManager;
	}

	public CustomInventoryManager getInventoryManager() {
		return inventoryManager;
	}

	public MessageConfig getMessageConfig() {
		return messageConfig;
	}
}
