package me.jishuna.spigotorigins.api.ability;

import static me.jishuna.spigotorigins.api.ParseUtils.checkLength;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

import me.jishuna.spigotorigins.api.InvalidOriginException;
import me.jishuna.spigotorigins.api.OriginPlayer;
import me.jishuna.spigotorigins.api.RegisterAbility;;

@RegisterAbility(name = "effect_immune")
public class ImmuneEffectAbility extends Ability {
	private static final Set<String> ALL_TYPES = Arrays.stream(PotionEffectType.values()).map(type -> type.getName())
			.collect(Collectors.toSet());

	private final Set<PotionEffectType> types = new HashSet<>();

	public ImmuneEffectAbility(String[] data) throws InvalidOriginException {
		checkLength(data, 1);

		for (int i = 0; i < data.length; i++) {
			String typeString = data[i].toUpperCase();
			if (!ALL_TYPES.contains(typeString))
				throw new InvalidOriginException(
						"Invalid effect type: " + typeString + " Valid types are: " + String.join(", ", ALL_TYPES));
			this.types.add(PotionEffectType.getByName(typeString));
		}

		addEventHandler(EntityPotionEffectEvent.class, this::onEffect);
	}

	private void onEffect(EntityPotionEffectEvent event, OriginPlayer originPlayer) {
		if (event.getNewEffect() == null)
			return;

		if (this.types.contains(event.getNewEffect().getType()))
			event.setCancelled(true);
	}
}
