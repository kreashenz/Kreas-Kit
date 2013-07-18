package me.kreashenz.kitpvp;

import java.util.ArrayList;
import java.util.List;

import me.kreashenz.kitpvp.utils.Functions;

import org.bukkit.Color;
import org.bukkit.Material;
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

public class Kits {

	protected List<String> assassin = new ArrayList<String>();

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