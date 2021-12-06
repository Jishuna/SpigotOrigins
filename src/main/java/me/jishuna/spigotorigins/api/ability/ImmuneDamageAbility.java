package me.jishuna.spigotorigins.api.ability;

import static me.jishuna.spigotorigins.api.ParseUtils.checkLength;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.jishuna.spigotorigins.api.InvalidOriginException;
import me.jishuna.spigotorigins.api.OriginPlayer;
import me.jishuna.spigotorigins.api.RegisterAbility;;

@RegisterAbility(name = "damage_immune")
public class ImmuneDamageAbility extends Ability {
	private static final Set<String> ALL_CAUSES = Arrays.stream(DamageCause.values()).map(Enum::toString)
			.collect(Collectors.toSet());

	private final Set<DamageCause> causes = new HashSet<>();

	public ImmuneDamageAbility(String[] data) throws InvalidOriginException {
		checkLength(data, 1);

		for (int i = 0; i < data.length; i++) {
			String typeString = data[i].toUpperCase();
			if (!ALL_CAUSES.contains(typeString))
				throw new InvalidOriginException(
						"Invalid damage cause: " + typeString + " Valid types are: " + String.join(", ", ALL_CAUSES));
			this.causes.add(DamageCause.valueOf(typeString));
		}

		addEventHandler(EntityDamageEvent.class, this::onDamage);
	}

	private void onDamage(EntityDamageEvent event, OriginPlayer originPlayer) {
		if (this.causes.contains(event.getCause()))
			event.setCancelled(true);
	}
}
