package me.kreashenz.kitpvp;

import java.text.DecimalFormat;

import me.kreashenz.kitpvp.utils.Functions;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Commands implements CommandExecutor {

	private KitPvP plugin;
	private Kits kits;

	public Commands(KitPvP plugin){
		this.plugin = plugin;
		this.kits = plugin.kit;
	}

	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args){
		if(s instanceof Player){
			Player p = (Player)s;
			if(cmd.getName().equalsIgnoreCase("archer")){
				if(p.hasPermission("kitpvp.archer")){
					if(!plugin.kits.hasAKit(p)){
						plugin.kits.whoHasAKit.add(p.getName());
						kits.ArcherKit(p);
					} else {
						Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.must-die-before-new-kit")));
					}
				} else Functions.noPerm(p);
			}
			if(cmd.getName().equalsIgnoreCase("pvp")){
				if(p.hasPermission("kitpvp.pvp")){
					if(!plugin.kits.hasAKit(p)){
						plugin.kits.whoHasAKit.add(p.getName());
						kits.PvPKit(p);
					} else {
						Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.must-die-before-new-kit")));
					}
				} else Functions.noPerm(p);
			}
			if(cmd.getName().equalsIgnoreCase("tank")){
				if(p.hasPermission("kitpvp.tank")){
					if(!plugin.kits.hasAKit(p)){
						plugin.kits.whoHasAKit.add(p.getName());
						kits.TankKit(p);
					} else {
						Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.must-die-before-new-kit")));
					}
				} else Functions.noPerm(p);
			}
			if(cmd.getName().equalsIgnoreCase("pyro")){
				if(p.hasPermission("kitpvp.pyro")){
					if(!plugin.kits.hasAKit(p)){
						plugin.kits.whoHasAKit.add(p.getName());
						kits.PyroKit(p);
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
						Functions.tell(p, "�cYou have no empty bowls!");
						return true; 
					}
					return true;
				} else Functions.noPerm(p);
			}
			if(cmd.getName().equalsIgnoreCase("medic")){
				if(p.hasPermission("kitpvp.medic")){
					if(!plugin.kits.hasAKit(p)){
						plugin.kits.whoHasAKit.add(p.getName());
						kits.MedicKit(p);
					} else {
						Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.must-die-before-new-kit")));
					}
				} else Functions.noPerm(p);
			}
			if(cmd.getName().equalsIgnoreCase("cupid")){
				if(p.hasPermission("kitpvp.cupid")){
					if(!plugin.kits.hasAKit(p)){
						plugin.kits.whoHasAKit.add(p.getName());
						kits.CupidKit(p);
					} else {
						Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.must-die-before-new-kit")));
					}
					if(!plugin.kits.isInCupidKit.contains(p.getName())){
						plugin.kits.isInCupidKit.add(p.getName());
					}
				} else Functions.noPerm(p);
			}
			if(cmd.getName().equalsIgnoreCase("assassin")){
				if(p.hasPermission("kitpvp.assassin")){
					if(!plugin.kits.hasAKit(p)){
						plugin.kits.whoHasAKit.add(p.getName());
						kits.assassin.add(p.getName());
						kits.AssassinKit(p);
					} else {
						Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.must-die-before-new-kit")));
					}
				} else Functions.noPerm(p);
			}
			if(cmd.getName().equalsIgnoreCase("knight")){
				if(p.hasPermission("kitpvp.knight")){
					if(!plugin.kits.hasAKit(p)){
						plugin.kits.whoHasAKit.add(p.getName());
						kits.KnightKit(p);
					} else {
						Functions.tell(p, Functions.format(plugin.getConfig().getString("messages.must-die-before-new-kit")));
					}
				} else Functions.noPerm(p);
			}
			if(cmd.getName().equalsIgnoreCase("kkit")){
				if(p.hasPermission("kitpvp.control")){
					if(args.length != 1){
						Functions.tell(p, "�cInvalid usage. �f/kkit <reload | save>");
					} else {
						if(args[0].equalsIgnoreCase("save")){
							plugin.saveConfig();
						} else if(args[0].equalsIgnoreCase("reload")){
							plugin.reloadConfig();
						} else {
							Functions.tell(p, "�cInvalid usage. �f/kkit <reload | save>");
						}
					}
				} else Functions.noPerm(p);
			}
			if(cmd.getName().equalsIgnoreCase("kitlist")){
				if(p.hasPermission("kitpvp.list")){
					String str = "";
					for(String st : plugin.getConfig().getConfigurationSection("Kits").getKeys(false)){
						if(p.hasPermission("kitpvp." + st)){
							st = "�a" + st + "�f";
						} else {
							st = "�c" + st + "�f";
						}
						str = str + st + ", ";
					}
					str = str.substring(0, str.length()-2);
					Functions.tell(p, "�8You can use the �agreen �8.");
					Functions.tell(p, str);
				} else Functions.noPerm(p);
			}
			if(cmd.getName().equalsIgnoreCase("stats")){
				DecimalFormat d = new DecimalFormat("##.##");
				if(p.hasPermission("kitpvp.stats")){
					FileConfiguration file = plugin.getConfig();
					if(args.length == 0){
						Functions.tell(p, "�7+�c--------------------------------------�c+");
						Functions.tell(p, "�7|| �6KDR �1: �a" + d.format(file.getDouble(p.getName() + ".kills") / file.getDouble(p.getName() + ".deaths")));
						Functions.tell(p, "�7|| �6Kills �1: �a" + file.getInt(p.getName() + ".kills"));
						Functions.tell(p, "�7|| �6Deaths �1: �a" + file.getInt(p.getName() + ".deaths"));
						Functions.tell(p, "�7+�c--------------------------------------�c+");
						if(!plugin.sb.hasBoard.contains(p.getName())){
							plugin.sb.setBoard(p);
							Functions.tell(p, "�aShowing your stats. Board will remove in 10 seconds.");
						} else Functions.tell(p, "�cYou already have a scoreboard up!");
					} else {
						Player t = Bukkit.getPlayer(args[0]);
						if(t.isOnline() && t != null){
							Functions.tell(p, "�7+�c--------------------------------------�c+");
							Functions.tell(p, "�7|| �6KDR �1: �a" + d.format(file.getDouble(t.getName() + ".kills") / file.getDouble(t.getName() + ".deaths")));
							Functions.tell(p, "�7|| �6Kills �1: �a" + file.getInt(t.getName() + ".kills"));
							Functions.tell(p, "�7|| �6Deaths �1: �a" + file.getInt(t.getName() + ".deaths"));
							Functions.tell(p, "�7+�c--------------------------------------�c+");
						} else Functions.tell(p, "�cThat player cannot be found, please try again.");
					}
				} else Functions.noPerm(p);
			}
		} else s.sendMessage("�cYou must be a player to use these commands.");
		return true;
	}

}