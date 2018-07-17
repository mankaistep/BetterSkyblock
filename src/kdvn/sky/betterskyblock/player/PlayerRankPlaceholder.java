package kdvn.sky.betterskyblock.player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.entity.Player;


public class PlayerRankPlaceholder extends PlaceholderExpansion {

	@Override
	public String onPlaceholderRequest(Player player, String placeholder) {
		// bs_rank_prefix
		if (placeholder.equals("rank_prefix")) {
			return PlayerRank.getRank(player).prefix;
		}
		
		return null;
	}

	@Override
	public String getAuthor() {
		return "MankaiStep";
	}

	@Override
	public String getIdentifier() {
		return "bs";
	}

	@Override
	public String getPlugin() {
		return "BetterSkyblock";
	}

	@Override
	public String getVersion() {
		return "";
	}

}
