package me.kreashenz.kitpvp;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import me.kreashenz.kitpvp.utils.Functions;

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
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeWrapper;

public class KitManager {

	private KitPvP plugin;

	public List<String> whoHasAKit = new ArrayList<String>();
	public List<String> isInCupidKit = new ArrayList<String>();

	public KitManager(KitPvP plugin) {
		this.plugin = plugin;
	}

	public boolean hasAKit(Player p){
		if(whoHasAKit.contains(p.getName())){
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public void giveKit(Player p, String kit){
		FileConfiguration a = plugin.getConfig();
		int slot;
		for(slot = 0; slot<=35; slot++){
			ItemStack i = new ItemStack(0);
			String aSlot = a.getString("Kits." + kit + ".items." + slot);
			if (a.contains(aSlot) &&
					!aSlot.equals("0") && !aSlot.equals("")) {
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
				p.getInventory().setItem(slot, i);
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
		p.updateInventory();
	}

	private ItemStack setColour(ItemStack item, int colour){
		LeatherArmorMeta im = (LeatherArmorMeta) item.getItemMeta();
		im.setColor(org.bukkit.Color.fromRGB(colour));
		item.setItemMeta(im);
		return item;
	}

}