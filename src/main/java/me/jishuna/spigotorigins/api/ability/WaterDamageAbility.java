package me.jishuna.spigotorigins.api.ability;

import org.bukkit.entity.Player;

import me.jishuna.spigotorigins.api.InvalidOriginException;
import me.jishuna.spigotorigins.api.OriginPlayer;
import me.jishuna.spigotorigins.api.RegisterAbility;;

@RegisterAbility(name = "hydrophobic")
public class WaterDamageAbility extends Ability implements TickingAbility {

	public WaterDamageAbility(String[] data) throws InvalidOriginException {
	}

	@Override
	public void tick(OriginPlayer originPlayer) {
		Player player = originPlayer.getPlayer();
		if (player.isInWater())
			player.damage(1);

	}
}
