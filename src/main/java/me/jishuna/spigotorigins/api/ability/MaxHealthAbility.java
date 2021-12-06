package me.jishuna.spigotorigins.api.ability;

import static me.jishuna.spigotorigins.api.ParseUtils.checkLength;
import static me.jishuna.spigotorigins.api.ParseUtils.readDouble;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;

import me.jishuna.spigotorigins.api.InvalidOriginException;
import me.jishuna.spigotorigins.api.OriginPlayer;
import me.jishuna.spigotorigins.api.RegisterAbility;;

@RegisterAbility(name = "max_health")
public class MaxHealthAbility extends Ability implements SetupAbility {
	private static final String NAME = "so:max_health";
	
	private final double amount;
	private final AttributeModifier modifier;

	public MaxHealthAbility(String[] data) throws InvalidOriginException {
		checkLength(data, 1);

		this.amount = readDouble(data[0], "amount");
		this.modifier = new AttributeModifier(NAME, this.amount - 20, Operation.ADD_NUMBER);
	}

	@Override
	public void onSetup(OriginPlayer originPlayer) {
		Player player = originPlayer.getPlayer();
		AttributeInstance instance = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);

		cleanupAttributes(player, instance);
		instance.addModifier(this.modifier);
		
		player.setHealthScale(this.amount);
		player.setHealthScaled(true);
	}

	@Override
	public void onCleanup(OriginPlayer originPlayer) {
		Player player = originPlayer.getPlayer();

		cleanupAttributes(player, player.getAttribute(Attribute.GENERIC_MAX_HEALTH));
		
		player.setHealthScale(20);
		player.setHealthScaled(false);
	}

	private void cleanupAttributes(Player player, AttributeInstance instance) {
		for (AttributeModifier modifier : instance.getModifiers()) {
			if (modifier.getName().equals(NAME))
				instance.removeModifier(modifier);
		}
	}
}