package kdvn.sky.betterskyblock.player;

import kdvn.sky.advancedore.main.Main;

public class PrefixOption {
	
	public static boolean ON_CHAT;
	public static boolean PLACEHOLDER;
	
	public static void init() {
		ON_CHAT = Main.config.getBoolean("prefix-options.on-chat");
		PLACEHOLDER = Main.config.getBoolean("prefix-options.placeholder");
	}
	
}
