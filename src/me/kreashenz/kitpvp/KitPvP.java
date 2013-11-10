package me.kreashenz.kitpvp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import me.kreashenz.kitpvp.metrics.Metrics;
import me.kreashenz.kitpvp.utils.Functions;
import me.kreashenz.kitpvp.utils.InventoryStuff;
import me.kreashenz.kitpvp.utils.KillstreakUtils;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class KitPvP extends JavaPlugin {

	private static KitPvP instance;

	public Economy econ = null;

	public KillstreakUtils streakUtils;

	public List<String> assassinList = new ArrayList<String>();

	@Override
	public void onEnable() {
		instance = this;

		File file = new File(getDataFolder(), "stats.yml");

		if(!(file.exists())){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				Functions.log(Level.SEVERE, "Couldn't create the stats.yml file.");
			}
		}

		saveDefaultConfig();

		streakUtils = new KillstreakUtils();

		listener(new Events(this));
		listener(new Signs());

		command("stats");
		command("pyro");
		command("tank");
		command("pvp");
		command("archer");
		command("medic");
		command("cupid");
		command("refill");
		command("kkit");
		command("assassin");
		command("knight");
		command("kitlist");

		setupVault();

		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		runStatsSaveTimer();
		runAssassinTimer();

		for (String kits : getAllKits())getLogger().info("Successfully registered kit: " + kits);

	}

	private void command(String cmd){
		getCommand(cmd).setExecutor(new Commands(this));
	}

	private void listener(Listener listener){
		getServer().getPluginManager().registerEvents(listener, this);
	}

	private void setupVault() {
		Plugin vault =  getServer().getPluginManager().getPlugin("Vault");
		if (vault != null && vault instanceof net.milkbowl.vault.Vault) {
			Functions.log(Level.INFO, "Loaded Vault v" + vault.getDescription().getVersion());
			if (!setupEconomy()) {
				Functions.log(Level.WARNING, "No economy plugin installed.");
			} else Functions.log(Level.INFO, "Found economy.");
		} else Functions.log(Level.WARNING, "Vault not loaded, please check your plugins folder or console.");
	}

	private boolean setupEconomy(){
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null) {
			econ = economyProvider.getProvider();
		} else {
			Functions.log(Level.WARNING, "Vault is not installed, can't give money for killstreaks. Disabling.");
			getServer().getPluginManager().disablePlugin(this);
			return false;
		}
		return (econ != null);
	}

	private void runStatsSaveTimer(){
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new BukkitRunnable(){
			@Override
			public void run() {
				streakUtils.save();
			}
		}, 0L, getConfig().getInt("config-save-delay")*20);
	}
	
	private void runAssassinTimer(){
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new BukkitRunnable(){
			public void run(){
				for(String str : assassinList){
					Player p = Bukkit.getPlayerExact(str);
					if(p != null){
						for(Player ps : Bukkit.getOnlinePlayers()){
							int distance = (int)p.getLocation().distance(ps.getLocation());
							if(distance < 13){
								ps.showPlayer(p);
							} else {
								ps.hidePlayer(p);
							}
						}
					}
				}
			}
		}, 0L, 20L);
	}

	public static KitPvP getInstance(){
		return instance;
	}
	
	public List<String> getAllKits(){
		List<String> all = new ArrayList<String>();
		for(String str : getConfig().getConfigurationSection("Kits").getKeys(false)){
			all.add(str);
		}
		for(String str : InventoryStuff.getAllKits()){
			all.add(str);
		}
		return all;
	}

}