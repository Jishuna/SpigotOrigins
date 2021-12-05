package me.jishuna.spigotorigins.api.ability;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.jishuna.spigotorigins.api.InvalidOriginException;
import me.jishuna.spigotorigins.api.OriginPlayer;
import me.jishuna.spigotorigins.api.RegisterAbility;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.damagesource.DamageSource;;

@RegisterAbility(name = "aquatic")
public class AquaticAbility extends Ability implements SetupAbility {

	public AquaticAbility(String[] data) throws InvalidOriginException {
		addEventHandler(EntityAirChangeEvent.class, this::onAirChange);
		addEventHandler(PlayerRespawnEvent.class, this::onRespawn);
	}

	private void onRespawn(PlayerRespawnEvent event, OriginPlayer originPlayer) {
		Bukkit.getScheduler().runTask(originPlayer.getOrigin().get().getPlugin(),
				() -> event.getPlayer().setRemainingAir(event.getPlayer().getMaximumAir() - 1));
	}

	private void onAirChange(EntityAirChangeEvent event, OriginPlayer originPlayer) {
		Player player = (Player) event.getEntity();
		int oldAir = player.getRemainingAir();
		int newAir = event.getAmount();

		int air = 0;
		if (oldAir > newAir) {
			air = Math.min(oldAir + 4, player.getMaximumAir() - 1);

			if (air == player.getMaximumAir() - 1)
				player.addPotionEffect(
						new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, true, false));
		} else {
			air = Math.max(oldAir - 1, -20);
			player.removePotionEffect(PotionEffectType.WATER_BREATHING);
		}

		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

		if (air == -20) {
			air = 0;
			entityPlayer.damageEntity(DamageSource.h, 2f);
		}

		event.setAmount(air);
	}

	@Override
	public void onSetup(OriginPlayer originPlayer) {
		Player player = originPlayer.getPlayer();

		if (player.getRemainingAir() >= player.getMaximumAir())
			player.setRemainingAir(player.getMaximumAir() - 1);
	}

	@Override
	public void onCleanup(OriginPlayer originPlayer) {
		originPlayer.getPlayer().removePotionEffect(PotionEffectType.WATER_BREATHING);
	}
}
