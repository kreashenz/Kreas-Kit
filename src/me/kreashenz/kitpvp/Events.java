package me.kreashenz.kitpvp;

import java.io.File;
import java.io.IOException;
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
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
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
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Events implements Listener {

	private KitPvP plugin;
	private KillstreakUtils streakUtils;

	public Events(KitPvP plugin){
		this.plugin = plugin;
		this.streakUtils = plugin.streakUtils;
	}

	private List<String> cooldown = new ArrayList<String>();

	@EventHandler
	public void PlayerDeath(EntityDeathEvent e) {
		if(e.getEntity() instanceof Player && e.getEntity().getKiller() instanceof Player){
			Player p = (Player)e.getEntity();
			Player k = p.getKiller();

			checkStreak(k);

			if (plugin.kits.whoHasAKit.contains(p.getName())){
				Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.new-kit-permission")));
				plugin.kits.whoHasAKit.remove(p.getName());
			}

			if (plugin.kits.isInCupidKit.contains(p.getName())){
				plugin.kits.isInCupidKit.remove(p.getName());
			}

			double perKill = plugin.getConfig().getDouble("Money-To-Give-Per-Kill");
			EconomyResponse r = plugin.econ.depositPlayer(k.getName(), perKill);
			if(r.transactionSuccess() && k!=null)Functions.tell(k, Functions.format(plugin.getConfig().getString("messages.killer-message-per-kill").replace("%p", p.getName()).replace("%amount%", "" + r.amount)));

			streakUtils.setStreaks(p, 0);
			streakUtils.setStreaks(k, streakUtils.getStreaks(k) + 1);

			Functions.tell(k, Functions.format(plugin.getConfig().getString("messages.killer-message-on-streak").replace("%streak%", "" + streakUtils.getStreaks(p))));

			if(plugin.getConfig().getBoolean("allow.dropsOnDeath") == false){
				e.setDroppedExp(0);
				e.getDrops().clear();
			}

		}
		if(e.getEntity() instanceof Horse){
			Horse horse = (Horse)e.getEntity();
			e.getDrops().clear();
			horse.getWorld().createExplosion(horse.getLocation().getX(), horse.getLocation().getY(), horse.getLocation().getZ(), 0, false, false);
		}
	}

	private void checkStreak(Player p){
		PlayerInventory pi = p.getInventory();
		switch(streakUtils.getStreaks(p)){
		case 3:
			Functions.givePot(p, PotionEffectType.SPEED, 600, 0);
			p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
			Bukkit.broadcastMessage(Functions.format(plugin.getConfig().getString("messages.broadcast-3-killstreak").replace("%p", p.getName())));
			EconomyResponse rf = plugin.econ.depositPlayer(p.getName(), plugin.getConfig().getInt("Money-To-Give-On-3-KillStreak"));
			if (rf.transactionSuccess())Functions.tell(p, Functions.format(plugin.getConfig().getString("message.killer-message-on-3-killstreak").replace("%amount%", "" + rf.amount).replace("%p", p.getName())));
		case 5:
			Functions.givePot(p, PotionEffectType.INCREASE_DAMAGE, 600, 0);
			p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
			Bukkit.broadcastMessage(Functions.format(plugin.getConfig().getString("messages.broadcast-5-killstreak").replace("%p", p.getName())));
			EconomyResponse ra = plugin.econ.depositPlayer(p.getName(), plugin.getConfig().getInt("Money-To-Give-On-5-KillStreak"));
			if (ra.transactionSuccess())Functions.tell(p, Functions.format(plugin.getConfig().getString("message.killer-message-on-5-killstreak").replace("%amount%", "" + ra.amount).replace("%p", p.getName())));
		case 7:
			ItemStack a = new ItemStack(Material.SNOW_BALL, 3);
			Functions.name(a, "§cGrenade");
			pi.addItem(a);
			p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
			Functions.tell(p, "§aEnjoy your rewards, use them wisely, though.");
			Bukkit.broadcastMessage(Functions.format(plugin.getConfig().getString("messages.broadcast-7-killstreak").replace("%p", p.getName())));
			EconomyResponse rb = plugin.econ.depositPlayer(p.getName(), plugin.getConfig().getInt("Money-To-Give-On-7-KillStreak"));
			if (rb.transactionSuccess())Functions.tell(p, Functions.format(plugin.getConfig().getString("message.killer-message-on-7-killstreak").replace("%amount%", "" + rb.amount).replace("%p", p.getName())));
		case 10:
			ItemStack helm = pi.getHelmet();
			ItemStack chest = pi.getChestplate();
			ItemStack legs = pi.getLeggings();
			ItemStack boots = pi.getBoots();
			helm.setDurability(helm.getType().getMaxDurability());
			chest.setDurability(chest.getType().getMaxDurability());
			legs.setDurability(legs.getType().getMaxDurability());
			boots.setDurability(boots.getType().getMaxDurability());
			pi.setArmorContents(new ItemStack[] {helm, chest, legs, boots});

			Functions.tell(p, plugin.getConfig().getString("messages.reward-message-10-streak"));

			Functions.givePot(p, PotionEffectType.FAST_DIGGING, 600, 0);
			p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);

			Bukkit.broadcastMessage(Functions.format(plugin.getConfig().getString("messages.broadcast-10-killstreak").replace("%p", p.getName())));

			EconomyResponse rc = plugin.econ.depositPlayer(p.getName(), plugin.getConfig().getInt("Money-To-Give-On-10-KillStreak"));
			if (rc.transactionSuccess())Functions.tell(p, Functions.format(plugin.getConfig().getString("message.killer-message-on-10-killstreak").replace("%amount%", "" + rc.amount).replace("%p", p.getName())));

		case 15:
			Functions.givePot(p, PotionEffectType.DAMAGE_RESISTANCE, 20, 0);
			Functions.givePot(p, PotionEffectType.FAST_DIGGING, 20, 0);
			Functions.givePot(p, PotionEffectType.FIRE_RESISTANCE, 20, 0);
			Functions.givePot(p, PotionEffectType.HEAL, 20, 0);
			Functions.givePot(p, PotionEffectType.INCREASE_DAMAGE, 20, 0);
			Functions.givePot(p, PotionEffectType.INVISIBILITY, 20, 0);
			Functions.givePot(p, PotionEffectType.JUMP, 20, 0);
			Functions.givePot(p, PotionEffectType.NIGHT_VISION, 20, 0);
			Functions.givePot(p, PotionEffectType.REGENERATION, 20, 0);
			Functions.givePot(p, PotionEffectType.SPEED, 20, 0);
			Functions.givePot(p, PotionEffectType.WATER_BREATHING, 20, 0);
			p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);

			Bukkit.broadcastMessage(Functions.format(plugin.getConfig().getString("messages.broadcast-15-killstreak").replace("%p", p.getName())));

			EconomyResponse rd = plugin.econ.depositPlayer(p.getName(), plugin.getConfig().getInt("Money-To-Give-On-15-KillStreak"));
			if (rd.transactionSuccess())Functions.tell(p, Functions.format(plugin.getConfig().getString("message.killer-message-on-15-killstreak").replace("%amount%", "" + rd.amount).replace("%p", p.getName())));
			break;
		case 20:
			if(plugin.kits.hasAKit(p)){
				plugin.kits.takeFromKitTracker(p);

				pi.addItem(new ItemStack(Material.EGG, 5));
				Functions.tell(p, "§6You have been given some §aFire Grenades§6!");

				Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.new-kit-permission")));

				p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);

				Bukkit.broadcastMessage(Functions.format(plugin.getConfig().getString("messages.broadcast-20-killstreak").replace("%p", p.getName())));

				EconomyResponse re = plugin.econ.depositPlayer(p.getName(), plugin.getConfig().getInt("Money-To-Give-On-20-KillStreak"));
				if (re.transactionSuccess())Functions.tell(p, Functions.format(plugin.getConfig().getString("message.killer-message-on-20-killstreak").replace("%amount%", "" + re.amount).replace("%p", p.getName())));
			}
		}
		if(streakUtils.getStreaks(p) > 20){
			Functions.givePot(p, PotionEffectType.DAMAGE_RESISTANCE, 20, 0);
			Functions.givePot(p, PotionEffectType.FAST_DIGGING, 20, 0);
			Functions.givePot(p, PotionEffectType.FIRE_RESISTANCE, 20, 0);
			Functions.givePot(p, PotionEffectType.HEAL, 20, 0);
			Functions.givePot(p, PotionEffectType.INCREASE_DAMAGE, 20, 0);
			Functions.givePot(p, PotionEffectType.INVISIBILITY, 20, 0);
			Functions.givePot(p, PotionEffectType.JUMP, 20, 0);
			Functions.givePot(p, PotionEffectType.NIGHT_VISION, 20, 0);
			Functions.givePot(p, PotionEffectType.REGENERATION, 20, 0);
			Functions.givePot(p, PotionEffectType.SPEED, 20, 0);
			Functions.givePot(p, PotionEffectType.WATER_BREATHING, 20, 0);
			p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
		}
	}

	@EventHandler
	public void PlayerJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		FileConfiguration a = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "stats.yml"));
		if(!a.contains(p.getName() + ".kills")){
			a.set(p.getName() + ".kills", "0");
			a.set(p.getName() + ".deaths", "0");
			a.set(p.getName() + ".KDR", "0");
			try {
				a.save(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "stats.yml"));
			} catch (IOException e1){
				e1.printStackTrace();
			}
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
		streakUtils.setStreaks(player, 0);
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
						Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.cupid-feather-use")));
					}
					new BukkitRunnable(){
						@Override
						public void run(){
							if(p.getGameMode() == GameMode.CREATIVE){
								p.setAllowFlight(true);
							} else {
								p.setAllowFlight(false);
								Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.cupid-feather-end")));
							}
						}
					}.runTaskLater(plugin, plugin.getConfig().getInt("Fly-Length-Cupid-Kit")*20);
					new BukkitRunnable(){
						@Override
						public void run(){
							cooldown.remove(p.getName());
							Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.cupid-feather-canUse")));
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

	@EventHandler
	public void onAssassinDisappear(PlayerMoveEvent e){
		Player p = e.getPlayer();
		for(Player ps : Bukkit.getOnlinePlayers()){
			if(plugin.kit.assassin.contains(p.getName())){
				if (!p.canSee(ps))
				{
					p.hidePlayer(ps);
				}
			}
		}
		for(Entity ents : p.getNearbyEntities(13, 13, 13)){
			if(ents instanceof LivingEntity && ents instanceof Player){
				Player ps = (Player)ents;

				if (!p.canSee(ps))
				{
					p.hidePlayer(ps);
				}
			}
		}
	}

}