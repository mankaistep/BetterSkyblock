package kdvn.sky.advancedore.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.wasteofplastic.askyblock.ASkyBlockAPI;

public class EventHandling implements Listener {

	@EventHandler
	public void onOre(BlockFromToEvent e) {
		World world = e.getBlock().getLocation().getWorld();
		if (e.getToBlock().getType() != Material.AIR)
			return;
		if (e.getBlock() == null || e.getBlock().getType() == Material.AIR)
			return;
		if (Main.DISABLED_WORLDS.contains(world))
			return;

		// Kiểm tra Block 1
		if (!Main.BLOCK_1.isThatBlock(e.getBlock().getType())) {
			return;
		}
		// Kiểm tra Block 2
		boolean check = false;
		List<Location> listL = getToLocation(e.getToBlock().getLocation(), e.getBlock().getLocation());
		for (Location l : listL) {
			if (Main.BLOCK_2.isThatBlock(l.getBlock().getType())) {
				check = true;
				break;
			}
		}
		if (check == false)
			return;
		
		e.setCancelled(true);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				rate(e.getBlock().getLocation(), e.getToBlock());
			}
		}.runTaskAsynchronously(Main.plugin);
		
	}
	
	// Methods 
	
	private List<Location> getToLocation(Location location, Location begin) {
		List<Location> list = new ArrayList<Location>();
		list.add(location.clone().add(1, 0, 0));
		list.add(location.clone().add(-1, 0, 0));
		list.add(location.clone().add(0, 0, 1));
		list.add(location.clone().add(0, 0, -1));
		list.add(location.clone().add(0, 1, 0));
		list.add(location.clone().add(0, -1, 0));
		
		int del = -1;
		for (int i = 0 ; i < list.size() ; i++) {
			Location l = list.get(i);
			if (l.getBlock().getLocation().distance(begin.getBlock().getLocation()) < 1) {
				del = i;
				break;
			}
		}
		
		if (del != -1) list.remove(del);

		return list;
	}

	
	
	private Material rate(Location location, Block block) {
		List<UUID> players = getOwner(location);
		if (players == null) {
			return Material.COBBLESTONE;
		}
		
		// Xác định permission
		CustomOre ore = Main.customOres.get(0);
		boolean check = false;
		for (int i = Main.customOres.size() - 1; i >= 0; i--) {
			String p = Main.customOres.get(i).permission;
			for (UUID player : players) {
//				PermissionUser u = PermissionsEx.getUser(Bukkit.getOfflinePlayer(player).getName());
				if (Bukkit.getPlayer(player).hasPermission(p)) {
					ore = Main.customOres.get(i);
					check = true;
					break;
				}
			}
			if (check) break;

		}
		// Xác định Block
		Material material = rate(ore);
//		for (Material m : ore.blockChance.keySet()) {
//			if (rate(ore.blockChance.get(m))) {
//				material = m;
//				break;
//			}
//		}
//		if (material == null)  {
//			List<Material> ms = new ArrayList<Material> (ore.blockChance.keySet());
//			material = ms.get(new Random().nextInt(ms.size()));
//		}
		
		
		final Material resultMaterial = material;
		new BukkitRunnable() {
			@Override
			public void run() {
				block.setType(resultMaterial);
			}
		}.runTask(Main.plugin);
		
		return material;
	}
	
	private List<UUID> getOwner(Location loc) {
		Set<Location> set = new HashSet<Location>();
		set.add(loc);
		List<UUID> players = ASkyBlockAPI.getInstance().getTeamMembers(ASkyBlockAPI.getInstance().getOwner(loc));
		players.add(ASkyBlockAPI.getInstance().getOwner(loc));
		return players;
	}
	
//	private boolean rate(double tiLe) {
//		double rate = tiLe * 100;
//		int random = new Random().nextInt(10000);
//		if (random < rate) {
//			return true;
//		} else
//			return false;
//	}
	
	private Material rate(CustomOre ore) {
		double sum = 0;
		for (Material m : ore.blockChance.keySet()) {
			sum += ore.blockChance.get(m);
		}
		double ran = (double) new Random().nextInt(new Double(sum * 10000).intValue()) / 10000;
		// Check
		Material result = Material.STONE;
		double nearest = 10000;
		for (Material m : ore.blockChance.keySet()) {
			double sub = Math.abs(ran - ore.blockChance.get(m));
			if (sub < nearest) {
				nearest = sub;
				result = m;
			}
		}
		
		return result;
	}
	
	
	
	

}
