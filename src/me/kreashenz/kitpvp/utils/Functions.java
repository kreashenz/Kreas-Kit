package me.kreashenz.kitpvp.utils;

import java.util.logging.Level;

import me.kreashenz.kitpvp.KitPvP;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Functions {

	public static void log(Level level, String msg){
		KitPvP.getInstance().getLogger().log(level,  msg);
	}

	public static String format(String msg){
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	public static void tell(Player p, String msg){
		p.sendMessage(format(msg));
	}

	public static void noPerm(Player p){
		tell(p, KitPvP.getInstance().getConfig().getString("messages.no-permission"));
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