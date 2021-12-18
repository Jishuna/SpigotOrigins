package me.jishuna.spigotorigins.api;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import me.jishuna.actionconfiglib.ActionContext;
import me.jishuna.spigotorigins.SpigotOrigins;

public class OriginPlayer {

	private final Player player;
	private Origin origin;

	public OriginPlayer(Player player, OriginRegistry registry) {
		this.player = player;
		this.origin = registry.getOrigin(player.getPersistentDataContainer().getOrDefault(PluginKeys.ORIGIN.getKey(),
				PersistentDataType.STRING, ""));

		this.setupOrigin();
	}

	public void handleContext(ActionContext context) {
		this.getOrigin().ifPresent(origin -> origin.handleAbilities(context));
	}

	public Player getPlayer() {
		return player;
	}

	public Optional<Origin> getOrigin() {
		return Optional.ofNullable(this.origin);
	}

	public void setupOrigin() {
		if (this.origin != null)
			this.origin
					.handleAbilities(new ActionContext.Builder(SpigotOrigins.ORIGIN_ADDED).user(getPlayer()).build());
	}

	public void cleanupOrigin() {
		if (this.origin != null)
			this.origin
					.handleAbilities(new ActionContext.Builder(SpigotOrigins.ORIGIN_REMOVED).user(getPlayer()).build());
	}

	public void setOrigin(Origin origin) {
		cleanupOrigin();

		this.origin = origin;

		setupOrigin();

//		this.getPlayer().getPersistentDataContainer().set(PluginKeys.ORIGIN.getKey(), PersistentDataType.STRING,
//				origin.getName());
	}

}
