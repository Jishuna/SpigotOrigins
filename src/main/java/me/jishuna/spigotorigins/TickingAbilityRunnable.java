package me.jishuna.spigotorigins;

import org.bukkit.scheduler.BukkitRunnable;

import me.jishuna.actionconfiglib.ActionContext;
import me.jishuna.actionconfiglib.triggers.TriggerRegistry;
import me.jishuna.spigotorigins.api.OriginPlayerManager;

public class TickingAbilityRunnable extends BukkitRunnable {

	private final OriginPlayerManager manager;

	public TickingAbilityRunnable(OriginPlayerManager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		manager.getPlayers().forEach(player -> {
			ActionContext context = new ActionContext.Builder(TriggerRegistry.TICK).user(player.getPlayer()).build();
			player.handleContext(context);
		});
	}

}
