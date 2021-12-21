package me.jishuna.spigotorigins;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.jishuna.actionconfiglib.ActionConfigLib;
import me.jishuna.actionconfiglib.triggers.Trigger;
import me.jishuna.commonlib.inventory.CustomInventoryManager;
import me.jishuna.commonlib.language.MessageConfig;
import me.jishuna.commonlib.utils.FileUtils;
import me.jishuna.spigotorigins.api.EventManager;
import me.jishuna.spigotorigins.api.InvalidOriginException;
import me.jishuna.spigotorigins.api.Origin;
import me.jishuna.spigotorigins.api.OriginPlayerManager;
import me.jishuna.spigotorigins.api.OriginRegistry;
import me.jishuna.spigotorigins.api.ability.AquaticAbility;
import me.jishuna.spigotorigins.api.ability.FangWaveAbility;
import me.jishuna.spigotorigins.api.ability.NoPearlDamageAbility;
import me.jishuna.spigotorigins.api.ability.ShellShieldAbility;
import me.jishuna.spigotorigins.nms.NMSManager;

public class SpigotOrigins extends JavaPlugin {
	public static final Trigger ORIGIN_ADDED = new Trigger("ORIGIN_ADDED");
	public static final Trigger ORIGIN_REMOVED = new Trigger("ORIGIN_REMOVED");
	public static final Trigger AIR_LEVEL_CHANGE = new Trigger("AIR_LEVEL_CHANGE");
	public static final Trigger PLAYER_TELEPORT = new Trigger("PLAYER_TELEPORT");

	private static final String PATH = "Origins";

	private OriginRegistry originRegistry;
	private OriginPlayerManager playerManager;
	private MessageConfig messageConfig;
	private ActionConfigLib actionLib;

	private CustomInventoryManager inventoryManager;

	@Override
	public void onEnable() {
		loadConfiguration();

		this.setupActionLib();

		new EventManager(this);

		this.originRegistry = new OriginRegistry();
		this.playerManager = new OriginPlayerManager(this);

		this.inventoryManager = new CustomInventoryManager();

		NMSManager.initAdapater(this);

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

	private void setupActionLib() {
		this.actionLib = ActionConfigLib.createInstance(this);
		
		this.actionLib.getTriggerRegistry().registerTrigger(ORIGIN_ADDED);
		this.actionLib.getTriggerRegistry().registerTrigger(ORIGIN_REMOVED);
		this.actionLib.getTriggerRegistry().registerTrigger(AIR_LEVEL_CHANGE);
		this.actionLib.getTriggerRegistry().registerTrigger(PLAYER_TELEPORT);

		this.actionLib.registerEffect("SHELL_SHIELD", ShellShieldAbility.class);
		this.actionLib.registerEffect("AQUATIC", AquaticAbility.class);
		this.actionLib.registerEffect("NO_PEARL_DAMAGE", NoPearlDamageAbility.class);
		this.actionLib.registerEffect("FANG_WAVE", FangWaveAbility.class);
	}

	public void loadConfiguration() {
		if (!this.getDataFolder().exists())
			this.getDataFolder().mkdirs();

		FileUtils.loadResourceFile(this, "messages.yml")
				.ifPresent(file -> this.messageConfig = new MessageConfig(file));
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

	public ActionConfigLib getActionLib() {
		return actionLib;
	}

	public MessageConfig getMessageConfig() {
		return messageConfig;
	}
}
