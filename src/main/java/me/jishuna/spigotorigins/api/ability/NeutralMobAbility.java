package me.jishuna.spigotorigins.api.ability;

import static me.jishuna.spigotorigins.api.ParseUtils.checkLength;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import me.jishuna.spigotorigins.api.InvalidOriginException;
import me.jishuna.spigotorigins.api.OriginPlayer;
import me.jishuna.spigotorigins.api.RegisterAbility;;

@RegisterAbility(name = "neutral")
public class NeutralMobAbility extends Ability {
	private static final Set<String> ALL_TYPES = Arrays.stream(EntityType.values()).map(Enum::toString)
			.collect(Collectors.toSet());

	private final Set<EntityType> types = EnumSet.noneOf(EntityType.class);

	public NeutralMobAbility(String[] data) throws InvalidOriginException {
		checkLength(data, 1);

		for (int i = 0; i < data.length; i++) {
			String typeString = data[i].toUpperCase();
			if (!ALL_TYPES.contains(typeString))
				throw new InvalidOriginException(
						"Invalid entity type: " + typeString + " Valid types are: " + String.join(", ", ALL_TYPES));
			this.types.add(EntityType.valueOf(typeString));
		}

		addEventHandler(EntityTargetLivingEntityEvent.class, this::onTarget);
	}

	private void onTarget(EntityTargetLivingEntityEvent event, OriginPlayer originPlayer) {
		if (event.getReason() == TargetReason.CLOSEST_PLAYER && this.types.contains(event.getEntityType()))
			event.setCancelled(true);
	}
}
