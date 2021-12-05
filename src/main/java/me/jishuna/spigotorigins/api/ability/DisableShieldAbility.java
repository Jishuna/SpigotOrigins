package me.jishuna.spigotorigins.api.ability;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.jishuna.spigotorigins.api.InvalidOriginException;
import me.jishuna.spigotorigins.api.OriginPlayer;
import me.jishuna.spigotorigins.api.RegisterAbility;;

@RegisterAbility(name = "disable_shield")
public class DisableShieldAbility extends Ability {

	public DisableShieldAbility(String[] data) throws InvalidOriginException {
		addEventHandler(PlayerInteractEvent.class, this::onInteract);
	}

	private void onInteract(PlayerInteractEvent event, OriginPlayer originPlayer) {
		if (event.getItem() == null || event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		ItemStack item = event.getItem();

		if (item.getType() == Material.SHIELD) {
			Player player = event.getPlayer();

			player.getWorld().dropItem(player.getLocation(), item.clone());
			event.setUseItemInHand(Result.DENY);
			item.setAmount(0);
		}
	}
}
