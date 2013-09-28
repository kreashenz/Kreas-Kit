package me.kreashenz.kitpvp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import me.kreashenz.kitpvp.metrics.Metrics;
import me.kreashenz.kitpvp.utils.Functions;
import me.kreashenz.kitpvp.utils.KillstreakUtils;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class KitPvP extends JavaPlugin {

	private static KitPvP instance;

	public Economy econ = null;

	public KitManager kits;
	public Kits kit;
	public KillstreakUtils streakUtils;
	public SBManager sb;

	@Override
	public void onEnable() {

		instance = this;

		String path = getDataFolder().getAbsolutePath() + File.separator + "stats.yml";

		File file = new File(path);

		if(!new File(getDataFolder().getAbsolutePath() + File.separator + "README-Update.txt").exists()){
			saveResource("README-Update.txt", false);
		}

		if(!(file.exists())){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				Functions.log(Level.SEVERE, "Couldn't create the stats.yml file.");
			}
		}

		saveDefaultConfig();

		streakUtils = new KillstreakUtils(this);
		kits = new KitManager(this);
		kit = new Kits();
		sb = new SBManager(this);

		listener(new Events(this));
		listener(new Signs(this));

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

		checkForOldConfig();

		runStatsSaveTimer();

		for (String kits : getConfig().getStringList("Kits"))getLogger().info("Successfully registered kit: " + kits);
	}

	private void checkForOldConfig(){
		if(getConfig().getDouble("version") != Double.valueOf(getDescription().getVersion()) || (!(getConfig().contains("version")))){

			try {
				File old = new File(getDataFolder().getAbsolutePath() + File.separator + "config.yml.old");
				File mew = new File(getDataFolder().getAbsolutePath() + File.separator + "config.yml");

				InputStream in = new FileInputStream(mew);
				OutputStream out = new FileOutputStream(old);

				byte[] buff = new byte[1024];

				int length;

				while((length = in.read(buff)) > 0)out.write(buff, 0, length);

				in.close();
				out.close();

				mew.delete();

				saveDefaultConfig();

			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	private void command(String cmd){
		getCommand(cmd).setExecutor(new Commands(this));
	}

	private void listener(Listener listener){
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(listener, this);
	}

	private void setupVault() {
		Plugin vault =  getServer().getPluginManager().getPlugin("Vault");
		if (vault != null && vault instanceof net.milkbowl.vault.Vault) {
			Functions.log(Level.INFO, "Loaded Vault v" + vault.getDescription().getVersion());
			if (!setupEconomy()) {
				Functions.log(Level.WARNING, "No economy plugin installed.");
			} else {
				Functions.log(Level.INFO, "Found economy.");
			}
		} else {
			Functions.log(Level.WARNING, "Vault not loaded, please check your plugins folder or console.");
		}
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

	public static KitPvP getInstance(){
		return instance;
	}

}