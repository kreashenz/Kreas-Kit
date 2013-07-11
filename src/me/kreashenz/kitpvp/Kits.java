package me.kreashenz.kitpvp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import me.kreashenz.kitpvp.utils.Functions;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Kits implements CommandExecutor {

	private KitPvP plugin;

	public Kits(KitPvP plugin){
		this.plugin = plugin;
	}

	protected List<String> assassin = new ArrayList<String>();

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args){
		if(s instanceof Player){
			Player p = (Player)s;
			if(cmd.getName().equalsIgnoreCase("archer")){
				if(p.hasPermission("kitpvp.archer")){
					if(!plugin.kits.hasAKit(p)){
						plugin.kits.addToKitTracker(p);
						ArcherKit(p);
					} else {
						Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.must-die-before-new-kit")));
					}
				} else Functions.noPerm(p);
			}
			if(cmd.getName().equalsIgnoreCase("pvp")){
				if(p.hasPermission("kitpvp.pvp")){
					if(!plugin.kits.hasAKit(p)){
						plugin.kits.addToKitTracker(p);
						PvPKit(p);
					} else {
						Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.must-die-before-new-kit")));
					}
				} else Functions.noPerm(p);
			}
			if(cmd.getName().equalsIgnoreCase("tank")){
				if(p.hasPermission("kitpvp.tank")){
					if(!plugin.kits.hasAKit(p)){
						plugin.kits.addToKitTracker(p);
						TankKit(p);
					} else {
						Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.must-die-before-new-kit")));
					}
				} else Functions.noPerm(p);
			}
			if(cmd.getName().equalsIgnoreCase("pyro")){
				if(p.hasPermission("kitpvp.pyro")){
					if(!plugin.kits.hasAKit(p)){
						plugin.kits.addToKitTracker(p);
						PyroKit(p);
					} else {
						Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.must-die-before-new-kit")));
					}
				} else Functions.noPerm(p);
			}
			if(cmd.getName().equalsIgnoreCase("refill")){
				if(p.hasPermission("kitpvp.refill")){
					ItemStack[] inv = p.getInventory().getContents();
					boolean gotBowl = false;
					for (ItemStack slot : inv) {
						if (slot != null && slot.getType() == Material.BOWL) {
							gotBowl = true;
							slot.setType(Material.MUSHROOM_SOUP);
						}
					}
					if (!gotBowl) {
						Functions.tell(p, "§cYou have no empty bowls!");
						return true; 
					}
					return true;
				} else Functions.noPerm(p);
			}
			if(cmd.getName().equalsIgnoreCase("medic")){
				if(p.hasPermission("kitpvp.medic")){
					if(!plugin.kits.hasAKit(p)){
						plugin.kits.addToKitTracker(p);
						MedicKit(p);
					} else {
						Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.must-die-before-new-kit")));
					}
				} else Functions.noPerm(p);
			}
			if(cmd.getName().equalsIgnoreCase("cupid")){
				if(p.hasPermission("kitpvp.cupid")){
					if(!plugin.kits.hasAKit(p)){
						plugin.kits.addToKitTracker(p);
						CupidKit(p);
					} else {
						Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.must-die-before-new-kit")));
					}
					if(!plugin.kits.hasCupidKit(p)){
						plugin.kits.addToCupidKit(p);
					}
				} else Functions.noPerm(p);
			}
			if(cmd.getName().equalsIgnoreCase("assassin")){
				if(p.hasPermission("kitpvp.assassin")){
					if(!plugin.kits.hasAKit(p)){
						plugin.kits.addToKitTracker(p);
						assassin.add(p.getName());
						AssassinKit(p);
					} else {
						Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.must-die-before-new-kit")));
					}
				} else Functions.noPerm(p);
			}
			if(cmd.getName().equalsIgnoreCase("knight")){
				if(p.hasPermission("")){
					if(!plugin.kits.hasAKit(p)){
						plugin.kits.addToKitTracker(p);
						KnightKit(p);
					} else {
						Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.must-die-before-new-kit")));
					}
				} else Functions.noPerm(p);
			}
			if(cmd.getName().equalsIgnoreCase("kkit")){
				if(args.length == 1){
					if(args[0].equalsIgnoreCase("list")){
						if(p.hasPermission("kitpvp.list")){
							String str = "";
							for(String st : plugin.getConfig().getConfigurationSection("Kits").getKeys(false)){
								if(p.hasPermission("kitpvp." + st)){
									st = "§a" + st + "§f";
								} else {
									st = "§c" + st + "§f";
								}
								str = str + st + ", ";
							}
							str = str.substring(0, str.length()-2);
							Functions.tell(p, "§8You can use the §agreen §8.");
							Functions.tell(p, str);
						} else Functions.noPerm(p);
					} else if(plugin.getConfig().getConfigurationSection("Kits").contains(args[0])){
						if(p.hasPermission("kitpvp."+args[0].toLowerCase())){
							if(!plugin.kits.hasAKit(p)){
								plugin.kits.addToKitTracker(p);
								plugin.kits.giveKit(p, args[0].toLowerCase());
							} else {
								Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.must-die-before-new-kit")));
							}
						} else Functions.noPerm(p);
					} else Functions.tell(p, "§cThat kit doesn't exist.");
				} else Functions.tell(p, "§cUsage : §f/kkit <kitName | list>");
			}
			if(cmd.getName().equalsIgnoreCase("stats")){
				DecimalFormat d = new DecimalFormat("##.##");
				if(p.hasPermission("kitpvp.stats")){
					FileConfiguration file = plugin.getConfig();
					if(args.length == 0){
						Functions.tell(p, "§7+§c--------------------------------------§c+");
						Functions.tell(p, "§7|| §6KDR §1: §a" + d.format(file.getDouble(p.getName() + ".kills") / file.getDouble(p.getName() + ".deaths")));
						Functions.tell(p, "§7|| §6Kills §1: §a" + file.getInt(p.getName() + ".kills"));
						Functions.tell(p, "§7|| §6Deaths §1: §a" + file.getInt(p.getName() + ".deaths"));
						Functions.tell(p, "§7+§c--------------------------------------§c+");
						if(!plugin.sb.hasBoard.contains(p.getName())){
							plugin.sb.setBoard(p);
							Functions.tell(p, "§aShowing your stats. Board will remove in 10 seconds.");
						} else Functions.tell(p, "§cYou already have a scoreboard up!");
					} else {
						Player t = Bukkit.getPlayer(args[0]);
						if(t.isOnline() && t != null){
							Functions.tell(p, "§7+§c--------------------------------------§c+");
							Functions.tell(p, "§7|| §6KDR §1: §a" + d.format(file.getDouble(t.getName() + ".kills") / file.getDouble(t.getName() + ".deaths")));
							Functions.tell(p, "§7|| §6Kills §1: §a" + file.getInt(t.getName() + ".kills"));
							Functions.tell(p, "§7|| §6Deaths §1: §a" + file.getInt(t.getName() + ".deaths"));
							Functions.tell(p, "§7+§c--------------------------------------§c+");
						} else Functions.tell(p, "§cThat player cannot be found, please try again.");
					}
				} else Functions.noPerm(p);
			}
		} else s.sendMessage("§cYou must be a player to use these commands.");
		return true;
	}

	private ItemStack ih = new ItemStack(Material.IRON_HELMET);
	private ItemStack ic = new ItemStack(Material.IRON_CHESTPLATE);
	private ItemStack il = new ItemStack(Material.IRON_LEGGINGS);
	private ItemStack ib = new ItemStack(Material.IRON_BOOTS);
	private ItemStack lh = new ItemStack(Material.LEATHER_HELMET);
	private ItemStack lc = new ItemStack(Material.LEATHER_CHESTPLATE);
	private ItemStack ll = new ItemStack(Material.LEATHER_LEGGINGS);
	private ItemStack lb = new ItemStack(Material.LEATHER_BOOTS);
	private ItemStack soups = new ItemStack(Material.MUSHROOM_SOUP);

	public void PyroKit(Player p){
		PlayerInventory pi = p.getInventory();
		clear(p);
		Functions.givePot(p, PotionEffectType.FIRE_RESISTANCE, 999999999, 1);
		ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
		ItemStack tnt = new ItemStack(Material.TNT, 16);
		Functions.name(tnt, "§rBombs");
		pi.setArmorContents(new ItemStack[] {ib, il, ic, ih});
		pi.addItem(sword, tnt);
		for(int i=1; i<=32; i++)pi.addItem(soups);
	}

	public void PvPKit(Player p){
		PlayerInventory pi = p.getInventory();
		clear(p);
		Functions.givePot(p, PotionEffectType.INCREASE_DAMAGE, 300, 0);
		ItemStack e = new ItemStack(Material.DIAMOND_SWORD, 1);
		e.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
		pi.setArmorContents(new ItemStack[] {ib, il, ic, ih});
		pi.addItem(e);
		for(int i=1; i<=34; i++)pi.addItem(soups);
	}

	public void TankKit(Player p){
		PlayerInventory pi = p.getInventory();
		clear(p);
		Functions.givePot(p, PotionEffectType.SLOW, 300, 2);
		Functions.givePot(p, PotionEffectType.INCREASE_DAMAGE, 300, 0);
		ItemStack dh = new ItemStack(Material.DIAMOND_HELMET, 1);
		ItemStack dc = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		ItemStack dl = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		ItemStack db = new ItemStack(Material.DIAMOND_BOOTS, 1);
		ItemStack e = new ItemStack(Material.IRON_SWORD, 1);
		pi.setArmorContents(new ItemStack[] {db, dl, dc, dh});
		pi.addItem(e);
		for(int i=1; i<=34; i++)pi.addItem(soups);
	}

	public void MedicKit(Player p) {
		PlayerInventory pi = p.getInventory();
		clear(p);
		ItemStack a = new ItemStack(Material.IRON_SWORD);
		lh.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		lc.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ll.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		lb.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		a.addEnchantment(Enchantment.DAMAGE_ALL, 0);
		a.addEnchantment(Enchantment.KNOCKBACK, 1);
		Functions.name(a, "§rScalpel");
		setColour(lh, Color.BLACK);
		setColour(lc, Color.BLACK);
		setColour(ll, Color.BLACK);
		setColour(lb, Color.BLACK);
		pi.addItem(a);
		pi.setArmorContents(new ItemStack[] {lb, ll, lc, lh});
		for(int i=1; i<=34; i++)pi.addItem(soups);		
	}

	public void ArcherKit(Player p){
		PlayerInventory pi = p.getInventory();
		clear(p);
		Functions.givePot(p, PotionEffectType.FAST_DIGGING, 15*60, 0);
		ItemStack bow = new ItemStack(Material.BOW);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		ItemStack arrow = new ItemStack(Material.ARROW, 1);
		lh.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		lc.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ll.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		lb.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		setColour(lh, Color.RED);
		setColour(lc, Color.YELLOW);
		setColour(ll, Color.BLUE);
		setColour(lb, Color.PURPLE);
		pi.setArmorContents(new ItemStack[] {lb, ll, lc, lh});
		pi.addItem(bow);
		for(int i=1; i<=32; i++)pi.addItem(soups);
		pi.addItem(arrow);
	}

	public void CupidKit(Player p){
		PlayerInventory pi = p.getInventory();
		clear(p);
		Functions.givePot(p, PotionEffectType.JUMP, 15*60, 1);
		ItemStack bow = new ItemStack(Material.BOW);
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
		ItemStack arrows = new ItemStack(Material.ARROW, 1);
		ItemStack feather = new ItemStack(Material.FEATHER, 1);
		pi.addItem(sword,bow,feather);
		for(int i=1; i<=29; i++)pi.addItem(soups);
		pi.addItem(arrows);
	}

	public void AssassinKit(Player p){
		PlayerInventory pi = p.getInventory();
		clear(p);
		Functions.givePot(p, PotionEffectType.FAST_DIGGING, 300, 2);
		Functions.givePot(p, PotionEffectType.SPEED, 300, 4);
		ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
		ItemStack helm = new ItemStack(Material.CHAINMAIL_HELMET, 1);
		ItemStack chest = new ItemStack(Material.GOLD_CHESTPLATE, 1);
		ItemStack legs = new ItemStack(Material.GOLD_LEGGINGS, 1);
		ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
		boots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 9);
		pi.setArmorContents(new ItemStack[] {boots, legs, chest, helm});
		pi.addItem(sword);
		for(int i=1; i<=33; i++)pi.addItem(soups);
	}

	public void KnightKit(Player p){
		PlayerInventory pi = p.getInventory();
		clear(p);
		Functions.givePot(p, PotionEffectType.HEALTH_BOOST, 10, 3);
		ItemStack sword = Functions.name(new ItemStack(Material.IRON_HOE), "§9Iron Halberd");
		ItemStack helm = new ItemStack(Material.CHAINMAIL_HELMET);
		ItemStack chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		ItemStack legs = new ItemStack(Material.CHAINMAIL_LEGGINGS);
		ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
		pi.setArmorContents(new ItemStack[] {boots, legs, chest, helm});
		pi.addItem(sword, new ItemStack(Material.BOW));
		for(int i = 1; i <= 32; i++)pi.addItem(soups);
		pi.addItem(new ItemStack(Material.ARROW, 64));
		Horse horse = p.getWorld().spawn(p.getLocation(), Horse.class);
		horse.setTamed(true);
		horse.setOwner(p);
		HorseInventory inv = horse.getInventory();
		horse.setAdult();
		horse.setVariant(Variant.HORSE);
		horse.setPassenger(p);
		inv.setArmor(new ItemStack(Material.IRON_BARDING));
		inv.setSaddle(new ItemStack(Material.SADDLE));
	}

	private ItemStack setColour(ItemStack item, Color color){
		LeatherArmorMeta meta = (LeatherArmorMeta)item.getItemMeta();
		meta.setColor(color);
		item.setItemMeta(meta);
		return item;
	}

	private void clear(Player p){
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		for(PotionEffect effect : p.getActivePotionEffects())p.removePotionEffect(effect.getType());
	}

}