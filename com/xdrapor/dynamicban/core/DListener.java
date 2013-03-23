package com.xdrapor.dynamicban.core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


/**
 * A Master listener for the plugin.
 * @author xDrapor
 */
public class DListener extends DCore implements Listener
{
	@EventHandler (priority = EventPriority.MONITOR)
	public void storePlayerData(PlayerJoinEvent e)
	{
		//Calls the DStorage.savePlayerData() method.
		//Saves player data to the MySQL database or FlatFile depending on the user's preference.
		storageHandler.savePlayerData(e);
	}
}
