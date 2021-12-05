package me.jishuna.spigotorigins.api.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import me.jishuna.spigotorigins.api.OriginPlayer;
import me.jishuna.spigotorigins.api.RegisterAbility;;

@RegisterAbility(name = "ender_pearl")
public class EnderPearlAbility extends Ability implements SetupAbility {
	private final Map<UUID, UUID> pearls = new HashMap<>();

	public EnderPearlAbility(String[] data) {
		addEventHandler(PlayerSwapHandItemsEvent.class, this::onAbilityToggle);
	}

	private void onAbilityToggle(PlayerSwapHandItemsEvent event, OriginPlayer originPlayer) {
		Player player = event.getPlayer();

		if (!player.isSneaking())
			return;

		event.setCancelled(true);

		UUID pearlId = pearls.get(player.getUniqueId());

		if (pearlId != null) {
			Entity pearl = Bukkit.getEntity(pearlId);

			if (!(pearl == null || !pearl.isValid()))
				return;
		}

		Entity pearl = player.launchProjectile(EnderPearl.class);
		player.playSound(player.getLocation(), Sound.ENTITY_ENDER_PEARL_THROW, 0.5f, 0f);

		pearls.put(player.getUniqueId(), pearl.getUniqueId());
	}

	@Override
	public void onSetup(OriginPlayer originPlayer) {
	}

	@Override
	public void onCleanup(OriginPlayer originPlayer) {
		this.pearls.remove(originPlayer.getPlayer().getUniqueId());
	}
}
