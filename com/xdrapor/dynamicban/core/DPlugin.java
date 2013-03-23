package com.xdrapor.dynamicban.core;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Class to simplify the usage of JavaPlugin.
 * @author xDrapor
 */
public class DPlugin extends JavaPlugin
{
	/** The main logger instance **/
	private static DLogger log;
	
	/** The DStorage instance **/
	private static DStorage storageHandler;
	
	/**
	 * Constructs a new DPlugin instance.
	 * Cannot be instantiated.
	 */
	protected DPlugin()
	{
		//Instantiate the DLogger object.
		log 		   = new DLogger();
		//Instantiate the DStorage object.
		storageHandler = new DStorage();
	}
	
	/**
	 * Overrides onEnable method in JavaPlugin.
	 * @see org.bukkit.plugin.java.JavaPlugin
	 */
	public void onEnable()
	{
		//Tells the user that the plugin is starting up.
		log.info("Started up.");
	}
	
	/**
	 * Overrides onDisable method in JavaPlugin.
	 * @see org.bukkit.plugin.java.JavaPlugin
	 */
	public void onDisable()
	{
		//Tells the user that the plugin is shutting down.
		log.info("Shut down.");
	}
	
	/**
	 * Registers an event to this plugin.
	 * @param l
	 */
	public void registerEvent(Listener l)
	{
		//Register the listener to this plugin.
		getServer().getPluginManager().registerEvents(l, this);
	}
	
	/**
	 * Returns the DLogger
	 * @return DLogger
	 */
	public DLogger getLog()
	{
		//Return the DLogger instance.
		return log;
	}
	
	/**
	 * Returns the DStorage
	 * @return DLogger
	 */
	public DStorage getStorage()
	{
		//Returns the DStorage instance.
		return storageHandler;
	}
}
