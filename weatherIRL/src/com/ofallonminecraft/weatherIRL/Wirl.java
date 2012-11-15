package com.ofallonminecraft.weatherIRL;


import java.io.File;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;


public class Wirl extends JavaPlugin
{
	boolean syncing = false; // enable syncing on reload and startup?
							 // base this on previous condition of this variable?



	// ---------- INITIALIZE HASMAPS TO STORE LOCATION AND ATTRIBUTES ---------- //
	public static String woeid = "";
	public static String[] attributes;
	// ---------- END INITIALIZE HASMAPS TO STORE LOCATION AND ATTRIBUTES ---------- //






	// ---------- MANAGE FILES WHEN ENABLING/DISABLING THE PLUGIN ---------- //
	public void onEnable() // on enable, load files
	{
		try
		{
			if (new File("plugins/weatherIRL/").exists()) {
				if (!(new File("plugins/weatherIRL/woeid.bin").exists())) {
					new File("plugins/weatherIRL/woeid.bin").createNewFile();
					SLAPI.save(woeid, "plugins/weatherIRL/woeid.bin");
				}
				if (!(new File("plugins/weatherIRL/attributes.bin").exists())) {
					new File("plugins/weatherIRL/attributes.bin").createNewFile();
					SLAPI.save(attributes, "plugins/weatherIRL/attributes.bin");
				}
			} else {
				new File("plugins/weatherIRL").mkdir();
				new File("plugins/weatherIRL/woeid.bin").createNewFile();
				new File("plugins/weatherIRL/attributes.bin").createNewFile();
				SLAPI.save(woeid, "plugins/weatherIRL/woeid.bin");
				SLAPI.save(attributes, "plugins/weatherIRL/attributes.bin");
			}
			getLogger().info("weatherIRL has been enabled");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onDisable() // on disable, save the files
	{
		getLogger().info("weatherIRL has been disabled.");
	}
	// ---------- END MANAGE FILES WHEN ENABLING/DISABLING THE PLUGIN ---------- //






	// ---------- HANDLE THE COMMANDS ---------- //
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{

		// Forecast
		if (cmd.getName().equalsIgnoreCase("forecast")) {
			try {
				woeid = SLAPI.load("plugins/weatherIRL/woeid.bin");
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			if (woeid.length()==0) {
				sender.sendMessage("You must set a location first! Use /syncweather [location]");
			} else {
				sender.sendMessage(RSSReader.rssReader("Forecast:", woeid));
			}
			return true;
		}
		
		// SyncWeather
		if (cmd.getName().equalsIgnoreCase("syncweather")) {
			if (args.length==0) {
				sender.sendMessage("You must choose a location! Use /syncweather [location]");
				return false;
			}
			String location = "";
			for (int i=0; i<args.length; ++i) {
				// append all arguments into one string (the location)
				location+=args[i]+" ";
			}
			try {
				woeid = FindWOEID.findWOEID(location);
			} catch (IOException e) {
				e.printStackTrace();
			}
			sender.sendMessage("Starting weather syncing for "+location+".");
			syncing = true;
			// START UP REPEATING SYNCING TASK
			return true;
		}

		// StopWeatherSync
		if (cmd.getName().equalsIgnoreCase("stopweathersync")) {
			if (!syncing) {
				sender.sendMessage("No weather syncing is currently taking place!");
				return false;
			}
			sender.sendMessage("Stopping weather syncing.");
			woeid = "";        // find a better way to do this, make it so syncweather
			syncing = false;   // doesn't require a location if one has already been stored?
			// STOP REPEATING SYNCING TASK
			return true;
		}
		
		// SyncAttributes
		if (cmd.getName().equalsIgnoreCase("syncattributes")) {
			if (args.length==0) {
				sender.sendMessage("Must choose attributes!");
				return false;
			}
			for (int i=0; i<args.length; ++i) {   // append all chosen attributes (if they are valid)
				// PUT CODE IN HERE
				return true;
			}
			return true;
		}
		
		return false;
	}
	// --------- END HANDLE THE COMMANDS ---------- //

}



