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
			streak.put(p.getName(), a);
		}
	}

	public void clearStreaks(Player p){
		streak.put(p.getName(), 0);
	}

	public void setKills(Player p, int kills){
		getConfig().set(p.getName() + ".kills", kills);
		save();
	}

	public void setDeaths(Player p, int deaths){
		getConfig().set(p.getName() + ".deaths", deaths);
		save();
	}

	public int getStreaks(Player p){
		if(streak.containsKey(p.getName())){
			return streak.get(p.getName());
		} else {
			return 0;
		}
	}

	public int getDeaths(Player p){
		return getConfig().getInt(p.getName() + ".deaths");
	}

	public int getKills(Player p){
		return getConfig().getInt(p.getName() + ".kills");
	}

	public void save(){
		try {
			YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "stats.yml"))
			.save(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "stats.yml"));
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private FileConfiguration getConfig(){
		return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "stats.yml"));
	}

}
