package fr.paulbrancieq.confeats.confeats.client;

import fr.paulbrancieq.accessoptions.commons.exeptions.AccessOptionsException;
import fr.paulbrancieq.confeats.confeats.Confeats;
import fr.paulbrancieq.confeats.confeats.commons.packets.ChangeOptionPacket;
import fr.paulbrancieq.accessoptions.OptionsAccessHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ConfeatsClient implements ClientModInitializer {
  /**
   * Runs the mod initializer on the client environment.
   */
  @Override
  public void onInitializeClient() {
    registerCommands();
    new ChangeOptionPacket().registerClient();
  }

  public void registerCommands() {
    ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> dispatcher.register(
        ClientCommandManager.literal("setConfig")
            .then(ClientCommandManager.argument("modId", StringArgumentType.string())
                .then(ClientCommandManager.argument("optionId", StringArgumentType.string())
                    .then(ClientCommandManager.argument("value", StringArgumentType.string())
                        .executes(context -> {
                          String modId = StringArgumentType.getString(context, "modId");
                          String optionId = StringArgumentType.getString(context, "optionId");
                          String value = StringArgumentType.getString(context, "value");
                          OptionsAccessHandler optionsAccessHandler = new OptionsAccessHandler();
                          optionsAccessHandler.setChatFeedback(true);
                          try {
                            optionsAccessHandler.instantModifyOption(modId, optionId, value);
                            return 1;
                          } catch (AccessOptionsException e) {
                            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of(e.getMessage()));
                            return 0;
                          } catch (Exception e) {
                            context.getSource().sendError(Text.of("Unknown error. Please report this to the mod author."));
                            Confeats.getLogger().error("Unknown error while trying to modify option", e);
                            return 0;
                          }
                        })
                    )
                )
            )
    ));
  }
}
