package com.xdrapor.dynamicban.core;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Class to assist with local storage.
 * @author xDrapor
 */
public class DFlatFile extends DCore
{
	//TODO: Comment class.
	private File violationsFile;
	private File playerDataFile;
	private FileConfiguration violations;
	private FileConfiguration playerdata;

	public DFlatFile() 
	{
		violationsFile = new File(dynamicBan.getDataFolder(), "violations.yml");
		playerDataFile = new File(dynamicBan.getDataFolder(), "playerdata.yml");
		playerdata = YamlConfiguration.loadConfiguration(playerDataFile);
		violations = YamlConfiguration.loadConfiguration(violationsFile);
		saveFlatFile();
	}

	public void savePlayerData(PlayerJoinEvent e)
	{
		playerdata.set("players." + e.getPlayer().getName().toLowerCase() + ".address", e.getPlayer().getAddress().getAddress().getHostAddress().replace(".", "/"));
		if(!playerdata.contains("players." + e.getPlayer().getName().toLowerCase() + ".initialaddress"))
		{
			playerdata.set("players." + e.getPlayer().getName().toLowerCase() + ".initialaddress", e.getPlayer().getAddress().getAddress().getHostAddress().replace(".", "/"));
			playerdata.set("players." + e.getPlayer().getName().toLowerCase() + ".immune", false);
			playerdata.set("lockedaddresses." + e.getPlayer().getAddress().getAddress().getHostAddress().replace(".", "/"), false);
		}
		playerdata.set("players." + e.getPlayer().getName().toLowerCase() + ".lastjoin", new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss").format(new Date()));
		saveFlatFile();
		reloadFlatFile();
	}

	public boolean isBanned(String username)
	{
		return false;
	}

	public void saveFlatFile()
	{
		if (playerDataFile == null || playerdata == null || violationsFile == null || violations == null)
		{
			return;
		}
		try 
		{
			violations.save(violationsFile);
			playerdata.save(playerDataFile);
		}
		catch (IOException ex)
		{
			dynamicBan.getLog().severe("Could not save flatfile!");
		}
	}

	public void reloadFlatFile()
	{
		playerdata = YamlConfiguration.loadConfiguration(playerDataFile);
		violations = YamlConfiguration.loadConfiguration(violationsFile);
	}
}
