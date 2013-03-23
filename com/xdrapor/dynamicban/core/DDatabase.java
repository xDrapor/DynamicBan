package com.xdrapor.dynamicban.core;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.event.player.PlayerJoinEvent;

/**
 * This class handles interactions with the MySQL database.
 * @author xDrapor
 */
public class DDatabase extends DCore
{
	/** Define Strings to hold the various details of the MySQL database **/
	private String address, database, username, password, prefix;
	
	/** Define a Statement object to execute queries **/
	private static Statement statement;
	
	/** Define a Connection object for the MySQL connection **/
	private static Connection connection;
	
	/** Define an Integer to hold the port of the connection **/
	private int port;

	/**
	 * Constructs a new DSQL instance.
	 * @param plugin
	 */
	public DDatabase()
	{
		//Sets the address to the configured address.
		address  = config.getString("mysql.address");
		//Sets the address to the configured address.
		port     = config.getInt("mysql.port");
		//Sets the address to the configured address.
		username = config.getString("mysql.username");
		//Sets the address to the configured address.
		password = config.getString("mysql.password");
		//Sets the address to the configured address.
		prefix 	 = config.getString("mysql.prefix");
		//Sets the address to the configured address.
		database = config.getString("mysql.dbname");
		//Sets up the connection with the MySQL database.
		getConnection();
		//Sets up tables if needed.
		setupTables();
	}

	/**
	 * Attempts to establish a connection with the MySQL database.
	 */
	private void getConnection()
	{
		try 
		{
			//Attempt to open a connection with the SQL database
			connection = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/" + database, username, password);
			//Log the successful connection.
			dynamicBan.getLog().mysql_info("Successfully connected to database " + database + " at " + address + "!");
		}
		catch (Exception e) 
		{
			//Print the error
			dynamicBan.getLog().mysql_severe("Connection could not be established with the SQL server.");
			//Print that we will be reverting to flatfile.
			dynamicBan.getLog().mysql_severe("Reverting to flatfile...");
			//Incase they need to report the issue.
			dynamicBan.getLog().mysql_severe("If reporting this as a bug, please provide this error: " + e.getMessage());
			//Switch to flatfile.
			storageHandler.setUsingDatabase(false);
		}
	}

