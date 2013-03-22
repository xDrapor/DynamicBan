package com.xdrapor.dynamicban.core;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Class to simplify the usage of JavaPlugin.
 * @author xDrapor
 */
public class DPlugin extends JavaPlugin
{
	/** The main logger instance **/
	protected final DLogger log;
	
	/**
	 * Constructs a new DPlugin instance.
	 * Cannot be instantiated.
	 */
	protected DPlugin()
	{
		log = new DLogger();
	}
	
	/**
	 * Overrides onEnable method in JavaPlugin.
	 * @see org.bukkit.plugin.java.JavaPlugin
	 */
	public void onEnable()
	{
		//Tells the user that the plugin is starting up.
		log.info("Starting up...");
	}
	
	/**
	 * Overrides onDisable method in JavaPlugin.
	 * @see org.bukkit.plugin.java.JavaPlugin
	 */
	public void onDisable()
	{
		//Tells the user that the plugin is shutting down.
		log.info("Shutting down...");
	}
}
