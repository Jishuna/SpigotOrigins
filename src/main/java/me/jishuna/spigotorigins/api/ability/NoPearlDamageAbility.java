package me.jishuna.spigotorigins.api.ability;

import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.jishuna.actionconfiglib.ActionContext;
import me.jishuna.actionconfiglib.ArgumentFormat;
import me.jishuna.actionconfiglib.ConfigurationEntry;
import me.jishuna.actionconfiglib.effects.Effect;
import me.jishuna.actionconfiglib.enums.EntityTarget;
import me.jishuna.actionconfiglib.exceptions.ParsingException;;

@ArgumentFormat(format = { "target" })
public class NoPearlDamageAbility extends Effect {
	private final EntityTarget target;

	public NoPearlDamageAbility(ConfigurationEntry entry) throws ParsingException {
		this.target = EntityTarget.fromString(entry.getString("target"));
	}

	@Override
	public void evaluate(ActionContext context) {
		if (!(context.getEvent()instanceof PlayerTeleportEvent event) || event.getCause() != TeleportCause.ENDER_PEARL)
			return;

		context.getLivingTargetOptional(this.target).ifPresent(entity -> {
			entity.teleport(event.getTo());
			event.setCancelled(true);
		});
	}
}
