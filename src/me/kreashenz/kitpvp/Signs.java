package me.kreashenz.kitpvp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Signs implements Listener {

	private KitPvP plugin;
	private Kits kits;

	public Signs(KitPvP plugin) {
		this.plugin = plugin;
		this.kits = plugin.kit;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerClickSign(PlayerInteractEvent e) {
		List<String> allkits = new ArrayList<String>(plugin.getConfig().getConfigurationSection("Kits").getKeys(false));
		Player p = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null)
			if (e.getClickedBlock().getType() == Material.WALL_SIGN
			|| e.getClickedBlock().getType() == Material.SIGN_POST) {
				Sign s = (Sign) e.getClickedBlock().getState();
				String[] lines = s.getLines();
				if (lines.length > 1 && lines[0].equalsIgnoreCase("§1[Kreas-Kit]")) {
					if (p.hasPermission("kitpvp.signs")) {
						if (lines[1].equalsIgnoreCase("Full") || lines[1].equalsIgnoreCase("Soups")
								|| (lines[1].equalsIgnoreCase("FullSoup"))
								|| lines[1].equalsIgnoreCase("Refill")) {
							if (!p.hasPermission("kitpvp.refill")) {
								p.sendMessage("§cYou do not have permission to refill.");
								return;
							}
							for(int i = 1; i <= 1; i++)
							p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP, 3));
						}
						if(lines[1].equalsIgnoreCase("Pyro")){
							if(!p.hasPermission("kitpvp.pyro")){
								p.sendMessage("§cYou do not have permission to use the Pyro kit.");
								return;
							}
							p.getInventory().clear();
							kits.PyroKit(p);
							p.updateInventory();
						}
						if(lines[1].equalsIgnoreCase("PvP")){
							if(!p.hasPermission("kitpvp.pvp")){
								p.sendMessage("§cYou do not have permission to use the PvP kit.");
								return;
							}
							p.getInventory().clear();
							kits.PvPKit(p);
							p.updateInventory();
						}
						if(lines[1].equalsIgnoreCase("Tank")){
							if(!p.hasPermission("kitpvp.tank")){
								p.sendMessage("§cYou do not have permission to use the Tank kit.");
								return;
							}
							p.getInventory().clear();
							kits.TankKit(p);
							p.updateInventory();
						}
						if(lines[1].equalsIgnoreCase("medic")){
							if(!p.hasPermission("kitpvp.medic")){
								p.sendMessage("§cYou do not have permission to use the Medic kit.");
								return;
							}
							p.getInventory().clear();
							kits.MedicKit(p);
							p.updateInventory();
						}
						if(lines[1].equalsIgnoreCase("archer")){
							if(!p.hasPermission("kitpvp.archer")){
								p.sendMessage("§cYou do not have permission to use the Archer kit.");
								return;
							}
							p.getInventory().clear();
							kits.ArcherKit(p);
							p.updateInventory();
						}
						if(allkits.contains(lines[1])){
							if(!p.hasPermission("kitpvp."+lines[1].toLowerCase()+".use")){
								p.sendMessage("§cYou do not have permission to use custom kits.");
								return;
							}
							p.getInventory().clear();
							plugin.kits.giveKit(p, lines[1]);
							p.updateInventory();
						}
						if(lines[1].equalsIgnoreCase("cupid")){
							if(!p.hasPermission("kitpvp.cupid")){
								p.sendMessage("§cYou do not have permission to use the Cupid kit.");
								return;
							}
							p.getInventory().clear();
							kits.CupidKit(p);
							p.updateInventory();
						}
					}
				} else p.sendMessage("§cYou don't have permission to use signs.");
			}
	}

	@EventHandler
	public void onSignEdit(SignChangeEvent e) {
		List<String> allkits = new ArrayList<String>(plugin.getConfig().getConfigurationSection("Kits").getKeys(false));
		String[] lines = e.getLines();
		Player p = e.getPlayer();
		boolean breakSign = false;
		if (lines.length > 1 && lines[0].equalsIgnoreCase("[KreasKit]")) {
			if (p.hasPermission("kitpvp.signs.refill")) {
				if (lines[1].equalsIgnoreCase("Full") || lines[1].equalsIgnoreCase("Soups")
						|| lines[1].equalsIgnoreCase("FullSoup")
						|| lines[1].equalsIgnoreCase("Refill")) {
					e.setLine(0, "§1[Kreas-Kit]");
					e.setLine(1, "Soups");
					p.sendMessage("§aSuccessfully created sign!");
					e.getBlock().getState().update();
				}
			} else breakSign = true;
			if (p.hasPermission("kitpvp.signs.kits.pyro")){
				if(lines[1].equalsIgnoreCase("Pyro")){
					e.setLine(0, "§1[Kreas-Kit]");
					e.setLine(1, "Pyro");
					p.sendMessage("§aSuccessfully created sign!");
					e.getBlock().getState().update();
				}
			} else breakSign = true;
			if(p.hasPermission("kitpvp.signs.kits.archer")){
				if(lines[1].equalsIgnoreCase("Archer")){
					e.setLine(0, "§1[Kreas-Kit]");
					e.setLine(1, "Archer");
					p.sendMessage("§aSuccessfully created sign!");
					e.getBlock().getState().update();
				}
			} else breakSign = true;
			if(p.hasPermission("kitpvp.signs.kits.pvp")){
				if(lines[1].equalsIgnoreCase("PvP")){
					e.setLine(0, "§1[Kreas-Kit]");
					e.setLine(1, "PvP");
					p.sendMessage("§aSuccessfully created sign!");
					e.getBlock().getState().update();
				}
			} else breakSign = true;
			if(p.hasPermission("kitpvp.signs.kits.tank")){
				if(lines[1].equalsIgnoreCase("Tank")){
					e.setLine(0, "§1[Kreas-Kit]");
					e.setLine(1, "Tank");
					p.sendMessage("§aSuccessfully created sign!");
					e.getBlock().getState().update();
				}
			} else breakSign = true;
			if(p.hasPermission("kitpvp.signs.kits.medic")){
				if(lines[1].equalsIgnoreCase("Medic")){
					e.setLine(0, "§1[Kreas-Kit]");
					e.setLine(1, "Medic");
					p.sendMessage("§aSuccessfully created sign!");
					e.getBlock().getState().update();
				}
			} else breakSign = true;
			if(p.hasPermission("kitpvp.signs.kits.cupid")){
				if(lines[1].equalsIgnoreCase("Cupid")){
					e.setLine(0, "§1[Kreas-Kit]");
					e.setLine(1, "Cupid");
					p.sendMessage("§aSuccessfully created sign!");
					e.getBlock().getState().update();
				}
			} else breakSign = true;
			if(p.hasPermission("kitpvp.signs.kits.custom")){
				if(allkits.contains(lines[1])){
					e.setLine(0, "§1[Kreas-Kit]");
					e.setLine(1, lines[1]);
					e.getBlock().getState().update();
				}
			} else breakSign = true;
			if(!(lines[1].equalsIgnoreCase("archer") || lines[1].equalsIgnoreCase("tank")
					|| lines[1].equalsIgnoreCase("pvp") || lines[1].equalsIgnoreCase("pyro")
					|| lines[1].equalsIgnoreCase("medic") || lines[1].equalsIgnoreCase("cupid")
					|| allkits.contains(lines[1]) || lines[1].equalsIgnoreCase("Full")
					|| lines[1].equalsIgnoreCase("Soups") || lines[1].equalsIgnoreCase("FullSoup")
					|| lines[1].equalsIgnoreCase("Refill"))){
				breakSign = true;
				p.sendMessage("§cI was unabled to find that sign, please try again!");
			}
			if (breakSign) {
				e.getBlock().breakNaturally();
				p.sendMessage("§cYou can't create those signs!");
			}
		}
	}
}