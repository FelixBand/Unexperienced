package net.smileycorp.unexperienced;

import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfigHandler {
	
	public static final ModConfigSpec config;

	public static ModConfigSpec.ConfigValue<Boolean> disableXP;
	public static ModConfigSpec.ConfigValue<Boolean> directXP;
	static ModConfigSpec.ConfigValue<Boolean> drinkBottles;
	public static boolean drinkBottlesClient;
	public static ModConfigSpec.ConfigValue<Integer> bottleExperience;

	static {
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
		builder.push("general");
		disableXP = builder.comment("Should xp drops be disabled?").define("disableXP", true);
		directXP = builder.comment("Should mob xp drops be given directly to the player?").define("directXP", false);
		drinkBottles = builder.comment("Can Bottles of enchanting be drunk to gain experience?").define("drinkBottles", true);
		bottleExperience = builder.comment("How many experience points to Bottles of Enchanting give if drunk?").define("bottleExperience", 7);
		builder.pop();
		config = builder.build();
	}

	public static boolean canDrinkBottles() {
		return Thread.currentThread().getThreadGroup() == SidedThreadGroups.CLIENT ? drinkBottlesClient : drinkBottles.get();
	}
}
