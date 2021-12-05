package me.jishuna.spigotorigins.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import me.jishuna.commonlib.items.ItemBuilder;
import me.jishuna.commonlib.language.MessageConfig;
import me.jishuna.spigotorigins.SpigotOrigins;
import me.jishuna.spigotorigins.api.ability.Ability;
import me.jishuna.spigotorigins.api.ability.SetupAbility;
import net.md_5.bungee.api.ChatColor;

public class Origin {
	private final SpigotOrigins plugin;

	private final String name;
	private final String displayName;
	private final List<String> description;

	private final int impact;

	private Material material;

	private final Set<Ability> abilities = new HashSet<>();

	public Origin(SpigotOrigins plugin, ConfigurationSection section) throws InvalidOriginException {
		this.plugin = plugin;

		this.name = section.getString("name");
		this.displayName = ChatColor.translateAlternateColorCodes('&', section.getString("display-name", this.name));

		this.description = section.getStringList("description").stream()
				.map(string -> ChatColor.translateAlternateColorCodes('&', string)).toList();

		this.impact = section.getInt("impact", 1);

		for (String abilityString : section.getStringList("abilities")) {
			Ability ability = plugin.getAbilityRegistry().parseString(abilityString);

			this.abilities.add(ability);
		}

		String materialName = section.getString("display-item", "none");
		this.material = Material.matchMaterial(materialName);

		if (this.material == null)
			throw new InvalidOriginException("Invalid display-item: " + materialName);
	}

	public <T extends Event> void handleAbilities(Class<T> type, T event, OriginPlayer player) {
		this.abilities
				.forEach(ability -> ability.getEventHandlers(type).forEach(wrapper -> wrapper.consume(event, player)));
	}

	public ItemStack getDisplayItem() {
		MessageConfig config = this.plugin.getMessageConfig();
		String icon = config.getString("impact-icon");

		return new ItemBuilder(this.material)
				.withName(this.displayName)
				.addLore(config.getString("impact") + getImpactColor()
						+ icon.repeat(this.impact) + ChatColor.GRAY + icon.repeat(5 - this.impact))
				.addLore("")
				.addLore(this.description)
				.build();
	}

	public void setupAbilities(OriginPlayer player) {
		this.abilities.forEach(ability -> {
			if (ability instanceof SetupAbility setup)
				setup.onSetup(player);
		});
	}

	public void cleanupAbilities(OriginPlayer player) {
		this.abilities.forEach(ability -> {
			if (ability instanceof SetupAbility setup)
				setup.onCleanup(player);
		});
	}

	public SpigotOrigins getPlugin() {
		return plugin;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getDifficulty() {
		return impact;
	}

	public Set<Ability> getAbilities() {
		return abilities;
	}

	private ChatColor getImpactColor() {
		return switch (this.impact) {
		case 1 -> ChatColor.GREEN;
		case 2 -> ChatColor.of("#aaff55");
		case 3 -> ChatColor.YELLOW;
		case 4 -> ChatColor.of("#ffaa55");
		case 5 -> ChatColor.RED;
		default -> ChatColor.GREEN;
		};
	}
}
