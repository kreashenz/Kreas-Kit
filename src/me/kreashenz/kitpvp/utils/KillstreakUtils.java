package me.kreashenz.kitpvp.utils;

import me.kreashenz.kitpvp.KitPvP;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class KillstreakUtils {

	private static KitPvP plugin;

	public static void setStreaks(Player p, int Streaks){
		getConfig().set("killstreaks." + p.getName(), Streaks);
		save();
	}

	public static void clearStreaks(Player p){
		getConfig().set("killstreaks." + p.getName(), null);
		save();
	}

	public static void setKills(Player p, int kills){
		getConfig().set(p.getName() + ".kills", kills);
		save();
	}

	public static void setDeaths(Player p, int deaths){
		getConfig().set(p.getName() + ".deaths", deaths);
		save();
	}

	public static int getStreaks(Player p){
		return getConfig().getInt("killstreaks." + p.getName());
	}

	public static int getDeaths(Player p){
		return getConfig().getInt(p.getName() + ".deaths");
	}

	public static int getKills(Player p){
		return getConfig().getInt(p.getName() + ".kills");
	}

	private static void save(){
		plugin = new KitPvP();
		plugin.saveConfig();
	}

	private static FileConfiguration getConfig(){
		plugin = new KitPvP();
		return plugin.getConfig();
	}

}
