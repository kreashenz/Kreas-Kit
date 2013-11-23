package me.kreashenz.kitpvp.utils;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.logging.Level;

import me.kreashenz.kitpvp.KitPvP;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeWrapper;

public class PManager {

	private Player p;
	private PlayerInventory pi;
	private String name;
	private KitPvP plugin;

	private static HashMap<Player, PManager> manager = new HashMap<Player, PManager>();
	private HashMap<String, String> kit = new HashMap<String, String>();

	private ItemStack helm;
	private ItemStack chest;
	private ItemStack legs;
	private ItemStack boots;
	private ItemStack sword;

	private PManager(Player p){
		this.p = p;
		this.pi = p.getInventory();
		this.name = p.getName();

		this.plugin = KitPvP.getInstance();
	}

	public static PManager getPManager(Player p){
		if(manager.containsKey(p)){
			return manager.get(p);
		} else {
			manager.put(p, new PManager(p));
			return manager.get(p);
		}
	}

	public void releaseManager(){
		manager.remove(p);
	}

	public boolean hasKit(){
		return (kit.containsKey(name) ? true : false);
	}

	@SuppressWarnings("deprecation")
	public void giveKit(String kit){
		FileConfiguration a = plugin.getConfig();
		int slot;
		for(slot = 0; slot <= 35; slot++){
			ItemStack i = new ItemStack(0);
			String aSlot = a.getString("Kits." + kit + ".items." + slot);
			if (a.contains(aSlot) && !aSlot.equals("0") && !aSlot.equals("")) {
				String[] s = aSlot.split(" ");
				String[] item = s[0].split(":");
				i.setTypeId(Integer.parseInt(item[0]));
				if(item.length > 1){
					i.setAmount(Integer.parseInt(item[1]));
					if(item.length > 2){
						i.setDurability((short)Integer.parseInt(item[2]));
					}
				} else {
					i.setAmount(1);
				}
				boolean one = true;
				if(s.length > 1){
					for(String b : s){
						if(!one){
							String[] ench = b.split(":");
							Enchantment enchNo = new EnchantmentWrapper(Integer.parseInt(ench[0]));
							int enchLvl = Integer.parseInt(ench[1]);
							i.addUnsafeEnchantment(enchNo, enchLvl);
						}
					}
				}
				pi.setItem(slot, i);
			}
		}

		String helm = a.getString("Kits." + kit + ".items.helmet");
		String chest = a.getString("Kits." + kit + ".items.chestplate");
		String legs = a.getString("Kits." + kit + ".items.leggings");
		String boot = a.getString("Kits." + kit + ".items.boots");

		int helmColour = 0;
		int chestColour = 0;
		int legsColour = 0;
		int bootColour = 0;

		ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET, 1);
		ItemStack lchestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		ItemStack lleggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		ItemStack lboots = new ItemStack(Material.LEATHER_BOOTS, 1);

		ItemStack ihelmet = new ItemStack(Material.IRON_HELMET, 1);
		ItemStack ichestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
		ItemStack ileggings = new ItemStack(Material.IRON_LEGGINGS, 1);
		ItemStack iboots = new ItemStack(Material.IRON_BOOTS, 1);

		ItemStack ghelmet = new ItemStack(Material.GOLD_HELMET, 1);
		ItemStack gchestplate = new ItemStack(Material.GOLD_CHESTPLATE, 1);
		ItemStack gleggings = new ItemStack(Material.GOLD_LEGGINGS, 1);
		ItemStack gboots = new ItemStack(Material.GOLD_BOOTS, 1);

		ItemStack dhelmet = new ItemStack(Material.DIAMOND_HELMET, 1);
		ItemStack dchestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		ItemStack dleggings = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		ItemStack dboots = new ItemStack(Material.DIAMOND_BOOTS, 1);

