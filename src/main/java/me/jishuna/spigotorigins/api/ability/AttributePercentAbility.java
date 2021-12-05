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

@RegisterAbility(name = "attribute_percent")
public class AttributePercentAbility extends Ability implements SetupAbility {

	private final String name;
	private final Attribute attribute;
	private final AttributeModifier modifier;

	public AttributePercentAbility(String[] data) throws InvalidOriginException {
		checkLength(data, 1);

		this.attribute = Attribute.valueOf(data[0].toUpperCase());
		this.name = "sop:" + data[0];
		
		double amount = readDouble(data[1], "amount");
		this.modifier = new AttributeModifier(this.name, amount, Operation.ADD_SCALAR);
	}

	@Override
	public void onSetup(OriginPlayer originPlayer) {
		Player player = originPlayer.getPlayer();
		AttributeInstance instance = player.getAttribute(this.attribute);

		cleanupAttributes(player, instance);
		instance.addModifier(this.modifier);
	}

	@Override
	public void onCleanup(OriginPlayer originPlayer) {
		Player player = originPlayer.getPlayer();

		cleanupAttributes(player, player.getAttribute(this.attribute));
	}

	private void cleanupAttributes(Player player, AttributeInstance instance) {
		for (AttributeModifier modifier : instance.getModifiers()) {
			if (modifier.getName().equals(this.name))
				instance.removeModifier(modifier);
		}
	}
}
