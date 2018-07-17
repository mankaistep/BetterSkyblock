package kdvn.sky.betterskyblock.player;

import kdvn.sky.advancedore.main.Main;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerRankEventHandling implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (!PrefixOption.ON_CHAT) return;
		PlayerRank rank = PlayerRank.getRank(e.getPlayer());
		e.setFormat(rank.prefix + e.getFormat());
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		// Data
		PlayerData.playerJoin(player.getName());
		// Rank
		PlayerRank.setRank(player, PlayerRank.checkRank(player));
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		// Data
		PlayerData.playerQuit(player.getName());
		// Rank
		PlayerRank.setRank(player, null);
	}
	
	@EventHandler
	public void onKillMob(EntityDeathEvent e) {
		Player player = e.getEntity().getKiller();
		if (player == null) return;
		PlayerData data = PlayerData.get(player.getName());
		data.mobKill += 1;
		PlayerRank.check(player);
	}
	
	@EventHandler
	public void onKillPlayer(EntityDeathEvent e) {
		if (!(e.getEntity() instanceof Player)) return;
		Player player = e.getEntity().getKiller();
		if (player == null) return;
		PlayerData data = PlayerData.get(player.getName());
		data.playerKill += 1;
		PlayerRank.check(player);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Location loc = e.getBlock().getLocation();
		Material type = e.getBlock().getType();
		
		Bukkit.getScheduler().runTask(Main.plugin, () -> {
			if (loc.getBlock().getType() != Material.AIR) {
				return;
			}
			Player player = e.getPlayer();
			PlayerData data = PlayerData.get(player.getName());
			int count = 1;
			if (data.blockBreak.containsKey(type)) {
				count += data.blockBreak.get(type);
			}
			data.blockBreak.put(type, count);
			PlayerRank.check(player);
		});
		
	}
	
	@EventHandler
	public void onBlockBuild(BlockPlaceEvent e) {
		Location loc = e.getBlock().getLocation();
		
		Bukkit.getScheduler().runTask(Main.plugin, () -> {
			if (loc.getBlock().getType() == Material.AIR) {
				return;
			}
			Player player = e.getPlayer();
			PlayerData data = PlayerData.get(player.getName());
			int count = 1;
			if (data.blockBuild.containsKey(e.getBlock().getType())) {
				count += data.blockBuild.get(e.getBlock().getType());
			}
			data.blockBuild.put(e.getBlock().getType(), count);
			PlayerRank.check(player);
		});

	}
	
	
	
	
	
}
