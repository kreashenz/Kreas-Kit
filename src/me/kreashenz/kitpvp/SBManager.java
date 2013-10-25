package me.kreashenz.kitpvp;

import java.util.ArrayList;
import java.util.List;

import me.kreashenz.kitpvp.utils.Functions;
import me.kreashenz.kitpvp.utils.KillstreakUtils;

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
	private KillstreakUtils streakUtils;

	protected List<String> hasBoard = new ArrayList<String>();

	public SBManager(KitPvP plugin){
		this.plugin = plugin;
		this.streakUtils = plugin.streakUtils;
	}

	public void setBoard(Player p){
		if(hasBoard.contains(p.getName())){
			Functions.tell(p, "§cYou already have a scoreboard up.");
		} else {
			hasBoard.add(p.getName());
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard board = manager.getNewScoreboard();
			Objective objective = board.registerNewObjective("dummy", "test");
			objective.setDisplayName("§bYour stats!");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);

			Score a = objective.getScore(Bukkit.getOfflinePlayer("§aKillstreak"));
			a.setScore(streakUtils.getStreaks(p));

			Score b = objective.getScore(Bukkit.getOfflinePlayer("§aKills"));
			b.setScore(streakUtils.getKills(p));

			Score c = objective.getScore(Bukkit.getOfflinePlayer("§aDeaths"));
			c.setScore(streakUtils.getDeaths(p));

			p.setScoreboard(board);

			removeBoard(p);
		}
	}

	public void removeTempScoreboard(Player p){
		hasBoard.remove(p.getName());
		p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}

	private void removeBoard(final Player p){
		new BukkitRunnable(){
			public void run(){
				if(hasBoard.contains(p.getName())){
					removeTempScoreboard(p);
				}
			}
		}.runTaskLater(plugin, plugin.getConfig().getInt("Scoreboard-Show-Time")*20);
	}
}