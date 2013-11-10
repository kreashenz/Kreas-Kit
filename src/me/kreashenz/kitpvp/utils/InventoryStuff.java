package me.kreashenz.kitpvp.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import me.kreashenz.kitpvp.KitPvP;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InventoryStuff {

	private static KitPvP plugin;

	private static File folder;

	private static List<String> all = new ArrayList<String>();

	static {
		plugin = KitPvP.getInstance();
		folder = new File(plugin.getDataFolder() + File.separator + "kits");

		if(!folder.exists())folder.mkdir();
	}

	public static void loadKits(){
		for(String str : getAllKits()){
			all.add(str);
		}
	}

	public static void addKit(String kit){
		all.add(kit);
		loadKits();
	}

	public static void deleteKit(String kit){
		all.remove(kit);
		for(File f : folder.listFiles()){
			String name = f.getName().replace(".yml", "");
			if(name.equalsIgnoreCase(kit)){
				f.delete();
				loadKits();
			}
		}
	}

	public static List<String> getAllKits(){
		List<String> kits = new ArrayList<String>();
		for(File file : folder.listFiles()){
			if(!(file.getName().startsWith("."))){
				String name = file.getName().replace(".yml", "");
				kits.add(name);
			}
		}
		for(String str : plugin.getConfig().getConfigurationSection("Kits").getKeys(false)){
			kits.add(str);
		}
		return kits;
	}

	public static boolean kitExists(String kit){
		return (getAllKits().contains(kit));
	}

	public static void setStuff(Player p, String file){
		p.getInventory().setArmorContents(getArmorContents(file));
		p.getInventory().setContents(getInventory(file));
		p.addPotionEffects(getPotionEffects(file));
	}

	private static Collection<PotionEffect> getPotionEffects(String file){
		if(file == null) return null;
		Collection<PotionEffect> pots = new HashSet<PotionEffect>();

		PotionEffect pot = null;

		FileConfiguration conf = YamlConfiguration.loadConfiguration(new File(folder, file + ".yml"));

		for(String str : conf.getConfigurationSection("PotionEffects").getKeys(false)){
			int level = conf.getInt("PotionEffects." + str + ".level");
			int duration = conf.getInt("PotionEffects." + str + ".duration");
			pot = new PotionEffect(PotionEffectType.getByName(str), duration, level);
			pots.add(pot);
		}

		return pots;
	}

	private static ItemStack[] getArmorContents(String file){
		if(file == null) return null;
		ItemStack[] items = null;

		FileConfiguration conf = YamlConfiguration.loadConfiguration(new File(folder, file + ".yml"));

		int size = conf.getInt("ArmorSlot", 4);

		items = new ItemStack[size];

		for(int i = 0; i < size; i++){
			if(conf.contains("ArmorSlot." + i)) items[i] = conf.getItemStack("ArmorSlot." + i);
			else items[i] = new ItemStack(Material.AIR);
		}

		return items;
	}

	private static ItemStack[] getInventory(String file){
		if(file == null) return null;
		ItemStack[] items = null;

		FileConfiguration conf = YamlConfiguration.loadConfiguration(new File(folder, file + ".yml"));

		int size = conf.getInt("Slot", 27);

		items = new ItemStack[size];

		for(int i = 0; i < size; i++){
			if(conf.contains("Slot." + i)) items[i] = conf.getItemStack("Slot." + i);
			else items[i] = new ItemStack(Material.AIR);
		}

		return items;
	}

	public static boolean saveInventory(PlayerInventory inv, String fileName){
		if(inv == null || fileName == "" || fileName.contains("[a-zA-Z_0-9]")) return false;
		File file = new File(folder, fileName + ".yml");
		if(file.exists()) file.delete();
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);

		ItemStack[] contents = inv.getContents();
		ItemStack[] armor = inv.getArmorContents();

		for(int i = 0; i < contents.length; i++){
			ItemStack item = contents[i];
			if(item != null) if(item.getType() != Material.AIR) conf.set("Slot." + i, item);
		}

		for(int i = 0; i < armor.length; i++){
			ItemStack item = armor[i];
			if(item != null) if(item.getType() != Material.AIR) conf.set("ArmorSlot." + i, item);
		}

		for(PotionEffect pot : inv.getHolder().getActivePotionEffects()){
			conf.set("PotionEffects." + pot.getType().getName() + ".level", pot.getAmplifier());
			conf.set("PotionEffects." + pot.getType().getName() + ".duration", pot.getDuration());
		}

		try {
			conf.save(file);
		}
		catch(IOException e){
			return false;
		}

		return true;
	}

}