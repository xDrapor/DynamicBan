package com.xdrapor.dynamicban.core;

import org.bukkit.configuration.file.FileConfiguration;

import com.xdrapor.dynamicban.DynamicBan;

/**
 * Class to prevent having to create multiple objects through instantiation.
 * @author xDrapor
 */
public class DCore 
{
	/** The global DynamicBan instance **/
	protected static final DPlugin dynamicBan 		= DynamicBan.instance;
	
	/** The global FileConfiguration instance **/
	protected static final FileConfiguration config = dynamicBan.getConfig();
	
	/** The global DStorage instance **/
	protected static final DStorage storageHandler  = new DStorage();
}
