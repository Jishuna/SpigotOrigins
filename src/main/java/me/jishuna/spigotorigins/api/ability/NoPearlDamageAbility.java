package me.jishuna.spigotorigins.api.ability;

import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.jishuna.spigotorigins.api.InvalidOriginException;
import me.jishuna.spigotorigins.api.OriginPlayer;
import me.jishuna.spigotorigins.api.RegisterAbility;;

@RegisterAbility(name = "no_pearl_damage")
public class NoPearlDamageAbility extends Ability {

	public NoPearlDamageAbility(String[] data) throws InvalidOriginException {
		addEventHandler(PlayerTeleportEvent.class, this::onTeleport);
	}

	private void onTeleport(PlayerTeleportEvent event, OriginPlayer originPlayer) {
		if (event.getCause() == TeleportCause.ENDER_PEARL) {
			event.setCancelled(true);
			event.getPlayer().teleport(event.getTo());
		}
	}
}
