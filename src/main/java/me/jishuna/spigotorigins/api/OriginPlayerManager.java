package me.jishuna.spigotorigins.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.jishuna.spigotorigins.SpigotOrigins;

public class OriginPlayerManager implements Listener {
	private final SpigotOrigins plugin;
	private final Map<UUID, OriginPlayer> playerMap = new HashMap<>();

	public OriginPlayerManager(SpigotOrigins plugin) {
		this.plugin = plugin;
	}

	public OriginPlayer getOriginPlayer(Player player) {
		return getOriginPlayer(player.getUniqueId());
	}

	public OriginPlayer getOriginPlayer(UUID id) {
		return this.playerMap.get(id);
	}
	
	public Collection<OriginPlayer> getPlayers() {
		return this.playerMap.values();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		OriginPlayer originPlayer = new OriginPlayer(player, this.plugin.getOriginRegistry());

		this.playerMap.put(player.getUniqueId(), originPlayer);

		handleInitialJoin(player, originPlayer);
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		OriginPlayer originPlayer = getOriginPlayer(player);

		if (originPlayer != null) {
			originPlayer.getOrigin().ifPresent(origin -> origin.cleanupAbilities(originPlayer));
		}

		this.playerMap.remove(player.getUniqueId());
	}

	private void handleInitialJoin(Player player, OriginPlayer originPlayer) {
		if (originPlayer.getOrigin().isEmpty()) {
			OriginSelectionInventory inventory = new OriginSelectionInventory(this.plugin, originPlayer);

			Bukkit.getScheduler().runTaskTimer(this.plugin, task -> {
				if (originPlayer.getOrigin().isPresent()) {
					task.cancel();
					return;
				}

				if (player.getOpenInventory().getTopInventory() != inventory.getBukkitInventory())
					plugin.getInventoryManager().openGui(player, inventory);
			}, 1, 1);
		}
	}
}
