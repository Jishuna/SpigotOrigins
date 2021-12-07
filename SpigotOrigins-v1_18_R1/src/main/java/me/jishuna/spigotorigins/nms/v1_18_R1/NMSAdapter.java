package me.jishuna.spigotorigins.nms.v1_18_R1;

import org.bukkit.craftbukkit.v1_18_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

import net.minecraft.world.damagesource.DamageSource;

public class NMSAdapter implements me.jishuna.spigotorigins.nms.NMSAdapter {

	@Override
	public void dealDrowningDamage(LivingEntity entity) {
		((CraftLivingEntity)entity).getHandle().a(DamageSource.h, 2f);
	}

}
