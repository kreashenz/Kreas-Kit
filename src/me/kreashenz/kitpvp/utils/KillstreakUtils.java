package me.kreashenz.kitpvp.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import me.kreashenz.kitpvp.KitPvP;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class KillstreakUtils {

	private HashMap<String, Integer> streak = new HashMap<String, Integer>();
	public HashMap<String, Integer> kills = new HashMap<String, Integer>();
	public HashMap<String, Integer> deaths = new HashMap<String, Integer>();

	private KitPvP plugin;

	public KillstreakUtils(KitPvP plugin){
		this.plugin = plugin;
	}

	public void setStreaks(Player p, int streaks){
		if(!streak.containsKey(p.getName())){
			streak.put(p.getName(), streaks);
		} else {
			int a = streak.get(p.getName());
			streak.remove(p.getName());
			streak.put(p.getName(), a+1);
		}
	}

	public void clearStreaks(Player p){
		streak.put(p.getName(), 0);
	}

	public void setKills(Player p, int kill){
		if(!kills.containsKey(p.getName())){
			kills.put(p.getName(), kill);
		} else {
			int a = kills.get(p.getName());
			kills.remove(p.getName());
			kills.put(p.getName(), a+1);
		}
	}

	public void setDeaths(Player p, int death){
		if(!deaths.containsKey(p.getName())){
			deaths.put(p.getName(), death);
		} else {
			int a = deaths.get(p.getName());
			deaths.remove(p.getName());
			deaths.put(p.getName(), a+1);
		}
	}

	public int getStreaks(Player p){
		if(streak.containsKey(p.getName())){
			return streak.get(p.getName());
		} else {
			return 0;
		}
	}
	
	public void setConfKills(Player p){
		int a = getConfig().getInt(p.getName() + ".kills");
		getConfig().set(p.getName() + ".kills", a + getHashKills(p));
	}
	
	public void setConfDeaths(Player p){
		int a = getConfig().getInt(p.getName() + ".deaths");
		getConfig().set(p.getName() + ".deaths", a + getHashDeaths(p));
	}

	public int getConfDeaths(Player p){
		return getConfig().getInt(p.getName() + ".deaths");
	}

	public int getConfKills(Player p){
		return getConfig().getInt(p.getName() + ".kills");
	}

	public int getHashKills(Player p){
		return kills.get(p.getName());
	}

	public int getHashDeaths(Player p){
		return deaths.get(p.getName());
	}

	public void save(){
		try {
			for(Player p : Bukkit.getOnlinePlayers()){
				setConfDeaths(p);
				setConfKills(p);
				getConfig().save(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "stats.yml"));
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private FileConfiguration getConfig(){
		return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "stats.yml"));
	}

}
