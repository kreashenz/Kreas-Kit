package me.kreashenz.kitpvp;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import me.kreashenz.kitpvp.utils.Functions;
import me.kreashenz.kitpvp.utils.InventoryStuff;
import me.kreashenz.kitpvp.utils.PManager;

import org.apache.commons.lang.StringUtils;
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

	private String prefix = "§1[Kreas-Kits]";

	private Set<String> allConfKits;
	private Set<String> allFolderKits;

	public Signs() {
		allConfKits = new HashSet<String>(KitPvP.getInstance().getConfig().getConfigurationSection("Kits").getKeys(false));
		for(String str : KitPvP.getInstance().getConfig().getConfigurationSection("Kits").getKeys(false))allConfKits.add(str);
		for(File f : new File(KitPvP.getInstance().getDataFolder() + File.separator + "kits").listFiles()){
			if(!(f.getName().startsWith("."))){
				String s = f.getName().replace(".yml", "");
				allFolderKits.add(s);
			}
		}
	}

	@EventHandler
	public void onPlayerClickSign(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		PManager pm = PManager.getPManager(p);
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null){
			if (e.getClickedBlock().getState() instanceof Sign) {
				Sign s = (Sign) e.getClickedBlock().getState();
				String[] lines = s.getLines();
				if (lines[0].equalsIgnoreCase(prefix)) {
					if (p.hasPermission("kitpvp.signs")) {
						if (lines[1].equalsIgnoreCase("Full") || lines[1].equalsIgnoreCase("Soups")
								|| (lines[1].equalsIgnoreCase("FullSoup"))
								|| lines[1].equalsIgnoreCase("Refill")) {
							if (!p.hasPermission("kitpvp.refill")) {
								Functions.tell(p, "§cYou do not have permission to refill.");
								return;
							}
							for(int i = 1; i <= 1; i++)
								p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP, 3));
						}
						else if(lines[1].equalsIgnoreCase("Pyro")){
							if(p.hasPermission("kitpvp.pyro")){
								pm.giveKit("pyro");
							} else Functions.noPerm(p);
						}
						else if(lines[1].equalsIgnoreCase("PvP")){
							if(p.hasPermission("kitpvp.pvp")){
								pm.giveKit("pvp");
							} else Functions.noPerm(p);
						}
						else if(lines[1].equalsIgnoreCase("Tank")){
							if(p.hasPermission("kitpvp.tank")){
								pm.giveKit("tank");
							} else Functions.noPerm(p);
						}
						else if(lines[1].equalsIgnoreCase("medic")){
							if(p.hasPermission("kitpvp.medic")){
								pm.giveKit("medic");
							} else Functions.noPerm(p);
						}
						else if(lines[1].equalsIgnoreCase("archer")){
							if(p.hasPermission("kitpvp.archer")){
								pm.giveKit("archer");
							} else Functions.noPerm(p);
						}
						else if(lines[1].equalsIgnoreCase("cupid")){
							if(p.hasPermission("kitpvp.cupid")){
								pm.giveKit("cupid");
							} else Functions.noPerm(p);
						}
						else if(lines[1].equalsIgnoreCase("assassin")){
							if(p.hasPermission("kitpvp.assassin")){
								pm.giveKit("assassin");
							} else Functions.noPerm(p);
						}
						else if(allConfKits.contains(lines[1])){
							if(p.hasPermission("kitpvp." + lines[1].toLowerCase())){
								pm.giveKit(lines[1].toLowerCase());
							} else Functions.noPerm(p);
						}
						else if(allFolderKits.contains(lines[1])){
							if(p.hasPermission("kitpvp." + lines[1].toLowerCase())){
								InventoryStuff.setStuff(p, lines[1]);
							} else Functions.noPerm(p);
						}
					} else Functions.tell(p, "§cYou don't have permission to use signs.");
				}
			}
		}
	}

	@EventHandler
	public void onSignEdit(SignChangeEvent e) {
		String[] lines = e.getLines();
		Player p = e.getPlayer();
		boolean breakSign = false;
		if (lines.length > 1 && lines[0].equalsIgnoreCase("[KreasKit]")) {
			if (p.hasPermission("kitpvp.signs.refill")) {
				if (lines[1].equalsIgnoreCase("Full") || lines[1].equalsIgnoreCase("Soups")
						|| lines[1].equalsIgnoreCase("FullSoup")
						|| lines[1].equalsIgnoreCase("Refill")) {
					e.setLine(0, prefix);
					e.setLine(1, "Soups");
					Functions.tell(p, "§aSuccessfully created sign!");
					e.getBlock().getState().update();
				}
			} else breakSign = true;
			if (p.hasPermission("kitpvp.signs.kits.pyro")){
				if(lines[1].equalsIgnoreCase("Pyro")){
					e.setLine(0, prefix);
					e.setLine(1, "Pyro");
					Functions.tell(p, "§aSuccessfully created sign!");
					e.getBlock().getState().update();
				}
			} else breakSign = true;
			if(p.hasPermission("kitpvp.signs.kits.archer")){
				if(lines[1].equalsIgnoreCase("Archer")){
					e.setLine(0, prefix);
					e.setLine(1, "Archer");
					Functions.tell(p, "§aSuccessfully created sign!");
					e.getBlock().getState().update();
				}
			} else breakSign = true;
			if(p.hasPermission("kitpvp.signs.kits.pvp")){
				if(lines[1].equalsIgnoreCase("PvP")){
					e.setLine(0, prefix);
					e.setLine(1, "PvP");
					Functions.tell(p, "§aSuccessfully created sign!");
					e.getBlock().getState().update();
				}
			} else breakSign = true;
			if(p.hasPermission("kitpvp.signs.kits.tank")){
				if(lines[1].equalsIgnoreCase("Tank")){
					e.setLine(0, prefix);
					e.setLine(1, "Tank");
					Functions.tell(p, "§aSuccessfully created sign!");
					e.getBlock().getState().update();
				}
			} else breakSign = true;
			if(p.hasPermission("kitpvp.signs.kits.medic")){
				if(lines[1].equalsIgnoreCase("Medic")){
					e.setLine(0, prefix);
					e.setLine(1, "Medic");
					Functions.tell(p, "§aSuccessfully created sign!");
					e.getBlock().getState().update();
				}
			} else breakSign = true;
			if(p.hasPermission("kitpvp.signs.kits.cupid")){
				if(lines[1].equalsIgnoreCase("Cupid")){
					e.setLine(0, prefix);
					e.setLine(1, "Cupid");
					Functions.tell(p, "§aSuccessfully created sign!");
					e.getBlock().getState().update();
				}
			} else breakSign = true;
			if(p.hasPermission("kitpvp.signs.kits.custom")){
				if(allConfKits.contains(lines[1])){
					e.setLine(0, prefix);
					e.setLine(1, StringUtils.capitalize(lines[1]));
					e.getBlock().getState().update();
				}
			} else breakSign = true;
			if(!(lines[1].equalsIgnoreCase("archer") || lines[1].equalsIgnoreCase("tank")
					|| lines[1].equalsIgnoreCase("pvp") || lines[1].equalsIgnoreCase("pyro")
					|| lines[1].equalsIgnoreCase("medic") || lines[1].equalsIgnoreCase("cupid")
					|| allConfKits.contains(lines[1]) || lines[1].equalsIgnoreCase("Full")
					|| lines[1].equalsIgnoreCase("Soups") || lines[1].equalsIgnoreCase("FullSoup")
					|| lines[1].equalsIgnoreCase("Refill"))){
				breakSign = true;
				Functions.tell(p, "§cI was unabled to find that sign, please try again!");
			}
			if (breakSign) {
				e.getBlock().breakNaturally();
				Functions.tell(p, "§cYou can't create those signs!");
			}
		}
	}
}