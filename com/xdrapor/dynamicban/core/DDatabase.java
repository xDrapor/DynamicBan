package com.xdrapor.dynamicban.core;

import java.sql.*;

import org.bukkit.event.player.PlayerLoginEvent;

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
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + prefix + "_playerdata" + " (username VARCHAR(32) NOT NULL, ipaddress VARCHAR(32) NOT NULL, initialipaddress VARCHAR(32), PRIMARY KEY (username));");
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
	public void savePlayerData(PlayerLoginEvent event)
	{
		try 
		{
			//Insert the player's data into the playerdata table.
			statement.executeUpdate("INSERT INTO " + prefix + "_playerdata (username, ipaddress, initialipaddress) VALUES ('"+ event.getPlayer().getName() + "','" + event.getPlayer().getAddress().getAddress().getHostAddress() +"','"+ event.getPlayer().getAddress().getAddress().getHostAddress() +"') ON DUPLICATE KEY UPDATE ipaddress='" +  event.getPlayer().getAddress().getAddress().getHostAddress() + "';");
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
	
	public void closeConnections()
	{
		try
		{
			connection.close();
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
