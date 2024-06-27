package net.smileycorp.unexperienced.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.smileycorp.unexperienced.CommonConfigHandler;
import net.smileycorp.unexperienced.Constants;

public class SyncBottlesMessage implements CustomPacketPayload {
	
	public static final Type<SyncBottlesMessage> TYPE = new Type(ResourceLocation.tryBuild(Constants.MODID, "sync_bottles"));
	
	private final boolean value;
	
	public SyncBottlesMessage(boolean value) {
		this.value = value;
	}
	
	public boolean get() {
		return value;
	}
	
	public void process(IPayloadContext ctx) {
		ctx.enqueueWork(() -> CommonConfigHandler.drinkBottlesClient = value);
	}
	
	@Override
	public Type<SyncBottlesMessage> type() {
		return TYPE;
	}
	
	public static class Decoder implements StreamCodec<FriendlyByteBuf, SyncBottlesMessage> {
		
		@Override
		public SyncBottlesMessage decode(FriendlyByteBuf buf) {
			return new SyncBottlesMessage(buf.readBoolean());
		}
		
		@Override
		public void encode(FriendlyByteBuf buf, SyncBottlesMessage message) {
			buf.writeBoolean(message.get());
		}
		
	}
}
