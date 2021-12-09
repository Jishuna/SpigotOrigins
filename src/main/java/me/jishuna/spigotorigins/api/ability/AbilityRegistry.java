package me.jishuna.spigotorigins.api.ability;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.file.YamlConfiguration;

import me.jishuna.actionconfiglib.Action;
import me.jishuna.actionconfiglib.ActionConfigLib;
import me.jishuna.commonlib.utils.FileUtils;
import me.jishuna.spigotorigins.SpigotOrigins;

public class AbilityRegistry {
	private static final String PATH = "Abilities";
	
	private final Map<String, Action> abilityMap = new HashMap<>();
	private final ActionConfigLib lib;

	public AbilityRegistry(SpigotOrigins plugin) {
		this.lib = plugin.getActionLib();
		reloadAbilities(plugin);
	}
	
	public Action getAbility(String key) {
		return this.abilityMap.get(key);
	}

	public void reloadAbilities(SpigotOrigins plugin) {
		File abilityFolder = new File(plugin.getDataFolder(), PATH);
		if (!abilityFolder.exists()) {
			abilityFolder.mkdirs();
			FileUtils.copyDefaults(plugin, PATH, name -> FileUtils.loadResourceFile(plugin, name));
		}

		this.loadRecursively(abilityFolder);
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
				Action action = lib.parseAction(config);
				if (action != null)
					registerAbility(action);
			} catch (Exception ex) {
				System.err.println("AHHHHH");
			}
		}
	}

	public void registerAbility(Action action) {
		this.abilityMap.put(action.getName(), action);
	}

	public Set<String> getAbilities() {
		return this.abilityMap.keySet();
	}
}
