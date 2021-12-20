package me.jishuna.spigotorigins.api.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.jishuna.actionconfiglib.ActionContext;
import me.jishuna.actionconfiglib.ArgumentFormat;
import me.jishuna.actionconfiglib.ConfigurationEntry;
import me.jishuna.actionconfiglib.effects.Effect;
import me.jishuna.actionconfiglib.enums.EntityTarget;
import me.jishuna.actionconfiglib.exceptions.ParsingException;
import me.jishuna.spigotorigins.SpigotOrigins;;

@ArgumentFormat(format = { "target" })
public class ShellShieldAbility extends Effect {
	private final Map<UUID, Float> shielding = new HashMap<>();
	private final EntityTarget target;

	public ShellShieldAbility(ConfigurationEntry entry) throws ParsingException {
		this.target = EntityTarget.fromString(entry.getString("target"));
	}

	@Override
	public void evaluate(ActionContext context) {
		LivingEntity entity = context.getLivingTarget(this.target);

		if (!(entity instanceof Player player))
			return;

		if (context.getTrigger() == SpigotOrigins.ORIGIN_REMOVED) {
			if (shielding.containsKey(player.getUniqueId()))
				disableShield(player);
		} else {
			if (shielding.containsKey(player.getUniqueId())) {
				disableShield(player);
			} else {
				enableShield(player);
			}
		}
	}

	private void enableShield(Player player) {
		this.shielding.put(player.getUniqueId(), player.getWalkSpeed());
		player.setWalkSpeed(0f);

		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128, true, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 3, true));

		player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_CLOSE, 1f, 0f);
	}

	private void disableShield(Player player) {
		Float speed = this.shielding.remove(player.getUniqueId());
		if (speed == null)
			return;

		player.setWalkSpeed(speed);
		player.removePotionEffect(PotionEffectType.JUMP);
		player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);

		player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_OPEN, 1f, 0f);
	}
}
