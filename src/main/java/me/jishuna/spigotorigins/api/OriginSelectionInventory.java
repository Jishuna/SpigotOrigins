package me.jishuna.spigotorigins.api;

import me.jishuna.commonlib.inventory.CustomInventory;
import me.jishuna.spigotorigins.SpigotOrigins;

public class OriginSelectionInventory extends CustomInventory {
	private final OriginPlayer player;

	public OriginSelectionInventory(SpigotOrigins plugin, OriginPlayer player) {
		super(null, 54, plugin.getMessageConfig().getString("select-origin"));
		this.player = player;

		this.addClickConsumer(event -> event.setCancelled(true));

		int index = 0;
		for (Origin origin : plugin.getOriginRegistry().getOrigins()) {
			this.addButton(index, origin.getDisplayItem(), event -> {
				this.player.setOrigin(origin);
				event.getWhoClicked().closeInventory();
			});
			index++;
		}

	}
}
