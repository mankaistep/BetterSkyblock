package kdvn.sky.betterskyblock.player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kdvn.sky.advancedore.main.Main;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerData {
	
	public Map<Material, Integer> blockBreak = new HashMap<Material, Integer> ();
	public Map<Material, Integer> blockBuild = new HashMap<Material, Integer> ();
	public int playerKill;
	public int mobKill;
	public PlayerRank currentRank;
	
	String playerName;
	
	private PlayerData(String playerName) {
		this.playerName = playerName;
		File file = new File(Main.plugin.getDataFolder(), "/player-data/" + playerName.toLowerCase() + ".yml");
		if (!file.exists() || file.length() == 0) {
			try {
				file.createNewFile();
				this.playerKill = 0;
				this.mobKill = 0;
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		this.blockBreak = getBlockCount(config.getString("break"));
		this.blockBuild = getBlockCount(config.getString("build"));
		this.playerKill = config.getInt("player-kill");
		this.mobKill = config.getInt("mob-kill");
	}
	
	public void save() {
		File file = new File(Main.plugin.getDataFolder(), "/player-data/" + playerName.toLowerCase() + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set("break", toString(this.blockBreak));
		config.set("build", toString(this.blockBuild));
		config.set("player-kill", this.playerKill);
		config.set("mob-kill", this.mobKill);
		
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Main.plugin.getLogger().info("Saved data of player: " + playerName);
	}
	
	private String toString(Map<Material, Integer> map) {
		if (map.size() == 0) return "";
		String result = "";
		for (Material m : map.keySet()) {
			result += m.name() + ":" + map.get(m) + ";";
		}
		
		result = result.substring(0, result.length() - 1);
		
		return result;
	}
	
	// STATIC
	
	private static Map<String, PlayerData> data = new HashMap<String, PlayerData> ();
	
	public static void createFolder() {
		File file = new File(Main.plugin.getDataFolder(), "/player-data");
		file.mkdir();
	}
	
	public static void playerQuit(String playerName) {
		get(playerName).save();
		if (data.containsKey(playerName.toLowerCase())) {
			data.remove(playerName.toLowerCase());
		}
	}
	
	public static void playerJoin(String playerName) {
		load(playerName);
	}
	
	public static PlayerData get(String playerName) {
		if (data.containsKey(playerName.toLowerCase())) {
			return data.get(playerName.toLowerCase());
		}
		
		return null;
	}
	
	public static void saveAll() {
		data.keySet().forEach(p -> {
			get(p).save();
		});
	}
	
	private static PlayerData load(String playerName) {
		PlayerData playerData = new PlayerData(playerName);
		data.put(playerName.toLowerCase(), playerData);
		Main.plugin.getLogger().info("Loaded data of player: " + playerName);
		return playerData;
	}
	
	public static Map<Material, Integer> getBlockCount(String s) {
		String[] a = s.split(";");
		Map<Material, Integer> map = new HashMap<Material, Integer> ();
		if (s.equalsIgnoreCase("")) return map;
		for (String data : a) {
			String mS = data.substring(0, data.lastIndexOf(":"));
			String cS = data.substring(data.lastIndexOf(":") + 1, data.length());
			map.put(Material.valueOf(mS), Integer.parseInt(cS));
		}
		
		return map;
	}
}
