package me.jishuna.spigotorigins.api;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import me.jishuna.actionconfiglib.ActionContext;

public class OriginPlayer {

	private final Player player;
	private Origin origin;

	public OriginPlayer(Player player, OriginRegistry registry) {
		this.player = player;
		this.origin = registry.getOrigin(player.getPersistentDataContainer().getOrDefault(PluginKeys.ORIGIN.getKey(),
				PersistentDataType.STRING, ""));

		this.getOrigin().ifPresent(origin -> origin.setupAbilities(this));
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

	public void setOrigin(Origin origin) {
		if (this.origin != null)
			this.origin.cleanupAbilities(this);

		this.origin = origin;

		if (this.origin != null)
			this.origin.setupAbilities(this);

//		this.getPlayer().getPersistentDataContainer().set(PluginKeys.ORIGIN.getKey(), PersistentDataType.STRING,
//				origin.getName());
	}

}