		ItemStack chelmet = new ItemStack(Material.CHAINMAIL_HELMET, 1);
		ItemStack cchestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
		ItemStack cleggings = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
		ItemStack cboots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);

		ItemStack helmet = null;
		ItemStack chestplate = null;
		ItemStack leggings = null;
		ItemStack boots = null;

		if(a.contains("Kits." + kit + ".items.helmetColour")){
			helmColour = Integer.parseInt(a.getString("Kits." + kit + ".helmetColour").replace("#", ""), 16);
			lhelmet = setColour(lhelmet, helmColour);
		}

		if(a.contains("Kits." + kit + ".items.chestplateColour")){
			chestColour = Integer.parseInt(a.getString("Kits." + kit + ".chestColour").replace("#", ""), 16);
			lchestplate = setColour(lchestplate, chestColour);
		}

		if(a.contains("Kits." + kit + ".items.legsColour")){
			legsColour = Integer.parseInt(a.getString("Kits." + kit + ".legsColour").replace("#", ""), 16);
			lleggings = setColour(lleggings, legsColour);
		}

		if(a.contains("Kits." + kit + ".items.bootsColour")){
			bootColour = Integer.parseInt(a.getString("Kits." + kit + ".bootsColour").replace("#", ""), 16);
			lboots = setColour(lboots, bootColour);
		}

		if (helm != null) {
			if (helm.equalsIgnoreCase("leather"))helmet = lhelmet;
			if (helm.equalsIgnoreCase("iron"))helmet = ihelmet;
			if (helm.equalsIgnoreCase("gold"))helmet = ghelmet;
			if (helm.equalsIgnoreCase("diamond"))helmet = dhelmet;
			if (helm.equalsIgnoreCase("chainmail"))helmet = chelmet;
			if (helm.equalsIgnoreCase("redwool"))helmet = new ItemStack(Material.WOOL, 1, (short)14);
			if (helm.equalsIgnoreCase("bluewool"))helmet = new ItemStack(Material.WOOL, 1, (short)11);
		}

		if (chest != null) {
			if (chest.equalsIgnoreCase("leather"))chestplate = lchestplate;
			if (chest.equalsIgnoreCase("iron"))chestplate = ichestplate;
			if (chest.equalsIgnoreCase("gold"))chestplate = gchestplate;
			if (chest.equalsIgnoreCase("diamond"))chestplate = dchestplate;
			if (chest.equalsIgnoreCase("chainmail"))chestplate = cchestplate;
		}

		if (legs != null) {
			if (legs.equalsIgnoreCase("leather"))leggings = lleggings;
			if (legs.equalsIgnoreCase("iron"))leggings = ileggings;
			if (legs.equalsIgnoreCase("gold"))leggings = gleggings;
			if (legs.equalsIgnoreCase("diamond"))leggings = dleggings;
			if (legs.equalsIgnoreCase("chainmail"))leggings = cleggings;
		}

		if (boot != null) {
			if (boot.equalsIgnoreCase("leather"))boots = lboots;
			if (boot.equalsIgnoreCase("iron"))boots = iboots;
			if (boot.equalsIgnoreCase("gold"))boots = gboots;
			if (boot.equalsIgnoreCase("diamond"))boots = dboots;
			if (boot.equalsIgnoreCase("chainmail"))boots = cboots;
		}

		short b = (short) a.getInt("Kits." + kit + ".items.helmetDurability");
		short c = (short) a.getInt("Kits." + kit + ".items.chestplateDurability");
		short d = (short) a.getInt("Kits." + kit + ".items.leggingsDurability");
		short e = (short) a.getInt("Kits." + kit + ".items.bootsDurability");
		if (b == -1) b = helmet.getType().getMaxDurability();
		if (c == -1) c = chestplate.getType().getMaxDurability();
		if (d == -1) d = leggings.getType().getMaxDurability();
		if (e == -1) e = boots.getType().getMaxDurability();
		if (helmet != null) helmet.setDurability(b);
		if (chestplate != null) chestplate.setDurability(c);
		if (leggings != null) leggings.setDurability(d);
		if (boots != null) boots.setDurability(e);

		if(a.contains("Kits." + kit + ".items.helmetEnchant") && helmet != null){
			for(String ench : a.getString("Kits." + kit + ".items.helmetEnchant").split(" ")){
				String[] enchant = ench.split(":");
				Enchantment enchNo = new EnchantmentWrapper(Integer.parseInt(enchant[0]));
				int enchLvl = Integer.parseInt(enchant[1]);
				helmet.addUnsafeEnchantment(enchNo, enchLvl);
			}
		}

		if(a.contains("Kits." + kit + ".items.chestplateEnchant") && helmet != null){
			for(String ench : a.getString("Kits." + kit + ".items.chestplateEnchant").split(" ")){
				String[] enchant = ench.split(":");
				Enchantment enchNo = new EnchantmentWrapper(Integer.parseInt(enchant[0]));
				int enchLvl = Integer.parseInt(enchant[1]);
				chestplate.addUnsafeEnchantment(enchNo, enchLvl);
			}
		}

		if(a.contains("Kits." + kit + ".items.leggingsEnchant") && helmet != null){
			for(String ench : a.getString("Kits." + kit + ".items.leggingEnchant").split(" ")){
				String[] enchant = ench.split(":");
				Enchantment enchNo = new EnchantmentWrapper(Integer.parseInt(enchant[0]));
				int enchLvl = Integer.parseInt(enchant[1]);
				leggings.addUnsafeEnchantment(enchNo, enchLvl);
			}
		}

		if(a.contains("Kits." + kit + ".items.bootsEnchant") && helmet != null){
			for(String ench : a.getString("Kits." + kit + ".items.bootsEnchant").split(" ")){
				String[] enchant = ench.split(":");
				Enchantment enchNo = new EnchantmentWrapper(Integer.parseInt(enchant[0]));
				int enchLvl = Integer.parseInt(enchant[1]);
				boots.addUnsafeEnchantment(enchNo, enchLvl);
			}
		}
		if(helmet != null)p.getInventory().setHelmet(helmet);
		if(chestplate != null)p.getInventory().setChestplate(chestplate);
		if(leggings != null)p.getInventory().setLeggings(leggings);
		if(boots != null)p.getInventory().setBoots(boots);

		if(a.contains("Kits." + kit + ".potionEffects")){
			for(String pots : a.getStringList("Kits." + kit + ".potionEffects")){
				String[] pot = pots.split(":");
				try {
					Constructor<PotionEffectTypeWrapper> constructor = PotionEffectTypeWrapper.class.getDeclaredConstructor();
					constructor.setAccessible(true);
					PotionEffectType potEff = constructor.newInstance(Integer.parseInt(pot[0]));
					int lvl = Integer.parseInt(pot[1]);
					int duration = Integer.parseInt(pot[2])*20;
					p.addPotionEffect(new PotionEffect(potEff, duration, lvl));
				} catch (Exception e1){
					Functions.log(Level.SEVERE, "Error trying to get into the 'PotionEffectTypeWrapper' class.");
					e1.printStackTrace();
				}
			}
		}

		if(a.contains("Kits." + kit + ".spawnHorse")){
			if(a.getBoolean("Kits." + kit + ".spawnHorse") == true){
				Horse horse = p.getWorld().spawn(p.getLocation(), Horse.class);
				HorseInventory horseInv = horse.getInventory();
				if(a.contains("Kits." + kit + ".horseArmour")){
					String path = a.getString("Kits." + kit + ".horseArmour");
					if(path.equalsIgnoreCase("iron"))horseInv.setArmor(new ItemStack(Material.IRON_BARDING));
					else if(path.equalsIgnoreCase("gold"))horseInv.setArmor(new ItemStack(Material.GOLD_BARDING));
					else if(path.equalsIgnoreCase("diamond"))horseInv.setArmor(new ItemStack(Material.DIAMOND_BARDING));
					else Functions.log(Level.WARNING, "Tried setting armour to an invalid type, available types: Iron, Gold, Diamond.");
				}
				if(a.contains("Kits." + kit + ".forceRidingHorse") && a.getBoolean("Kits." + kit + ".forceRidingHorse") == true){
					horseInv.setSaddle(new ItemStack(Material.SADDLE));
					horse.setPassenger(p);
				}
				if(a.contains("Kits." + kit + ".horseColour")){
					String path = a.getString("Kits." + kit + ".horseColour");
					if(path.equalsIgnoreCase("black"))horse.setColor(Color.BLACK);
					else if(path.equalsIgnoreCase("brown"))horse.setColor(Color.BROWN);
					else if(path.equalsIgnoreCase("chestnut"))horse.setColor(Color.CHESTNUT);
					else if(path.equalsIgnoreCase("creamy"))horse.setColor(Color.CREAMY);
					else if(path.equalsIgnoreCase("dark_brown"))horse.setColor(Color.DARK_BROWN);
					else if(path.equalsIgnoreCase("gray"))horse.setColor(Color.GRAY);
					else if(path.equalsIgnoreCase("white"))horse.setColor(Color.WHITE);
					else Functions.log(Level.WARNING, "Tried setting the horse's color to one not found.");
				}
				if(a.contains("Kits." + kit + ".horseType")){
					String path = a.getString("Kits." + kit + ".horseType");
					if(path.equalsIgnoreCase("horse"))horse.setVariant(Variant.HORSE);
					else if(path.equalsIgnoreCase("donkey"))horse.setVariant(Variant.DONKEY);
					else if(path.equalsIgnoreCase("mule"))horse.setVariant(Variant.MULE);
					else if(path.equalsIgnoreCase("undead"))horse.setVariant(Variant.UNDEAD_HORSE);
					else if(path.equalsIgnoreCase("skeletal"))horse.setVariant(Variant.SKELETON_HORSE);
					else Functions.log(Level.WARNING, "Tried setting the horse's type to one wasn't found.");
				}
			}
		}
		if(kit.equalsIgnoreCase("knight")){
			pi = p.getInventory();
			clear();
			Functions.givePot(p, PotionEffectType.HEALTH_BOOST, 10, 3);
			sword = Functions.name(new ItemStack(Material.IRON_HOE), "§9Iron Halberd");
			this.helm = new ItemStack(Material.CHAINMAIL_HELMET);
			this.chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
			this.legs = new ItemStack(Material.CHAINMAIL_LEGGINGS);
			this.boots = new ItemStack(Material.CHAINMAIL_BOOTS);
			pi.setArmorContents(new ItemStack[] {this.boots, this.legs, this.chest, this.helm});
			pi.addItem(sword, new ItemStack(Material.BOW));
			refill(32);
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
		else if(kit.equalsIgnoreCase("pyro")){
			pi = p.getInventory();
			clear();
			Functions.givePot(p, PotionEffectType.FIRE_RESISTANCE, 999999999, 1);
			sword = new ItemStack(Material.STONE_SWORD, 1);		
			pi.setArmorContents(new ItemStack[] {new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_HELMET)});
			pi.addItem(sword, Functions.name(new ItemStack(Material.TNT, 16), "§rBombs"));
			refill(32);
		} else if(kit.equalsIgnoreCase("pvp")){
			pi = p.getInventory();
			clear();
			Functions.givePot(p, PotionEffectType.INCREASE_DAMAGE, 300, 0);
			sword = new ItemStack(Material.DIAMOND_SWORD, 1);
			sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
			pi.setArmorContents(new ItemStack[] {new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_HELMET)});
			pi.addItem(sword);
			refill(34);
		} else if(kit.equalsIgnoreCase("tank")){
			pi = p.getInventory();
			clear();
			Functions.givePot(p, PotionEffectType.SLOW, 300, 2);
			Functions.givePot(p, PotionEffectType.INCREASE_DAMAGE, 300, 0);
			this.helm = new ItemStack(Material.DIAMOND_HELMET, 1);
			this.chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
			this.legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
			this.boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
			sword = new ItemStack(Material.IRON_SWORD, 1);
			pi.setArmorContents(new ItemStack[] {this.boots, this.legs, this.chest, this.helm});
			pi.addItem(sword);
			refill(34);
		} else if(kit.equalsIgnoreCase("medic")){
			pi = p.getInventory();
			clear();
			sword = Functions.name(new ItemStack(Material.IRON_SWORD), "§rScalpel");
			this.helm = setColour(new ItemStack(Material.LEATHER_HELMET), org.bukkit.Color.BLACK);
			this.chest = setColour(new ItemStack(Material.LEATHER_CHESTPLATE), org.bukkit.Color.BLACK);
			this.legs = setColour(new ItemStack(Material.LEATHER_LEGGINGS), org.bukkit.Color.BLACK);
			this.boots = setColour(new ItemStack(Material.LEATHER_BOOTS), org.bukkit.Color.BLACK);
			this.helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			this.chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			this.legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			sword.addEnchantment(Enchantment.DAMAGE_ALL, 0);
			sword.addEnchantment(Enchantment.KNOCKBACK, 1);
			pi.addItem(sword);
			pi.setArmorContents(new ItemStack[] {this.boots, this.legs, this.chest, this.helm});
			refill(34);
		} else if(kit.equalsIgnoreCase("archer")){
			pi = p.getInventory();
			clear();
			Functions.givePot(p, PotionEffectType.FAST_DIGGING, 15*60, 0);
			ItemStack bow = new ItemStack(Material.BOW);
			bow.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
			bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
			this.helm = new ItemStack(setColour(new ItemStack(Material.LEATHER_HELMET), org.bukkit.Color.RED));
			this.chest = new ItemStack(setColour(new ItemStack(Material.LEATHER_CHESTPLATE), org.bukkit.Color.YELLOW));
			this.legs = new ItemStack(setColour(new ItemStack(Material.LEATHER_LEGGINGS), org.bukkit.Color.BLUE));
			this.boots = new ItemStack(setColour(new ItemStack(Material.LEATHER_BOOTS), org.bukkit.Color.PURPLE));
			this.helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			this.chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			this.legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			this.boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			pi.setArmorContents(new ItemStack[] {this.boots, this.legs, this.chest, this.helm});
			pi.addItem(bow);
			refill(32);
			pi.addItem(new ItemStack(Material.ARROW));
		} else if(kit.equalsIgnoreCase("cupid")){
			pi = p.getInventory();
			clear();
			Functions.givePot(p, PotionEffectType.JUMP, 15*20, 1);
			ItemStack bow = new ItemStack(Material.BOW);
			bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
			sword = new ItemStack(Material.STONE_SWORD, 1);
			pi.addItem(sword, bow, new ItemStack(Material.FEATHER, 1));
			refill(29);
			pi.addItem(new ItemStack(Material.ARROW));
		} else if(kit.equalsIgnoreCase("assassin")){
			pi = p.getInventory();
			clear();
			Functions.givePot(p, PotionEffectType.FAST_DIGGING, 300, 2);
			Functions.givePot(p, PotionEffectType.SPEED, 300, 4);
			sword = new ItemStack(Material.IRON_SWORD, 1);
			this.helm = new ItemStack(Material.CHAINMAIL_HELMET, 1);
			this.chest = new ItemStack(Material.GOLD_CHESTPLATE, 1);
			this.legs = new ItemStack(Material.GOLD_LEGGINGS, 1);
			this.boots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
			this.boots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 9);
			pi.setArmorContents(new ItemStack[] {this.boots, this.legs, this.chest, this.helm});
			pi.addItem(sword);
			refill(33);
			plugin.assassinList.add(name);
		}
		Functions.tell(p, "§7You chose the §6" + kit + "§7 kit!");
		this.kit.put(p.getName(), kit);
		p.updateInventory();
	}

	private ItemStack setColour(ItemStack item, int color){
		LeatherArmorMeta im = (LeatherArmorMeta) item.getItemMeta();
		im.setColor(org.bukkit.Color.fromRGB(color));
		item.setItemMeta(im);
		return item;
	}

	private ItemStack setColour(ItemStack item, org.bukkit.Color color){
		LeatherArmorMeta im = (LeatherArmorMeta) item.getItemMeta();
		im.setColor(color);
		item.setItemMeta(im);
		return item;
	}

	private void clear(){
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		for(PotionEffect effect : p.getActivePotionEffects())p.removePotionEffect(effect.getType());
	}

	private void refill(int amount){
		pi = p.getInventory();
		for(int i = 1; i <= amount; i++)pi.addItem(new ItemStack(Material.MUSHROOM_SOUP));
	}

	public void removeKit() {
		this.kit.remove(name);
	}

	public String getKit(){
		return (kit.containsKey(name) ? kit.get(name) : null);
	}

}
