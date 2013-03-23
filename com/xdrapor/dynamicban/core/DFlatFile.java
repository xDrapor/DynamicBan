package com.xdrapor.dynamicban.core;

import org.bukkit.event.player.PlayerLoginEvent;

/**
 * Class to assist with local storage.
 * @author xDrapor
 */
public class DFlatFile extends DCore
{
	
	//TODO: Handle flatfile.
	
	public DFlatFile() 
	{

	}

	public void savePlayerData(PlayerLoginEvent e)
	{
		
	}

	public boolean isBanned(String username)
	{
		return false;
	}
}
