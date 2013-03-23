package com.xdrapor.dynamicban.core;

import java.util.logging.Logger;

/**
 * A class to assist with logging related functions.
 * @author xDrapor
 */
public class DLogger 
{
	/** Logger instance of Minecraft **/
	private static final Logger log = Logger.getLogger("Minecraft");
	/** Default prefix of any logger messages **/
	private static final String prefix = "[DynamicBan] ";
	private static final String mysql = "[MySQL] ";
	
	/**
	 * Logs a message on the INFO level.
	 * Use for any general information, such as startup, responses, etc.
	 * @param msg
	 */
	public void info(String msg)
	{
		//Uses the previously defined Logger instance to log on the INFO level.
		log.info(prefix + msg);
	}
	
	/**
	 * Logs a message on the CONFIG level.
	 * Use for any configuration-related logging.
	 * @param msg
	 */
	public void config(String msg)
	{
		//Uses the previously defined Logger instance to log on the CONFIG level.
		log.config(prefix + msg);
	}
	
	/**
	 * Logs a message on the FINE level.
	 * Use for notifying users of errors that can be ignored, etc.
	 * @param msg
	 */
	public void fine(String msg)
	{
		//Uses the previously defined Logger instance to log on the FINE level.
		log.fine(prefix + msg);
	}
	
	/**
	 * Logs a message on the FINER level.
	 * Use for notifying users of updates, etc.
	 * @param msg
	 */
	public void finer(String msg)
	{
		//Uses the previously defined Logger instance to log on the FINER level.
		log.finer(prefix + msg);
	}
	
	/**
	 * Logs a message on the SEVERE level.
	 * Use for handling errors.
	 * @param msg
	 */
	public void severe(String msg)
	{
		//Uses the previously defined Logger instance to log on the SEVERE level.
		log.severe(prefix + msg);
	}
	
	/**
	 * Logs a message on the SEVERE level.
	 * Use for handling errors.
	 * @param msg
	 */
	public void mysql_severe(String msg)
	{
		//Uses the previously defined Logger instance to log on the SEVERE level.
		log.severe(prefix + mysql + msg);
	}
	
	/**
	 * Logs a message on the INFO level.
	 * Use for any general information, such as startup, responses, etc.
	 * @param msg
	 */
	public void mysql_info(String msg)
	{
		//Uses the previously defined Logger instance to log on the INFO level.
		log.info(prefix + mysql + msg);
	}
}
