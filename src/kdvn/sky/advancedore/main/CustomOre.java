package kdvn.sky.advancedore.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;

public class CustomOre {
	
	public String name;
	public boolean isDefault;
	public Map<Material, Double> blockChance = new HashMap<Material, Double> ();
	public String permission;
	
	public CustomOre(String name) {
		this.name = name;
		if (name.equals("default")) {
			this.isDefault = true;
		} else this.isDefault = false;
		this.permission = Main.config.getString("ores." + name + ".permission");
		List<String> list = Main.config.getStringList("ores." + name + ".blocks");
		Map<Material, Double> blockChance = new HashMap<Material, Double> ();
		for (String s : list) {
			String block = s.substring(0, s.lastIndexOf(":"));
			String chanceS = s.substring(s.indexOf(":") + 1, s.length());
			try {
				Material material = Material.valueOf(block);
				double chance = Double.parseDouble(chanceS);
				blockChance.put(material, chance);
			}
			catch (Exception ex) {
				System.out.println("Error: " + block + " : " + chanceS);
				continue;
			}
		}
		this.blockChance = blockChance;
	}
	
	public static List<CustomOre> getListFromConfig() {
		int id = 1;
		List<CustomOre> list = new ArrayList<CustomOre> ();
		list.add(new CustomOre("default"));
		while (Main.config.contains("ores.custom" + id)) {
			list.add(new CustomOre("custom" + id));
			id++;
		}
		return list;
	}
	
}
