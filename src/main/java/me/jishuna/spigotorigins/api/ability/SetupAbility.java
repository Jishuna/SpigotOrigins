package me.jishuna.spigotorigins.api.ability;

import me.jishuna.spigotorigins.api.OriginPlayer;

public interface SetupAbility {

	public void onSetup(OriginPlayer originPlayer);

	public void onCleanup(OriginPlayer originPlayer);
}
