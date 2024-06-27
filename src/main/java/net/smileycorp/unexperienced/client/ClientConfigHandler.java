package net.smileycorp.unexperienced.client;

import com.google.common.collect.Lists;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class ClientConfigHandler {
	
	public static final ModConfigSpec config;

	public static ModConfigSpec.ConfigValue<Boolean> hideBar;
	private static ModConfigSpec.ConfigValue<List<? extends String>> showBarItemsOption;
	private static ModConfigSpec.ConfigValue<List<? extends String>> showBarBlocksOption;

	private static List<Item> showBarItems = Lists.newArrayList();
	private static List<Block> showBarBlocks = Lists.newArrayList();

	static {
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
		builder.push("general");
		hideBar = builder.comment("Should the exp bar be hidden?").define("hideBar", true);
		showBarItemsOption = builder.comment("Items that when held show the xp bar, when hideBar is true.")
				.define("showBarItems", Lists.newArrayList("minecraft:experience_bottle"));
		showBarBlocksOption = builder.comment("Blocks that when hovered over show the xp bar, when hideBar is true.")
				.define("showBarBlocks", Lists.newArrayList("minecraft:enchanting_table", "minecraft:anvil", "minecraft:grindstone"));
		builder.pop();
		config = builder.build();
	}

	public static boolean shouldShowBar(ItemStack stack) {
		if (stack != null) {
			if (showBarItems.isEmpty() &! showBarItemsOption.get().isEmpty()) {
				for (String item : showBarItemsOption.get()) {
					try {
						ResourceLocation loc = ResourceLocation.tryParse(item);
						if (BuiltInRegistries.ITEM.containsKey(loc)) {
							showBarItems.add(BuiltInRegistries.ITEM.get(loc));
						}
					} catch (Exception e) {}
				}
			}
			if (showBarItems.contains(stack.getItem())) return true;
		}
		return false;
	}

	public static boolean shouldShowBar(BlockState state) {
		if (state != null) {
			if (showBarBlocks.isEmpty() &! showBarBlocksOption.get().isEmpty()) {
				for (String block : showBarBlocksOption.get()) {
					try {
						ResourceLocation loc = ResourceLocation.tryParse(block);
						if (BuiltInRegistries.BLOCK.containsKey(loc)) {
							showBarBlocks.add(BuiltInRegistries.BLOCK.get(loc));
						}
					} catch (Exception e) {}
				}
			}
			if (showBarBlocks.contains(state.getBlock())) return true;
		}
		return false;
	}

}
