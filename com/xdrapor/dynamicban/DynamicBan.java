package com.xdrapor.dynamicban;

import com.xdrapor.dynamicban.core.DListener;
import com.xdrapor.dynamicban.core.DPlugin;

/**
 * Main class handled to be called by Bukkit.
 * @author xDrapor
 */
public class DynamicBan extends DPlugin
{
	/** Holds a static instance of this class **/
	public static DynamicBan instance;
	
	/**
	 * Overrides onEnable method in DPlugin.
	 * @see com.xdrapor.dynamicban.core.DPlugin
	 */
	public void onEnable()
	{
		//Set the instance to the current instance.
		instance = this;
		//Sets the config to copy the defaults
		getConfig().options().copyDefaults(true);
		//Sets the config to copy the header.
		getConfig().options().copyHeader(true);
		//Saves the config.
		saveConfig();
		//Initialize the storage handler.
		getStorage().init();
		//Registers the DListener.
		registerEvent(new DListener());
		//Explicitly call the onEnable method from DPlugin
		super.onEnable();
	}
	
	/**
	 * Overrides onDisable method in DPlugin.
	 * @see com.xdrapor.dynamicban.core.DPlugin
	 */
	public void onDisable()
	{
		//Closes streams/connections.
		getStorage().close();
		//Explicitly call the onDisable method from DPlugin
		super.onDisable();
	}
}
