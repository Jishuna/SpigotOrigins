package me.jishuna.spigotorigins;

import org.bukkit.scheduler.BukkitRunnable;

import me.jishuna.spigotorigins.api.OriginPlayerManager;
import me.jishuna.spigotorigins.api.ability.TickingAbility;

public class TickingAbilityRunnable extends BukkitRunnable {

	private final OriginPlayerManager manager;

	public TickingAbilityRunnable(OriginPlayerManager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		manager.getPlayers().forEach(player -> {
			player.getOrigin().ifPresent(origin -> {
				origin.getAbilities().forEach(ability -> {
					if (ability instanceof TickingAbility ticking) {
						ticking.tick(player);
					}
				});
			});
		});
	}

}
