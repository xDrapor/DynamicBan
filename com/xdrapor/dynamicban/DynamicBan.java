package com.xdrapor.dynamicban;

import com.xdrapor.dynamicban.core.DPlugin;

/**
 * Main class handled to be called by Bukkit.
 * @author xDrapor
 */
public class DynamicBan extends DPlugin
{
	/**
	 * Overrides onEnable method in DPlugin.
	 * @see com.xdrapor.dynamicban.core.DPlugin
	 */
	public void onEnable()
	{
		//Explicitly call the onEnable method from DPlugin
		super.onEnable();
	}
	
	/**
	 * Overrides onDisable method in DPlugin.
	 * @see com.xdrapor.dynamicban.core.DPlugin
	 */
	public void onDisable()
	{
		//Explicitly call the onDisable method from DPlugin
		super.onDisable();
	}
}
