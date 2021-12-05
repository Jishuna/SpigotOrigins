package me.jishuna.spigotorigins.api.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.jishuna.spigotorigins.api.OriginPlayer;
import me.jishuna.spigotorigins.api.RegisterAbility;;

@RegisterAbility(name = "shell_shield")
public class ShellShieldAbility extends Ability implements SetupAbility {
	private final Map<UUID, Float> shielding = new HashMap<>();

	public ShellShieldAbility(String[] data) {
		addEventHandler(PlayerSwapHandItemsEvent.class, this::onAbilityToggle);
		addEventHandler(PlayerDeathEvent.class, this::onPlayerDeath);
		addEventHandler(PlayerMoveEvent.class, this::onMove);
	}

	private void onMove(PlayerMoveEvent event, OriginPlayer originPlayer) {
		if (!shielding.containsKey(event.getPlayer().getUniqueId()))
			return;

		Location to = event.getTo();
		Location from = event.getFrom();

		to.setX(from.getX());
		to.setZ(from.getZ());
	}

	private void onPlayerDeath(PlayerDeathEvent event, OriginPlayer originPlayer) {
		this.shielding.remove(event.getEntity().getUniqueId());
	}

	private void onAbilityToggle(PlayerSwapHandItemsEvent event, OriginPlayer originPlayer) {
		Player player = event.getPlayer();

		if (!player.isSneaking())
			return;

		event.setCancelled(true);

		if (shielding.containsKey(player.getUniqueId())) {
			disableShield(player);
		} else {
			enableShield(player);
		}
	}

	@Override
	public void onSetup(OriginPlayer originPlayer) {
	}

	@Override
	public void onCleanup(OriginPlayer originPlayer) {
		this.shielding.remove(originPlayer.getPlayer().getUniqueId());
	}

	private void enableShield(Player player) {
		this.shielding.put(player.getUniqueId(), player.getWalkSpeed());
		player.setWalkSpeed(0f);

		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128, true, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 3, true));

		player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_CLOSE, 0.5f, 0f);
	}

	private void disableShield(Player player) {
		Float speed = this.shielding.remove(player.getUniqueId());
		if (speed == null)
			return;

		player.setWalkSpeed(speed);
		player.removePotionEffect(PotionEffectType.JUMP);
		player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);

		player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_OPEN, 0.5f, 0f);
	}
}
