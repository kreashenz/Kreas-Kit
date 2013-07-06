package me.kreashenz.kitpvp;

import java.util.ArrayList;
import java.util.List;

import me.kreashenz.kitpvp.utils.Functions;
import me.kreashenz.kitpvp.utils.KillstreakUtils;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Events implements Listener {

	private KitPvP plugin;
	public Events(KitPvP plugin){
		this.plugin=plugin;
	}

	private List<String> cooldown = new ArrayList<String>();

	@EventHandler
	public void PlayerDeath(PlayerDeathEvent e) {
		if(e.getEntity() instanceof Player && e.getEntity().getKiller() instanceof Player){
			Player p = e.getEntity();
			Player k = p.getKiller();
			if (plugin.kits.hasAKit(p)){
				plugin.kits.takeFromKitTracker(p);
			}
			if (plugin.kits.hasCupidKit(p)){
				plugin.kits.takeFromCupitKit(p);
			}
			double perKill = plugin.getConfig().getDouble("Money-To-Give-Per-Kill");
			EconomyResponse r = plugin.econ.depositPlayer(k.getName(), perKill);
			if(r.transactionSuccess() && k!=null)k.sendMessage("§6You have been rewarded with §a$" + perKill + "§6 for killing §a" + p.getName());

			KillstreakUtils.setStreaks(p, 0);
			KillstreakUtils.setStreaks(k, KillstreakUtils.getStreaks(k) + 1);

			k.sendMessage("§eYou now have killed §a" + KillstreakUtils.getStreaks(k) + "§e player(s)!");

			switch(KillstreakUtils.getStreaks(k)){
			case 3:
				k.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 0));
				k.playSound(k.getLocation(), Sound.LEVEL_UP, 1, 1);
				Bukkit.broadcastMessage("§a" + k.getName() + " §6has come up with a §a3 §6killstreak!");
				EconomyResponse rf = plugin.econ.depositPlayer(k.getName(), plugin.getConfig().getInt("Money-To-Give-On-3-KillStreak"));
				if (rf.transactionSuccess())k.sendMessage("§6You have received §a$" + plugin.getConfig().getInt("Money-To-Give-On-3-KillStreak") + "§6 for killing §a" + p.getName());
				break;
			case 5:
				k.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999999, 0));
				k.playSound(k.getLocation(), Sound.LEVEL_UP, 1, 1);
				Bukkit.broadcastMessage("§a" + k.getName() + " §6is on a §a5§6 killstreak, so watch out!");
				EconomyResponse ra = plugin.econ.depositPlayer(k.getName(), plugin.getConfig().getInt("Money-To-Give-On-5-KillStreak"));
				if (ra.transactionSuccess())
					k.sendMessage("§6You have received §a$" + plugin.getConfig().getInt("Money-To-Give-On-5-KillStreak") + "§6 for killing §a" + p.getName());
				break;
			case 7:
				ItemStack a = new ItemStack(Material.SNOW_BALL, 3);
				ItemMeta aa = a.getItemMeta();
				aa.setDisplayName("§rGrenade");
				a.setItemMeta(aa);
				k.getInventory().addItem(a);
				k.playSound(k.getLocation(), Sound.LEVEL_UP, 1, 1);
				k.sendMessage("§aEnjoy your rewards, use them wisely, though.");
				Bukkit.broadcastMessage("§a" + k.getName() + " §6is on a killer §a7§6 killstreak, watch out!");
				EconomyResponse rb = plugin.econ.depositPlayer(k.getName(), plugin.getConfig().getInt("Money-To-Give-On-7-KillStreak"));
				if (rb.transactionSuccess())
					k.sendMessage("§6You have recieved §a$" + plugin.getConfig().getInt("Money-To-Give-On-7-KillStreak") + "§6 for killing §a" + p.getName());
				break;
			case 10:
				ItemStack helm = k.getInventory().getHelmet();
				ItemStack chest = k.getInventory().getChestplate();
				ItemStack legs = k.getInventory().getLeggings();
				ItemStack boots = k.getInventory().getBoots();
				helm.setDurability(helm.getType().getMaxDurability());
				chest.setDurability(chest.getType().getMaxDurability());
				legs.setDurability(legs.getType().getMaxDurability());
				boots.setDurability(boots.getType().getMaxDurability());
				k.getInventory().setHelmet(helm);
				k.getInventory().setChestplate(chest);
				k.getInventory().setLeggings(legs);
				k.getInventory().setBoots(boots);
				k.sendMessage("§6You are on a great 10 kill streak!! Keep going! You have been rewarded by having your §aarmour fixed§6, and §ahaste §aadded!");
				k.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999999, 0));
				k.playSound(k.getLocation(), Sound.LEVEL_UP, 1, 1);
				Bukkit.broadcastMessage("§a" + k.getName() + " §6is on an epic 10 killstreak! Keep him out of your way, or you might get mauled!");
				EconomyResponse rc = plugin.econ.depositPlayer(k.getName(), plugin.getConfig().getInt("Money-To-Give-On-10-KillStreak"));
				if (rc.transactionSuccess())
					k.sendMessage("§6You have recieved §a$" + plugin.getConfig().getInt("Money-To-Give-On-10-KillStreak") + "§6 for killing §a" + p.getName());
				break;
			case 15:
				k.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 20*20, 0));
				k.playSound(k.getLocation(), Sound.LEVEL_UP, 1, 1);
				Bukkit.broadcastMessage("§a" + k.getName() + " §6is on a §aMASSACRE§6!! Stay the hell outta his way!");
				EconomyResponse rd = plugin.econ.depositPlayer(k.getName(), plugin.getConfig().getInt("Money-To-Give-On-15-KillStreak"));
				if (rd.transactionSuccess())
					k.sendMessage("§6You have recieved §a$" + plugin.getConfig().getInt("Money-To-Give-On-15-KillStreak") + "§6 for killing §a" + p.getName());
				break;
			case 20:
				if(plugin.kits.hasAKit(k)){
					plugin.kits.takeFromKitTracker(k);
					k.getInventory().addItem(new ItemStack(Material.EGG, 5));
					k.sendMessage("§aYou have permission to you a new kit!");
					k.sendMessage("§6You have been given some §aFire Grenades§6!");
					k.playSound(k.getLocation(), Sound.LEVEL_UP, 1, 1);
					EconomyResponse re = plugin.econ.depositPlayer(k.getName(), plugin.getConfig().getInt("Money-To-Give-On-20-KillStreak"));
					if (re.transactionSuccess())
						k.sendMessage("§6You have recieved §a$" + plugin.getConfig().getInt("Money-To-Give-On-20-KillStreak") + "§6 for killing §a" + p.getName());
				}
				break;
			}
			if(KillstreakUtils.getStreaks(k) > 20){
				k.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*20, 0));
				k.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 20*20, 0));
				k.playSound(k.getLocation(), Sound.LEVEL_UP, 1, 1);
			}
		}
	}

	@EventHandler
	public void PlayerDropItemsOnDeath(PlayerDeathEvent e){
		if(!plugin.getConfig().getBoolean("allow.dropsOnDeath")){
			e.setDroppedExp(0);
			e.getDrops().clear();
		}
		if(e.getEntity() instanceof Player){
			if(plugin.kits.hasAKit(e.getEntity())){
				plugin.kits.whoHasAKit.remove(e.getEntity().getName());
				e.getEntity().sendMessage("§aYou can now use another kit.");
			}
		}
	}

	@EventHandler
	public void PlayerJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		FileConfiguration a = plugin.getConfig();
		if(!a.contains(p.getName() + ".kills")){
			a.set(p.getName() + ".kills", "0");
			a.set(p.getName() + ".deaths", "0");
			a.set(p.getName() + ".KDR", "0");
			a.set(p.getName() + ".killstreaks", "0");
			plugin.saveConfig();
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onTnTThrow(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(plugin.getConfig().getBoolean("MustHaveKitToThrowTNT", true)){
			if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK){
				if(p.getItemInHand().getType() == Material.TNT){
					if(p.getItemInHand().getAmount()>=16) {
						TNTPrimed tnt = p.getWorld().spawn(p.getLocation().add(0.65, 1.65, 0), TNTPrimed.class);
						tnt.setVelocity(p.getLocation().getDirection().multiply(2));
						tnt.setFuseTicks(40);
						if(p.getItemInHand().getAmount()>16) {p.getItemInHand().setAmount(p.getItemInHand().getAmount()-16);
						} else {
							p.setItemInHand(new ItemStack(Material.AIR, 0));
						}
						p.updateInventory();
					}
				}
			}
		}
	}

	@EventHandler
	public void onSoupConsume(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack bowl = new ItemStack(Material.BOWL, 1);
		try {
			if (e.getAction() == Action.RIGHT_CLICK_AIR && p.getItemInHand().getType() == Material.MUSHROOM_SOUP 
					&& p.getHealth() + 7 <= 20) {
				e.setCancelled(true);
				p.getInventory().setItemInHand(bowl);
				p.setHealth(p.getHealth() + 6);
			}
			if (e.getAction() == Action.RIGHT_CLICK_AIR && p.getItemInHand().getType() == Material.MUSHROOM_SOUP 
					&& p.getHealth() + 6 > 20) {
				e.setCancelled(true);
				p.getInventory().setItemInHand(bowl);
				p.setHealth(20);
			}
		} catch (IllegalArgumentException iea){}
	}

	@EventHandler
	public void itemDrop(PlayerDropItemEvent e){
		if (plugin.getConfig().getBoolean("allow.itemdropping") == false){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onGrenadeHit(ProjectileHitEvent e){
		World w = e.getEntity().getWorld();
		if(e.getEntity() instanceof Egg){
			Egg ent = (Egg)e.getEntity();
			Location explosion = ent.getLocation();
			for(Entity player : ent.getNearbyEntities(1,0,1))
				player.setFireTicks(7*20);
			for(Entity player : ent.getNearbyEntities(2,1,2))
				player.setFireTicks(5*20);
			for(Entity player : ent.getNearbyEntities(3,2,3))
				player.setFireTicks(4*20);
			for(Entity player : ent.getNearbyEntities(4,3,4))
				player.setFireTicks(3*20);
			for(Entity player : ent.getNearbyEntities(5,4,5))
				player.setFireTicks(2*20);
			for(Entity player : ent.getNearbyEntities(6,5,6))
				player.setFireTicks(1*20);
			w.createExplosion(explosion.getX(), explosion.getY(), explosion.getZ(), 2F, false, false);
			w.playSound(ent.getLocation(), Sound.EXPLODE, 12, 14);
			ent.setShooter(e.getEntity().getShooter());
		}
		if(e.getEntity() instanceof Snowball){
			Location loc = e.getEntity().getLocation();
			w.createExplosion(loc.getX(), loc.getY(), loc.getZ(), 2F, false, false);
			e.getEntity().setShooter(e.getEntity().getShooter());
		}
	}

	@EventHandler
	public void onChickenSpawnFromEgg(CreatureSpawnEvent e){
		if(e.getSpawnReason() == SpawnReason.EGG){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e){
		Player player = e.getPlayer();
		KillstreakUtils.setStreaks(player, 0);
	}

	@EventHandler
	public void onFeatherFly(PlayerInteractEvent e){
		final Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_AIR
				||e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(p.getItemInHand().getType() == Material.FEATHER){
				if(!cooldown.contains(p.getName())){
					if(p.getGameMode() == GameMode.CREATIVE){
						Functions.tell(p, "§cYou are already in fly mode, silly :)");
					} else {
						cooldown.add(p.getName());
						p.setAllowFlight(true);
						Functions.tell(p, "§aCupid fly mode, activated!");
					}
					new BukkitRunnable(){
						@Override
						public void run(){
							if(p.getGameMode() == GameMode.CREATIVE){
								p.setAllowFlight(true);
							} else {
								p.setAllowFlight(false);
								Functions.tell(p, "§cYour arms are too sore to fly again, maybe sometime soon..");
							}
						}
					}.runTaskLater(plugin, plugin.getConfig().getInt("Fly-Length-Cupid-Kit")*20);
					new BukkitRunnable(){
						@Override
						public void run(){
							cooldown.remove(p.getName());
							Functions.tell(p, "§aYour arms are feeling good again.");
						}
					}.runTaskLater(plugin, plugin.getConfig().getInt("Fly-Cooldown-Cupid-Kit")*20);
				}
			}
		}
	}

	@EventHandler
	public void onCupidFall(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(e.getCause() == DamageCause.FALL){
				if(plugin.kits.hasCupidKit(p)){
					e.setCancelled(true);
				} else {
					e.setCancelled(false);
				}
			}
		}
	}

}