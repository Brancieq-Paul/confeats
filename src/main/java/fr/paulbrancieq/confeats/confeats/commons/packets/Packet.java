package fr.paulbrancieq.confeats.confeats.commons.packets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public abstract class Packet {
  public Identifier packetId;
  public PacketByteBuf buf;

  public Packet(String stringPacketId) {
    this.packetId = new Identifier(stringPacketId.toLowerCase());
    this.buf = PacketByteBufs.create();
  }

  public void registerClient() {
    ClientPlayNetworking.registerGlobalReceiver(this.packetId, this::handleClient);
    ServerPlayNetworking.registerGlobalReceiver(this.packetId, this::handleServer);
  }

  public void registerServer() {
    ServerPlayNetworking.registerGlobalReceiver(this.packetId, this::handleServer);
  }

  public abstract void handleClient(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender);
  public abstract void handleServer(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender);

  @Environment(EnvType.CLIENT)
  public void sendToServer() {
    ClientPlayNetworking.send(this.packetId, this.buf);
  }

  @Environment(EnvType.SERVER)
  public void sendToPlayer(ServerPlayerEntity player) {
    ServerPlayNetworking.send(player, this.packetId, this.buf);
  }
}
