package me.jishuna.spigotorigins.api.ability;

import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.jishuna.spigotorigins.api.InvalidOriginException;
import me.jishuna.spigotorigins.api.OriginPlayer;
import me.jishuna.spigotorigins.api.RegisterAbility;;

@RegisterAbility(name = "no_trading")
public class NoTradeAbility extends Ability {

	public NoTradeAbility(String[] data) throws InvalidOriginException {
		addEventHandler(PlayerInteractEntityEvent.class, this::onInteract);
	}

	private void onInteract(PlayerInteractEntityEvent event, OriginPlayer originPlayer) {
		if (event.getRightClicked().getType() == EntityType.VILLAGER) {
			Player player = event.getPlayer();
			player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1f);
			event.getRightClicked().playEffect(EntityEffect.VILLAGER_ANGRY);
			
			event.setCancelled(true);
		}
	}
}
