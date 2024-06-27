package net.smileycorp.unexperienced;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.smileycorp.unexperienced.client.ClientConfigHandler;
import net.smileycorp.unexperienced.network.SyncBottlesMessage;

@Mod(Constants.MODID)
public class Unexperienced {

	public Unexperienced(ModContainer container, IEventBus bus) {
		NeoForge.EVENT_BUS.register(new EventHandler());
		container.registerConfig(ModConfig.Type.COMMON, CommonConfigHandler.config);
		container.registerConfig(ModConfig.Type.CLIENT, ClientConfigHandler.config);
		bus.addListener(Unexperienced::initPackets);
	}
	
	public static void initPackets(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar channel = event.registrar("1");
		channel.commonToClient(SyncBottlesMessage.TYPE,  new SyncBottlesMessage.Decoder(), SyncBottlesMessage::process);
	}
	
	public static void addExperience(Player player, int xpValue) {
		if (player != null) new ExperienceOrb(player.level(), 0, 0, 0, xpValue).playerTouch(player);
	}
	
}
