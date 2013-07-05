package me.kreashenz.kitpvp;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Kits implements CommandExecutor {

	private KitManager kit;
	private KitPvP plugin;
	
	public Kits(KitPvP plugin){
		this.plugin = plugin;
		this.kit = new KitManager(plugin);
	}
	private List<String> assassin = new ArrayList<String>();

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args){
		if(s instanceof Player){
			Player p = (Player)s;
			if (cmd.getName().equalsIgnoreCase("admin")) {
				if (s.hasPermission("kitpvp.admin")) {
					if (args.length == 0){
						s.sendMessage("§cYou must type in a message.");
					} else {
						for (Player t : Bukkit.getOnlinePlayers()){
							if (t.hasPermission("kitpvp.admin")) {
								String chatMsg = "";
								for (int i = 0; i < args.length; i++) {
									chatMsg = chatMsg + args[i] + ' ';
								}
								String player = p.getDisplayName();
								t.sendMessage("§b[§9Admin§b] §c" + player + "§3 " + chatMsg);
							}
						}
					}
				} else s.sendMessage("§cYou do not have permission to use this command.");
			}
			if(cmd.getName().equalsIgnoreCase("archer")){
				if(s.hasPermission("kitpvp.archer")){
					if(!kit.hasAKit(p)){
						kit.addToKitTracker(p);
					} else {
						s.sendMessage("§cYou must die before you use another kit.");
						return false;
					}
					ArcherKit(p);
				} else s.sendMessage("§cYou do not have permission to use this command.");
			}
			if(cmd.getName().equalsIgnoreCase("pvp")){
				if(s.hasPermission("kitpvp.pvp")){
					if(!kit.hasAKit(p)){
						kit.addToKitTracker(p);
					} else {
						s.sendMessage("§cYou must die before you use another kit.");
						return false;
					}
					PvPKit(p);
				} else s.sendMessage("§cYou do not have permission to use this command.");
			}
			if(cmd.getName().equalsIgnoreCase("tank")){
				if(s.hasPermission("kitpvp.tank")){
					if(!kit.hasAKit(p)){
						kit.addToKitTracker(p);
					} else {
						s.sendMessage("§cYou must die before you use another kit.");
						return false;
					}
					TankKit(p);
				} else s.sendMessage("§cYou do not have permission to use this command.");
			}
			if(cmd.getName().equalsIgnoreCase("pyro")){
				if(s.hasPermission("kitpvp.pyro")){
					if(!kit.hasAKit(p)){
						kit.addToKitTracker(p);
					} else {
						p.sendMessage("§cYou must die before you use another kit.");
						return false;
					}
					PyroKit(p);
				} else p.sendMessage("§cYou do not have permission to use this command.");
			}
			if(cmd.getName().equalsIgnoreCase("refill")){
				if(s.hasPermission("kitpvp.refill")){
					ItemStack[] inv = p.getInventory().getContents();
					boolean gotBowl = false;
					for (ItemStack slot : inv) {
						if (slot != null && slot.getType() == Material.BOWL) {
							gotBowl = true;
							slot.setType(Material.MUSHROOM_SOUP);
						}
					}
					if (!gotBowl) {
						p.sendMessage("§cYou have no empty bowls!");
						return true; 
					}
					return true;
				} else s.sendMessage("§cYou do not have permission to use this command.");
			}
			if(cmd.getName().equalsIgnoreCase("medic")){
				if(s.hasPermission("kitpvp.medic")){
					if(!kit.hasAKit(p)){
						kit.addToKitTracker(p);
					} else {
						p.sendMessage("§cYou must die before you use another kit.");
						return false;
					}
					MedicKit(p);
				} else s.sendMessage("§cYou do not have permission to use this command.");
			}
			if(cmd.getName().equalsIgnoreCase("cupid")){
				if(s.hasPermission("kitpvp.cupid")){
					if(!kit.hasAKit(p)){
						kit.addToKitTracker(p);
					} else {
						s.sendMessage("§cYou must die before you use another kit.");
						return false;
					}
					if(!kit.hasCupidKit(p)){
						kit.addToCupidKit(p);
					}
					CupidKit(p);
				} else s.sendMessage("§cYou do not have permission to use this command.");
			}
			if(cmd.getName().equalsIgnoreCase("assassin")){
				assassin.add(p.getName());
				if(s.hasPermission("kitpvp.assassin")){
					if(!kit.hasAKit(p)){
						kit.addToKitTracker(p);
					} else {
						s.sendMessage("§çYou must die before you use another kit");
						return false;
					}
					AssassinKit(p);
				}
			}
			if(cmd.getName().equalsIgnoreCase("kkit")){
				if(args.length > 0){
					if(s.hasPermission("kitpvp."+args[0].toLowerCase())){
						if(!kit.hasAKit(p)){
							kit.addToKitTracker(p);
						} else {
							p.sendMessage("§cYou must die before you use another kit.");
						}
						plugin.kits.giveKit(p, args[0].toLowerCase());
					} else p.sendMessage("§cYou do not have permission to use this command.");
				} else p.sendMessage("§cUsage : §f/kkit <kitName>");
			}
			if(cmd.getName().equalsIgnoreCase("stats")){
				DecimalFormat d = new DecimalFormat("##.##");
				if(s.hasPermission("kitpvp.stats")){
					if(args.length == 0){
						FileConfiguration player = plugin.getConfig();
						p.sendMessage("§7+§c--------------------------------------§c+");
						p.sendMessage("§7|| §6KDR §1: §a" + d.format(player.getDouble("Kills") / player.getDouble("Deaths")));
						p.sendMessage("§7|| §6Kills §1: §a" + player.getInt("Kills"));
						p.sendMessage("§7|| §6Deaths §1: §a" + player.getInt("Deaths"));
						p.sendMessage("§7+§c--------------------------------------§c+");
					} else {
						Player t = Bukkit.getPlayer(args[0]);
						if(t.isOnline() && t != null){
							FileConfiguration target = YamlConfiguration.loadConfiguration(new File("plugins/Kreas-Kit/users/" + t.getName().toLowerCase() + ".yml"));
							p.sendMessage("§7+§c--------------------------------------§c+");
							p.sendMessage("§7|| §6KDR §1: §a" + d.format(target.getDouble("Kills") / target.getDouble("Deaths")));
							p.sendMessage("§7|| §6Kills §1: §a" + target.getInt("Kills"));
							p.sendMessage("§7|| §6Deaths §1: §a" + target.getInt("Deaths"));
							p.sendMessage("§7+§c--------------------------------------§c+");
						} else p.sendMessage("§cThat player cannot be found, please try again.");
					}
				} else p.sendMessage("§cYou do not have permission to use this command.");
			}
		} else s.sendMessage("§cYou must be a player to use these commands.");

		return true;
	}

	public void PyroKit(Player p){
		PlayerInventory pi = p.getInventory();
		pi.clear();
		pi.setArmorContents(null);
		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());
		p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1, 999999999));
		ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
		ItemStack tnt = new ItemStack(Material.TNT, 16);
		ItemMeta mtnt = (ItemMeta)tnt.getItemMeta();
		mtnt.setDisplayName("§rBombs");
		tnt.setItemMeta(mtnt);
		ItemStack ih = new ItemStack(Material.IRON_HELMET, 1);
		ItemStack ic = new ItemStack(Material.IRON_CHESTPLATE, 1);
		ItemStack il = new ItemStack(Material.IRON_LEGGINGS, 1);
		ItemStack ib = new ItemStack(Material.IRON_BOOTS, 1);
		ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP, 1);
		pi.setHelmet(ih);
		pi.setChestplate(ic);
		pi.setLeggings(il);
		pi.setBoots(ib);
		pi.addItem(sword, tnt);
		for(int i=1; i<=32; i++)pi.addItem(soup);
	}

	public void PvPKit(Player p){
		PlayerInventory pi = p.getInventory();
		pi.clear();
		pi.setArmorContents(null);
		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());
		p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1, 18000));
		ItemStack e = new ItemStack(Material.DIAMOND_SWORD, 1);
		e.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
		ItemStack ih = new ItemStack(Material.IRON_HELMET, 1);
		ItemStack ic = new ItemStack(Material.IRON_CHESTPLATE, 1);
		ItemStack il = new ItemStack(Material.IRON_LEGGINGS, 1);
		ItemStack ib = new ItemStack(Material.IRON_BOOTS, 1);
		ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP, 1);
		pi.setHelmet(ih);
		pi.setChestplate(ic);
		pi.setLeggings(il);
		pi.setBoots(ib);
		pi.addItem(e);
		for(int i=1; i<=34; i++)pi.addItem(soup);
	}

	public void TankKit(Player p){
		PlayerInventory pi = p.getInventory();
		pi.clear();
		pi.setArmorContents(null);
		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2, 18000));
		p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1, 18000));
		ItemStack dh = new ItemStack(Material.DIAMOND_HELMET, 1);
		ItemStack dc = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		ItemStack dl = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		ItemStack db = new ItemStack(Material.DIAMOND_BOOTS, 1);
		ItemStack e = new ItemStack(Material.IRON_SWORD, 1);
		ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP, 1);
		pi.setHelmet(dh);
		pi.setChestplate(dc);
		pi.setLeggings(dl);
		pi.setBoots(db);
		pi.addItem(e);
		for(int i=1; i<=34; i++)pi.addItem(soup);
	}

	public void MedicKit(Player p) {
		PlayerInventory pi = p.getInventory();
		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());
		pi.clear();
		pi.setArmorContents(null);
		ItemStack a = new ItemStack(Material.IRON_SWORD);
		ItemMeta aa = a.getItemMeta();
		ItemStack lh = new ItemStack(Material.LEATHER_HELMET, 1);
		ItemStack lc = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		ItemStack ll = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		ItemStack lb = new ItemStack(Material.LEATHER_BOOTS, 1);
		ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP, 1);
		lh.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		lc.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ll.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		lb.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		a.addEnchantment(Enchantment.DAMAGE_ALL, 0);
		a.addEnchantment(Enchantment.KNOCKBACK, 1);
		aa.setDisplayName("§rScalpel");
		a.setItemMeta(aa);
		LeatherArmorMeta lha = (LeatherArmorMeta)lh.getItemMeta();
		LeatherArmorMeta lca = (LeatherArmorMeta)lc.getItemMeta();
		LeatherArmorMeta lla = (LeatherArmorMeta)ll.getItemMeta();
		LeatherArmorMeta lba = (LeatherArmorMeta)lb.getItemMeta();
		lha.setColor(Color.BLACK);
		lca.setColor(Color.BLACK);
		lla.setColor(Color.BLACK);
		lba.setColor(Color.BLACK);
		lh.setItemMeta(lha);
		lc.setItemMeta(lca);
		ll.setItemMeta(lla);
		lb.setItemMeta(lba);
		pi.addItem(a);
		pi.setHelmet(lh);
		pi.setChestplate(lc);
		pi.setLeggings(ll);
		pi.setBoots(lb);
		for(int i=1; i<=34; i++)pi.addItem(soup);		
	}

	public void ArcherKit(Player p){
		PlayerInventory pi = p.getInventory();
		pi.clear();
		pi.setArmorContents(null);
		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());
		p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 18000, 0));
		ItemStack bow = new ItemStack(Material.BOW);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		ItemStack arrow = new ItemStack(Material.ARROW, 1);
		ItemStack lh = new ItemStack(Material.LEATHER_HELMET, 1);
		ItemStack lc = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		ItemStack ll = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		ItemStack lb = new ItemStack(Material.LEATHER_BOOTS, 1);
		lh.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		lc.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ll.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		lb.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP, 1);
		LeatherArmorMeta aa = (LeatherArmorMeta) lh.getItemMeta();
		LeatherArmorMeta ab = (LeatherArmorMeta) lc.getItemMeta();
		LeatherArmorMeta ac = (LeatherArmorMeta) ll.getItemMeta();
		LeatherArmorMeta ad = (LeatherArmorMeta) lb.getItemMeta();
		aa.setColor(Color.RED);
		ab.setColor(Color.YELLOW);
		ac.setColor(Color.BLUE);
		ad.setColor(Color.PURPLE);
		lh.setItemMeta(aa);
		lc.setItemMeta(ab);
		ll.setItemMeta(ac);
		lb.setItemMeta(ad);
		pi.setHelmet(lh);
		pi.setChestplate(lc);
		pi.setLeggings(ll);
		pi.setBoots(lb);
		pi.addItem(bow);
		for(int i=1; i<=32; i++)
			pi.addItem(soup);
		pi.addItem(arrow);
	}

	public void CupidKit(Player p){
		PlayerInventory pi = p.getInventory();
		pi.clear();
		pi.setArmorContents(null);
		for(PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());
		p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 18000, 1));
		ItemStack bow = new ItemStack(Material.BOW);
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
		ItemStack rape = new ItemStack(Material.MUSHROOM_SOUP, 1);
		ItemStack anal = new ItemStack(Material.ARROW, 1);
		ItemStack lick = new ItemStack(Material.FEATHER, 1);
		pi.addItem(sword,bow,lick);
		for(int i=1; i<=29; i++)
			pi.addItem(rape);
		pi.addItem(anal);
	}

	public void AssassinKit(Player p){
		PlayerInventory pi = p.getInventory();
		pi.clear();
		pi.setArmorContents(null);
		for(PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());
		p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2, 18000));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 4, 18000));
		ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
		ItemStack soups = new ItemStack(Material.MUSHROOM_SOUP, 1);
		ItemStack helm = new ItemStack(Material.CHAINMAIL_HELMET, 1);
		ItemStack chest = new ItemStack(Material.GOLD_CHESTPLATE, 1);
		ItemStack legs = new ItemStack(Material.GOLD_LEGGINGS, 1);
		ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
		boots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 9);
		pi.setHelmet(helm);
		pi.setChestplate(chest);
		pi.setLeggings(legs);
		pi.setBoots(boots);
		pi.addItem(sword);
		for(int i=1; i<=33; i++)pi.addItem(soups);
	}
}
