package com.xdrapor.dynamicban.core;

import org.bukkit.event.player.PlayerJoinEvent;
import static com.xdrapor.dynamicban.DynamicBan.instance;


/**
 * A class that wraps both the flatfile class and the database class into one class.
 * @author xDrapor
 */
public class DStorage 
{
	/** Variable to store whether we are using the database at init or not **/
	private static boolean usingDatabase;

	/** Define a DDatabase object **/
	private static DDatabase database; 

	/** Define a DFlatFile object **/
	private static DFlatFile flatFile;

	/**
	 * Initialize needed elements.
	 */
	public void init()
	{
		//Set the usingDatabase variable to the value found in the configuration.
		usingDatabase = instance.getConfig().getBoolean("mysql.enabled");
		//Instantiate the DFlatFile class.
		//We instantiate flatFile as a backup irrespective of configuration.
		flatFile	  = new DFlatFile();
		//If we are using the database
		if(usingDatabase)
		{
			//Instantiate the DDatabase class
			database  = new DDatabase();
		}
	}

	/**
	 * Closes streams and connections.
	 */
	public void close()
	{
		//If we are using the database
		if(usingDatabase)
		{
			//Close all connections.
			database.closeConnections();
		}
	}
	
	/**
	 * Saves player data to the configured storage method.
	 * @param e
	 */
	public void savePlayerData(PlayerJoinEvent e)
	{
		//If using the database.
		if(usingDatabase)
		{
			//Save to the database
			database.savePlayerData(e);
		}
		else
		{
			//Save to the local flatfile.
			flatFile.savePlayerData(e);
		}
	}

	/**
	 * Returns whether a user is banned by the server or not.
	 * @param username
	 * @return
	 */
	public boolean isBanned(String username)
	{
		//If using the database.
		if(usingDatabase)
		{
			//Return the result of a MySQL query.
			return database.isBanned(username) || flatFile.isBanned(username);
		}
		else
		{
			//Check the local files for bans.
			return flatFile.isBanned(username);
		}
	}

	/**
	 * Sets whether we use a MySQL database or not.
	 * @param flag
	 */
	public void setUsingDatabase(boolean flag)
	{
		//Sets the usingDatabase boolean to the flag boolean.
		usingDatabase = flag;
	}
}
