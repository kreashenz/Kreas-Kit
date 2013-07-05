package me.kreashenz.kitpvp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class SBManager {

	private KitPvP plugin;

	public List<String> hasBoard = new ArrayList<String>();

	public SBManager(){
		plugin = new KitPvP();
	}

	public void setBoard(Player p){
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective objective = board.registerNewObjective("dummy", "test");
		objective.setDisplayName("§bYour stats!");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		Score a = objective.getScore(Bukkit.getOfflinePlayer("§aKillstreak"));
		a.setScore(plugin.getStreaks(p));

		Score b = objective.getScore(Bukkit.getOfflinePlayer("§aKills"));
		b.setScore(plugin.getKills(p));

		Score c = objective.getScore(Bukkit.getOfflinePlayer("§aDeaths"));
		c.setScore(plugin.getDeaths(p));

		p.setScoreboard(board);

	}

	public void removeBoard(Player p){
		if(!p.isOnline() || p == null || !hasBoard.contains(p.getName())){
			return;
		} else {
			hasBoard.remove(p.getName());
		}
	}

	public void setTempBoard(final Player p){
		if(!p.isOnline() || p == null)return;
		new BukkitRunnable(){
			@Override
			public void run(){
				if(hasBoard.contains(p.getName())){
					return;
				} else {
					setBoard(p);
					p.sendMessage("§aYou've set your scoreboard. Removing in §c" + plugin.getConfig().getInt("Scoreboard-Show-Time") + "§a seconds!");
				}
			}
		}.runTaskTimer(plugin, 20L, plugin.getConfig().getInt("Scoreboard-Show-Time"));
	}

}