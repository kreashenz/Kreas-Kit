package me.kreashenz.kitpvp.utils;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Functions {

	private static String noPerm = "§cYou don't have permission.";
	
	public static void log(Level level, String msg){
		Bukkit.getLogger().log(level, msg);
	}

	public static void format(String msg){
		ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public static void tell(Player p, String msg){
		p.sendMessage(msg);
	}

	public static void noPerm(Player p){
		tell(p, noPerm);
	}
	
}