	/**
	 * Sets up tables, if they do not exist.
	 */
	private void setupTables()
	{
		try 
		{
			//Dont execute this code if the connection is null.
			if(connection != null)
			{
				//Create a new statement.
				statement = connection.createStatement();
				//Note: if time is -1, then it is a permanent ban
				//Creates the violations table.
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + prefix + "_violations" + " (username VARCHAR(32) NOT NULL, bantime BIGINT NOT NULL, ipaddress VARCHAR(32) NOT NULL, banishedtime BIGINT NOT NULL, ipbantime BIGINT NOT NULL, rangetime BIGINT NOT NULL, rangelevel TINYINT(1) NOT NULL, warns INT NOT NULL, kicks INT NOT NULL, PRIMARY KEY (username));");
				//Creates the playerdata table
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + prefix + "_playerdata" + " (username VARCHAR(32) NOT NULL, ipaddress VARCHAR(32) NOT NULL, initialipaddress VARCHAR(32), lastjoin VARCHAR(32), lockedip TINYINT(1), immune TINYINT(1), PRIMARY KEY (username));");
			}
		} 
		catch (Exception e) 
		{
			//Print what error occurred.
			dynamicBan.getLog().mysql_severe("Error when creating tables in the database! Please check your configuration file, reverting to flatfile.");
			//Print that we will be reverting to flatfile.
			dynamicBan.getLog().mysql_severe("Reverting to flatfile...");
			//Incase they need to report the issue.
			dynamicBan.getLog().mysql_severe("If reporting this bug, please provide this error: " + e.getMessage());
			//Switch to flatfile.
			storageHandler.setUsingDatabase(false);
		}
	}

	/**
	 * Saves player data to the MySQL database.
	 * @param event
	 */
	public void savePlayerData(PlayerJoinEvent event)
	{
		try 
		{
			//Insert the player's data into the playerdata table.
			statement.executeUpdate("INSERT INTO " + prefix + "_playerdata (username, ipaddress, initialipaddress, lastjoin, lockedip, immune) VALUES ('"+ event.getPlayer().getName() + "','" + event.getPlayer().getAddress().getAddress().getHostAddress() +"','"+ event.getPlayer().getAddress().getAddress().getHostAddress() +"', '" + new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss").format(new Date())+ "', 0,0) ON DUPLICATE KEY UPDATE ipaddress='" +  event.getPlayer().getAddress().getAddress().getHostAddress() + "', lastjoin='" + new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss").format(new Date()) + "';");
			//Insert the player's data into the violations table.
			statement.executeUpdate("INSERT INTO " + prefix + "_violations (username, ipaddress, bantime, banishedtime, ipbantime, rangetime, rangelevel, warns, kicks) VALUES ('"+ event.getPlayer().getName() + "', '" + event.getPlayer().getAddress().getAddress().getHostAddress() + "', 0, 0, 0, 0, 0, 0, 0) ON DUPLICATE KEY UPDATE username='" +  event.getPlayer().getName() + "';");
		} 
		catch (Exception e) 
		{	
			//Print the error
			dynamicBan.getLog().mysql_severe("Error occurred when storing data for " + event.getPlayer().getName());
			//Print that we will be reverting to flatfile.
			dynamicBan.getLog().mysql_severe("Reverting to flatfile...");
			//Incase they need to report the issue.
			dynamicBan.getLog().mysql_severe("If reporting this as a bug, please provide this error: " + e.getMessage());
			//Switch to flatfile.
			storageHandler.setUsingDatabase(false);
		}
	}

	/**
	 * Checks to see if a user is banned.
	 * @param name
	 * @return String
	 */
	public boolean isBanned(String name)
	{
		//Define a new integer to hold the time of the ban.
		long timeHolder = 0;
		//Define a new ResultSet to hold the results of the MySQL query.
		ResultSet result;
		try
		{
			//Execute the query
			statement.executeQuery("SELECT bantime FROM " + prefix + "_violations WHERE username='" + name + "';");
			//Get the result
			result = statement.getResultSet();
			//Move the "cursor" to the first row of the ResultSet index.
			result.first();
			//Set the timeHolder to the long value of the result.
			timeHolder = result.getLong("bantime");
		}
		catch (Exception e)
		{
			//Print what error occurred.
			dynamicBan.getLog().mysql_severe("Query failed.");
			//Print that we will be reverting to flatfile.
			dynamicBan.getLog().mysql_severe("Reverting to flatfile...");
			//Incase they need to report the issue.
			dynamicBan.getLog().mysql_severe("If reporting this as a bug, please provide this error: " + e.getMessage());
			//Switch to flatfile.
			storageHandler.setUsingDatabase(false);
		}
		//Return the boolean result, if the time is -1, the user is permanently banned.
		return timeHolder == -1 ? true : false;
	}
	
	/** 
	 * Checks to see if a user is temporarily banned.
	 * @param name
	 * @return String
	 */
	public boolean isTempbanned(String name)
	{
		//Define a new integer to hold the time of the ban.
		long timeHolder = 0;
		//Define a new ResultSet to hold the results of the MySQL query.
		ResultSet result;
		try
		{
			//Execute the query
			statement.executeQuery("SELECT bantime FROM " + prefix + "_violations WHERE username='" + name + "';");
			//Get the result
			result = statement.getResultSet();
			//Move the "cursor" to the first row of the ResultSet index.
			result.first();
			//Set the timeHolder to the long value of the result.
			timeHolder = result.getLong("bantime");
		}
		catch (Exception e)
		{
			//Print what error occurred.
			dynamicBan.getLog().mysql_severe("Query failed.");
			//Print that we will be reverting to flatfile.
			dynamicBan.getLog().mysql_severe("Reverting to flatfile...");
			//Incase they need to report the issue.
			dynamicBan.getLog().mysql_severe("If reporting this as a bug, please provide this error: " + e.getMessage());
			//Switch to flatfile.
			storageHandler.setUsingDatabase(false);
		}
		//Return the boolean result, if the time is -1, the user is permanently banned.
		return timeHolder > 0 ? true : false;
	}
	
	/** 
	 * Checks to see if a user is ipbanned.
	 * @param name
	 * @return String
	 */
	public boolean isIPbanned(String address)
	{
		//Define a new integer to hold the time of the ban.
		long timeHolder = 0;
		//Define a new ResultSet to hold the results of the MySQL query.
		ResultSet result;
		//Define a new String to hold the name of the matched address
		String name = "";
		try
		{
			//Execute the query
			statement.executeQuery("SELECT username FROM " + prefix + "_violations WHERE ipaddress='" + address + "';");
			//Set the name to the result
			result = statement.getResultSet();
			//Move the "cursor" to the first row of the ResultSet index.
			result.first();
			//Only if a match in address were found
			if(result.getFetchSize() > 0)
			{
				//Set the name string to the result
				name = result.getString("username");
				//Execute the query
				statement.executeQuery("SELECT ipbantime FROM " + prefix + "_violations WHERE username='" + name + "';");
				//Get the result
				result = statement.getResultSet();
				//Move the "cursor" to the first row of the ResultSet index.
				result.first();
				//Set the timeHolder to the long value of the result.
				timeHolder = result.getLong("ipbantime");
			}
		}
		catch (Exception e)
		{
			//Print what error occurred.
			dynamicBan.getLog().mysql_severe("Query failed.");
			//Print that we will be reverting to flatfile.
			dynamicBan.getLog().mysql_severe("Reverting to flatfile...");
			//Incase they need to report the issue.
			dynamicBan.getLog().mysql_severe("If reporting this as a bug, please provide this error: " + e.getMessage());
			//Switch to flatfile.
			storageHandler.setUsingDatabase(false);
		}
		//Return the boolean result, if the time is -1, the user is permanently banned.
		return timeHolder == -1 ? true : false;
	}
	
	/** 
	 * Checks to see if a user is temporarily ipbanned.
	 * @param name
	 * @return String
	 */
	public boolean isTempIPbanned(String address)
	{
		//Define a new integer to hold the time of the ban.
		long timeHolder = 0;
		//Define a new ResultSet to hold the results of the MySQL query.
		ResultSet result;
		//Define a new String to hold the name of the matched address
		String name = "";
		try
		{
			//Execute the query
			statement.executeQuery("SELECT username FROM " + prefix + "_violations WHERE ipaddress='" + address + "';");
			//Set the name to the result
			result = statement.getResultSet();
			//Move the "cursor" to the first row of the ResultSet index.
			result.first();
			//Only if a match in address were found
			if(result.getFetchSize() > 0)
			{
				//Set the name string to the result
				name = result.getString("username");
				//Execute the query
				statement.executeQuery("SELECT ipbantime FROM " + prefix + "_violations WHERE username='" + name + "';");
				//Get the result
				result = statement.getResultSet();
				//Move the "cursor" to the first row of the ResultSet index.
				result.first();
				//Set the timeHolder to the long value of the result.
				timeHolder = result.getLong("ipbantime");
			}
		}
		catch (Exception e)
		{
			//Print what error occurred.
			dynamicBan.getLog().mysql_severe("Query failed.");
			//Print that we will be reverting to flatfile.
			dynamicBan.getLog().mysql_severe("Reverting to flatfile...");
			//Incase they need to report the issue.
			dynamicBan.getLog().mysql_severe("If reporting this as a bug, please provide this error: " + e.getMessage());
			//Switch to flatfile.
			storageHandler.setUsingDatabase(false);
		}
		//Return the boolean result, if the time is -1, the user is permanently banned.
		return timeHolder > 0 ? true : false;
	}
	
	/**
	 * Close all connections created.
	 */
	public void closeConnections()
	{
		try
		{
			//Close the connection.
			connection.close();
			//Close the statement.
			statement.close();
		} 
		catch (Exception e)
		{
			//Print what error occurred.
			dynamicBan.getLog().mysql_severe("Error when closing connections!");
			//Incase they need to report the issue.
			dynamicBan.getLog().mysql_severe("If reporting this as a bug, please provide this error: " + e.getMessage());
		}
	}
}
