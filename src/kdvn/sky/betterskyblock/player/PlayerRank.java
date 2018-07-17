package kdvn.sky.betterskyblock.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kdvn.sky.advancedore.main.Main;
import kdvn.sky.advancedore.main.TitleAPI;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PlayerRank {
	
	public Map<PlayerRankRequirement, String> requirements = new HashMap<PlayerRankRequirement, String> ();
	public String prefix;
	public List<String> rankUpCommands = new ArrayList<String> ();	
	public int i;
	
	private PlayerRank(String name) {
		FileConfiguration config = Main.config;
		
		if (name.equalsIgnoreCase("default")) {
			this.i = 0;
		} else this.i = Integer.parseInt(name.replace("custom", ""));
		
		if (config.get("player-ranks." + name + ".requirements") != null) {
			for (String s : config.getConfigurationSection("player-ranks." + name + ".requirements").getKeys(false)) {
				this.requirements.put(PlayerRankRequirement.valueOf(s), config.getString("player-ranks." + name + ".requirements." + s));
			}
		}
//		this.requirements = get(rS);
		this.prefix = config.getString("player-ranks." + name + ".prefix").replace("&", "§");
		this.rankUpCommands = config.getStringList("player-ranks." + name + "." + "rank-up-cmds");
	}
	
//	private Map<PlayerRankRequirement, String> get(String s) {
//		String[] a = s.split(";");
//		Map<PlayerRankRequirement, String> map = new HashMap<PlayerRankRequirement, String> ();
//		for (String data : a) {
//			String mS = data.substring(0, data.lastIndexOf(":"));
//			String cS = data.substring(data.lastIndexOf(":") + 1, data.length());
//			map.put(PlayerRankRequirement.valueOf(mS), cS);
//		}
//		
//		return map;
//	}
	
	// STATIC
	
	private static List<PlayerRank> ranks = new ArrayList<PlayerRank> ();
	private static Map<String, PlayerRank> playerRanks = new HashMap<String, PlayerRank> ();
	private static PlayerRank defaultRank;
	
	public static PlayerRank load(String name) {
		PlayerRank rank = new PlayerRank(name);
		if (name.equals("default")) {
			defaultRank = rank; 
		}
		return rank;
	}
	
	public static void load(PlayerRank rank) {
		ranks.add(rank);
	}
	
	public static void loadAll() {
		FileConfiguration config = Main.config;
		ranks.clear();
		for (String s : config.getConfigurationSection("player-ranks").getKeys(false)) {
			load(load(s));
		}
	}
	
	public static PlayerRank checkRank(Player player) {
		int max = 0;
		PlayerRank result = defaultRank;
		for (PlayerRank rank : ranks) {
			for (PlayerRankRequirement r : rank.requirements.keySet()) {
				if (!r.check(player, rank.requirements.get(r))) {
					return result;
				}
			}
			int i = rank.i;
			if (i > max) {
				max = i;
				result = rank;
			}
		}
		
		return result;
	}
	
	public static PlayerRank getRank(Player player) {
		return playerRanks.get(player.getName().toLowerCase());
	}
	
	public static void setRank(Player player, PlayerRank rank) {
		if (rank == null) {
			playerRanks.remove(player);
			return;
		}
		playerRanks.put(player.getName().toLowerCase(), rank);
	}
	
	public static void check(Player player) {
		PlayerRank currentRank = getRank(player);
		PlayerRank checkRank = checkRank(player);
		if (currentRank != checkRank) {
			TitleAPI.sendTitle(player, 10, 30, 10, "§2§lRANK UP ▲", "§6Rank hiện tại: §f" + checkRank.prefix);
			player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 1, 1);
			setRank(player, checkRank);
			
			for (String s : checkRank.rankUpCommands) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("{player}", player.getName()));
			}
			
			Main.plugin.getLogger().info("Player " + player.getName() + " is ranked up");
		}
	}
	
	
	
	
	
	
	
	
	
}
