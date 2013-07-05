package me.kreashenz.kitpvp;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import me.kreashenz.kitpvp.metrics.Metrics;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class KitPvP extends JavaPlugin {

	public KitManager kits;
	public Economy econ = null;
	private Kits kit;

	@Override
	public void onEnable() {

		kits = new KitManager(this);
		kit = new Kits(this);

		getServer().getPluginManager().registerEvents(new Events(this), this);
		getServer().getPluginManager().registerEvents(new Signs(this), this);

		saveDefaultConfig();

		getCommand("stats").setExecutor(kit);
		getCommand("pyro").setExecutor(kit);
		getCommand("tank").setExecutor(kit);
		getCommand("pvp").setExecutor(kit);
		getCommand("archer").setExecutor(kit);
		getCommand("admin").setExecutor(kit);
		getCommand("medic").setExecutor(kit);
		getCommand("cupid").setExecutor(kit);
		getCommand("refill").setExecutor(kit);

		setupVault(getServer().getPluginManager());

		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(!getConfig().contains("MustHaveKitToThrowTNT")){
			File f = new File(getConfig().getName());
			f.delete();

			saveDefaultConfig();
		}
	}

	private void setupVault(PluginManager pm) {
		Plugin vault =  pm.getPlugin("Vault");
		if (vault != null && vault instanceof net.milkbowl.vault.Vault) {
			getLogger().info("Loaded Vault v" + vault.getDescription().getVersion());
			if (!setupEconomy()) {
				getLogger().warning("No economy plugin installed.");
			} else {
				getLogger().warning("Vault not loaded, please check your plugins folder or console.");
			}
		}
	}

	private boolean setupEconomy(){
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null) {
			econ = economyProvider.getProvider();
		} else {
			getLogger().warning("Vault is not installed, can't give money for killstreaks. Disabling.");
			getServer().getPluginManager().disablePlugin(this);
			return false;
		}
		return (econ != null);
	}

	public static void log(Level lvl, String msg){
		Bukkit.getLogger().log(lvl, msg);
	}

	public int getStreaks(Player p){
		return getConfig().getInt("killstreaks." + p.getName());
	}

	public void setStreaks(Player p, int Streaks){
		getConfig().set("killstreaks." + p.getName(), Streaks);
		saveConfig();
	}

	public void clearStreaks(Player p){
		getConfig().set("killstreaks." + p.getName(), null);
		saveConfig();
	}

	public void setKills(Player p, int kills){
		getConfig().set(p.getName() + ".kills", kills);
		saveConfig();
	}

	public void setDeaths(Player p, int deaths){
		getConfig().set(p.getName() + ".deaths", deaths);
		saveConfig();
	}

	public int getDeaths(Player p){
		return getConfig().getInt(p.getName() + ".deaths");
	}

	public int getKills(Player p){
		return getConfig().getInt(p.getName() + ".kills");
	}
}