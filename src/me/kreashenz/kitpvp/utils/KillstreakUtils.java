package me.kreashenz.kitpvp.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import me.kreashenz.kitpvp.KitPvP;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class KillstreakUtils {

	private HashMap<String, Integer> streak = new HashMap<String, Integer>();

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

	public int getStreaks(Player p){
		return (streak.containsKey(p.getName()) ? streak.get(p.getName()) : 0);
	}

	public void setKills(Player p, int kills){
		getConfig().set(p.getName() + ".kills", kills);
		save();
	}

	public void setDeaths(Player p, int deaths){
		getConfig().set(p.getName() + ".deaths", deaths);
		save();
	}

	public int getDeaths(Player p){
		return getConfig().getInt(p.getName() + ".deaths");
	}

	public int getKills(Player p){
		return getConfig().getInt(p.getName() + ".kills");
	}
	
	public double getKDR(Player p){
		int kills = getKills(p);
		int deaths = getDeaths(p);
		int kdr = something(kills, deaths);
		if(kdr != 0){
			kills = kills/kdr;
			deaths = deaths/kdr;
		}
		double ratio = Math.round(((double)kills/(double)deaths) * 100D) / 100D;
		if(kills == 0){
			ratio = 0.0;
		} else if(deaths == 0){
			ratio = kills;
		}
		return ratio;
	}

	public void save(){
		try {
			getConfig().save(new File(KitPvP.getInstance().getDataFolder(), "stats.yml"));
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private FileConfiguration getConfig(){
		return YamlConfiguration.loadConfiguration(new File(KitPvP.getInstance().getDataFolder(), "stats.yml"));
	}
	
	private int something(int a, int b){
		if (b==0) return a;
		return something(b,a % b);
	}

}
