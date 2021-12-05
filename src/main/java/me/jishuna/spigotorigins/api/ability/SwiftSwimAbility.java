package me.jishuna.spigotorigins.api.ability;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.jishuna.spigotorigins.api.InvalidOriginException;
import me.jishuna.spigotorigins.api.OriginPlayer;
import me.jishuna.spigotorigins.api.RegisterAbility;;

@RegisterAbility(name = "swift_swim")
public class SwiftSwimAbility extends Ability {
	private static final PotionEffect SWIM_EFFECT = new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 30, 0, true,
			false);

	public SwiftSwimAbility(String[] data) throws InvalidOriginException {
		addEventHandler(PlayerMoveEvent.class, this::onMove);
	}

	private void onMove(PlayerMoveEvent event, OriginPlayer originPlayer) {
		Player player = event.getPlayer();
		
		if (player.getEyeLocation().getBlock().getType() == Material.WATER) {
			player.addPotionEffect(SWIM_EFFECT);
		}
	}
}
