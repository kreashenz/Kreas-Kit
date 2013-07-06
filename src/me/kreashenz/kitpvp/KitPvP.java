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
import net.milkbowl.vault.economy.Economy;

import org.bukkit.event.Listener;
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

		listener(new Events(this));
		listener(new Signs(this));

		saveDefaultConfig();

		command("stats");
		command("pyro");
		command("tank");
		command("pvp");
		command("archer");
		command("medic");
		command("cupid");
		command("refill");

		setupVault(getServer().getPluginManager());

		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		checkForOldConfig();
	}

	private void checkForOldConfig(){
		if(getConfig().getDouble("version") != Double.valueOf(getDescription().getVersion())){

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

				System.out.print(old.getAbsolutePath());
				System.out.print(mew.getAbsolutePath());

				mew.delete();

				saveDefaultConfig();

			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	private void command(String cmd){
		getCommand(cmd).setExecutor(kit);
	}

	private void listener(Listener listener){
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(listener, this);
	}

	private void setupVault(PluginManager pm) {
		Plugin vault =  pm.getPlugin("Vault");
		if (vault != null && vault instanceof net.milkbowl.vault.Vault) {
			Functions.log(Level.INFO, "Loaded Vault v" + vault.getDescription().getVersion());
			if (!setupEconomy()) {
				Functions.log(Level.WARNING, "No economy plugin installed.");
			} else {
				Functions.log(Level.WARNING, "Vault not loaded, please check your plugins folder or console.");
			}
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
}