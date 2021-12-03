package me.jishuna.spigotorigins.api.ability;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.jishuna.spigotorigins.api.OriginPlayer;
import me.jishuna.spigotorigins.api.RegisterAbility;;

@RegisterAbility(name = "shell_shield")
public class ShellShieldAbility extends Ability {
	private final Set<UUID> shielding = new HashSet<>();

	public ShellShieldAbility(String[] data) {
		addEventHandler(PlayerSwapHandItemsEvent.class, this::onAbilityToggle);
		addEventHandler(PlayerDeathEvent.class, this::onPlayerDeath);
	}
	
	private void onPlayerDeath(PlayerDeathEvent event, OriginPlayer originPlayer) {
		this.shielding.remove(event.getEntity().getUniqueId());
	}

	private void onAbilityToggle(PlayerSwapHandItemsEvent event, OriginPlayer originPlayer) {
		Player player = event.getPlayer();

		if (!player.isSneaking())
			return;

		event.setCancelled(true);

		if (shielding.contains(player.getUniqueId())) {
			disableShield(player);
		} else {
			enableShield(player);
		}
	}

	private void enableShield(Player player) {
		this.shielding.add(player.getUniqueId());

		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 50));
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128));
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 3));

		player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_CLOSE, 1f, 1f);
	}

	private void disableShield(Player player) {
		this.shielding.remove(player.getUniqueId());

		player.removePotionEffect(PotionEffectType.SLOW);
		player.removePotionEffect(PotionEffectType.JUMP);
		player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);

		player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_OPEN, 1f, 1f);
	}

	@Override
	public String getKey() {
		return "shell_shield";
	}

}
