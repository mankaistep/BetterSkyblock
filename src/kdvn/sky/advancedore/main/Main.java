package kdvn.sky.advancedore.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kdvn.sky.betterskyblock.player.PlayerData;
import kdvn.sky.betterskyblock.player.PlayerRank;
import kdvn.sky.betterskyblock.player.PlayerRankEventHandling;
import kdvn.sky.betterskyblock.player.PlayerRankPlaceholder;
import kdvn.sky.betterskyblock.player.PrefixOption;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	public static FileConfiguration config;
	public static List<CustomOre> customOres = new ArrayList<CustomOre> ();
	
	public static BlockType BLOCK_1;
	public static BlockType BLOCK_2;
	
	public static List<World> DISABLED_WORLDS = new ArrayList<World> ();
	
	@Override
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage("§b[BetterSkyblock] §aPlugin was enabled! - Author: KeoDeoVN");
		//
		plugin = this;
		this.saveDefaultConfig();
		reloadConfiguration();
		PlayerData.createFolder();
		//
		Bukkit.getPluginManager().registerEvents(new EventHandling(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerRankEventHandling(), this);
		// Placeholder
		if (PrefixOption.PLACEHOLDER) {
			new PlayerRankPlaceholder().register();
			Bukkit.getConsoleSender().sendMessage("§b[BetterSkyblock] §aRegistered placeholder!");
		}
		//
		Bukkit.getOnlinePlayers().forEach(p -> {
			PlayerData.playerJoin(p.getName());
		});
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		if (cmd.getName().equalsIgnoreCase("bs") && sender.hasPermission("bs.*")) {
			if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				reloadConfiguration();
				sender.sendMessage("§aReloaded");
			}
		}
		return false;
	}
	
	public static void reloadConfiguration() {
		File file = new File(Main.plugin.getDataFolder(), "config.yml");
		config = YamlConfiguration.loadConfiguration(file);
		customOres = CustomOre.getListFromConfig();
		BLOCK_1 = BlockType.valueOf(config.getString("material1"));
		BLOCK_2 = BlockType.valueOf(config.getString("material2"));
		DISABLED_WORLDS.clear();
		List<String> worlds = config.getStringList("disabled-worlds");
		for (String s : worlds) {
			for (World w : Bukkit.getWorlds()) {
				if (w.getName().equalsIgnoreCase(s)) {
					DISABLED_WORLDS.add(w);
				}
			}
		}
		PrefixOption.init();
		PlayerRank.loadAll();
		//
	}
	
}
