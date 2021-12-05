package me.jishuna.spigotorigins.api.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.util.Vector;

import me.jishuna.spigotorigins.api.OriginPlayer;
import me.jishuna.spigotorigins.api.RegisterAbility;;

@RegisterAbility(name = "fang_wave")
public class IllagerFangAbility extends Ability implements SetupAbility {
	private final Map<UUID, Long> cooldowns = new HashMap<>();

	public IllagerFangAbility(String[] data) {
		addEventHandler(PlayerSwapHandItemsEvent.class, this::onAbilityToggle);
	}

	private void onAbilityToggle(PlayerSwapHandItemsEvent event, OriginPlayer originPlayer) {
		Player player = event.getPlayer();

		if (!player.isSneaking())
			return;

		event.setCancelled(true);

		Long cooldown = this.cooldowns.get(player.getUniqueId());

		if (cooldown != null && cooldown > System.currentTimeMillis())
			return;

		Vector vector = player.getEyeLocation().getDirection().setY(0).normalize();
		Location location = player.getLocation();

		for (int i = 0; i < 8; i++) {
			location.add(vector);
			location.setY(player.getLocation().getBlockY() + 3);

			for (int j = 0; j < 6; j++) {
				if (!location.getBlock().isPassable())
					break;
				
				location.subtract(0, 1, 0);
			}

			EvokerFangs fangs = location.getWorld().spawn(location.add(0, 1, 0), EvokerFangs.class);
			fangs.setOwner(player);
		}
		cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + 3000);
	}

	@Override
	public void onSetup(OriginPlayer originPlayer) {
	}

	@Override
	public void onCleanup(OriginPlayer originPlayer) {
		this.cooldowns.remove(originPlayer.getPlayer().getUniqueId());
	}
}
