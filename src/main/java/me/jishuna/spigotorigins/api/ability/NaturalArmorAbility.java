package me.jishuna.spigotorigins.api.ability;

import static me.jishuna.spigotorigins.api.ParseUtils.checkLength;
import static me.jishuna.spigotorigins.api.ParseUtils.readInt;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;

import me.jishuna.spigotorigins.api.InvalidOriginException;
import me.jishuna.spigotorigins.api.OriginPlayer;
import me.jishuna.spigotorigins.api.RegisterAbility;;

@RegisterAbility(name = "natural_armor")
public class NaturalArmorAbility extends Ability implements SetupAbility {
	private static final String ATTRIBUTE_NAME = "spigotorigins:natural_armor";

	private final AttributeModifier modifier;

	public NaturalArmorAbility(String[] data) throws InvalidOriginException {
		checkLength(data, 1);

		int amount = readInt(data[0], "amount");
		this.modifier = new AttributeModifier(ATTRIBUTE_NAME, amount, Operation.ADD_NUMBER);
	}

	@Override
	public void onSetup(OriginPlayer originPlayer) {
		Player player = originPlayer.getPlayer();
		AttributeInstance armor = player.getAttribute(Attribute.GENERIC_ARMOR);

		cleanupAttributes(player, armor);
		armor.addModifier(this.modifier);
	}

	@Override
	public void onCleanup(OriginPlayer originPlayer) {
		Player player = originPlayer.getPlayer();

		cleanupAttributes(player, player.getAttribute(Attribute.GENERIC_ARMOR));
	}

	private void cleanupAttributes(Player player, AttributeInstance instance) {
		for (AttributeModifier modifier : instance.getModifiers()) {
			if (modifier.getName().equals(ATTRIBUTE_NAME))
				instance.removeModifier(modifier);
		}
	}

	@Override
	public String getKey() {
		return "natural_armor";
	}

}
