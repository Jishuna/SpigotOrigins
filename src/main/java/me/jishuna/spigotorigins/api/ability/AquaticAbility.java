package me.jishuna.spigotorigins.api.ability;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.jishuna.actionconfiglib.ActionContext;
import me.jishuna.actionconfiglib.ArgumentFormat;
import me.jishuna.actionconfiglib.ConfigurationEntry;
import me.jishuna.actionconfiglib.effects.Effect;
import me.jishuna.actionconfiglib.enums.EntityTarget;
import me.jishuna.actionconfiglib.exceptions.ParsingException;
import me.jishuna.spigotorigins.nms.NMSManager;;

@ArgumentFormat(format = { "target" })
public class AquaticAbility extends Effect {
	private final EntityTarget target;

	public AquaticAbility(ConfigurationEntry entry) throws ParsingException {
		this.target = EntityTarget.fromString(entry.getString("target"));
	}

	@Override
	public void evaluate(ActionContext context) {
		if (!(context.getEvent()instanceof EntityAirChangeEvent event))
			return;

		LivingEntity entity = context.getLivingTarget(this.target);
		int oldAir = entity.getRemainingAir();
		int newAir = event.getAmount();

		int air = 0;
		if (oldAir > newAir) {
			air = Math.min(oldAir + 4, entity.getMaximumAir() - 1);

			if (air == entity.getMaximumAir() - 1)
				entity.addPotionEffect(
						new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, true, false));
		} else {
			air = Math.max(oldAir - 1, -20);
			entity.removePotionEffect(PotionEffectType.WATER_BREATHING);
		}

		if (air == -20) {
			air = 0;
			NMSManager.getAdapter().dealDrowningDamage(entity);
		}

		event.setAmount(air);
	}
}
