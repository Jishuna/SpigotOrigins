package me.jishuna.spigotorigins.api.ability;

import org.bukkit.Location;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.util.Vector;

import me.jishuna.actionconfiglib.ActionContext;
import me.jishuna.actionconfiglib.ArgumentFormat;
import me.jishuna.actionconfiglib.ConfigurationEntry;
import me.jishuna.actionconfiglib.effects.Effect;
import me.jishuna.actionconfiglib.enums.EntityTarget;
import me.jishuna.actionconfiglib.exceptions.ParsingException;;

@ArgumentFormat(format = { "target" })
public class FangWaveAbility extends Effect {
	private final EntityTarget target;

	public FangWaveAbility(ConfigurationEntry entry) throws ParsingException {
		this.target = EntityTarget.fromString(entry.getString("target"));
	}

	@Override
	public void evaluate(ActionContext context) {
		context.getLivingTargetOptional(this.target).ifPresent(entity -> {		
			Vector vector = entity.getEyeLocation().getDirection().setY(0).normalize();
			Location location = entity.getLocation();

			for (int i = 0; i < 8; i++) {
				location.add(vector);
				location.setY(entity.getLocation().getBlockY() + 3);

				for (int j = 0; j < 6; j++) {
					if (!location.getBlock().isPassable())
						break;

					location.subtract(0, 1, 0);
				}

				EvokerFangs fangs = location.getWorld().spawn(location.add(0, 1, 0), EvokerFangs.class);
				fangs.setOwner(entity);
			}
		});
	}
}
