package fr.paulbrancieq.confeats.confeats.commons.packets;

import fr.paulbrancieq.accessoptions.commons.exeptions.AccessOptionsException;
import fr.paulbrancieq.confeats.confeats.Confeats;
import fr.paulbrancieq.accessoptions.OptionsAccessHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import fr.paulbrancieq.confeats.confeats.Constants;

public class ChangeOptionPacket extends Packet {
  public String modId;
  public String optionId;
  public String value;

  public ChangeOptionPacket(String modId, String optionId, String value) {
    super(Constants.PACKET_SET_OPTION_ID);
    this.modId = modId;
    this.optionId = optionId;
    this.value = value;
    buf.writeString(modId);
    buf.writeString(optionId);
    buf.writeString(value);
  }

  public ChangeOptionPacket() {
    super("changeOption");
  }

  @Override
  public void handleClient(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
    String modId = buf.readString();
    String optionId = buf.readString();
    String value = buf.readString();
    client.inGameHud.getChatHud().addMessage(Text.of("Received instruction to modify option '" + optionId + "' from mod '" + modId + "' to value '" + value + "'"));
    OptionsAccessHandler optionsAccessHandler = new OptionsAccessHandler();
    optionsAccessHandler.setChatFeedback(true);
    try {
      optionsAccessHandler.instantModifyOption(modId, optionId, value);
    } catch (AccessOptionsException e) {
      client.inGameHud.getChatHud().addMessage(Text.of(e.getMessage()));
    } catch (Exception e) {
      client.inGameHud.getChatHud().addMessage(Text.of("Unknown error. Please report this to the mod author."));
      Confeats.getLogger().error("Unknown error while trying to modify option", e);
    }
  }

  @Override
  public void handleServer(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
  }
}
