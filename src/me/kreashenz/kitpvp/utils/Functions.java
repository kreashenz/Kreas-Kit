package me.kreashenz.kitpvp.utils;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Functions {

	private static String noPerm = "§cYou don't have permission.";

	public static void log(Level level, String msg){
		Bukkit.getLogger().log(level, "[Kreas-Kits] " + msg);
	}

	public static String format(String msg){
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	public static void tell(Player p, String msg){
		p.sendMessage(msg);
	}

	public static void noPerm(Player p){
		tell(p, noPerm);
	}

	public static ItemStack name(ItemStack item, String name){
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName(name);
		item.setItemMeta(itemmeta);
		return item;
	}

	public static void givePot(Player p, PotionEffectType pot, int time, int level){
		p.addPotionEffect(new PotionEffect(pot, time*20, level));
	}
	
}
