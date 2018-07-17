package kdvn.sky.betterskyblock.player;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public enum PlayerRankRequirement {
	
	BLOCK_BREAK {
		@Override
		public boolean check(Player player, String data) {
			PlayerData d = PlayerData.get(player.getName());
			Map<Material, Integer> count = PlayerData.getBlockCount(data);
			
			for (Material m : count.keySet()) {
				if (d.blockBreak.containsKey(m)) {
					if (d.blockBreak.get(m) < count.get(m)) return false;
				} else return false;
			}
			
			return true;
		}
	},
	BLOCK_BUILD {
		@Override
		public boolean check(Player player, String data) {
			PlayerData d = PlayerData.get(player.getName());
			Map<Material, Integer> count = PlayerData.getBlockCount(data);
			
			for (Material m : count.keySet()) {
				if (d.blockBuild.containsKey(m)) {
					if (d.blockBuild.get(m) < count.get(m)) return false;
				} else return false;
			}
			
			return true;
		}
	},
	MOB_KILL {
		@Override
		public boolean check(Player player, String data) {
			int amount = Integer.parseInt(data);
			return PlayerData.get(player.getName()).mobKill >= amount;
		}
	},
	PLAYER_KILL {
		@Override
		public boolean check(Player player, String data) {
			int amount = Integer.parseInt(data);
			return PlayerData.get(player.getName()).playerKill >= amount;
		}
	};
	
	public abstract boolean check(Player player, String data);
	
}
