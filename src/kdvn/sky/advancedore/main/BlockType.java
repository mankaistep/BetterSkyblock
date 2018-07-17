package kdvn.sky.advancedore.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;


public class BlockType {
	
//	FENCE(Arrays.asList(new Material[] {Material.FENCE, Material.SPRUCE_FENCE, Material.BIRCH_FENCE, Material.JUNGLE_FENCE, Material.ACACIA_FENCE})),
//	WATER(Arrays.asList(new Material[] {Material.STATIONARY_WATER})),
//	LAVA(Arrays.asList(new Material[] {Material.LAVA, Material.STATIONARY_LAVA}));
	
	private List<Material> list = new ArrayList<Material> ();
	
	private BlockType(List<Material> list) {
		this.list = list;
	}
	
	public List<Material> getList() {
		return this.list;
	}
	
	public boolean isThatBlock(Material material) {
		return this.list.contains(material);
	}
	
	public static BlockType valueOf(String s) {
		String list = Main.config.getString("materials." + s);
		String[] mList = list.split(",");
		List<Material> result = new ArrayList<Material> ();
		for (String m : mList) {
			result.add(Material.valueOf(m));
		}
		
		return new BlockType(result);
	}
	
}
