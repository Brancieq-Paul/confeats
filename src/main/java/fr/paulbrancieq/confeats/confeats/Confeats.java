package fr.paulbrancieq.confeats.confeats;

import fr.paulbrancieq.confeats.confeats.commons.packets.ChangeOptionPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.HashMap;

public class Confeats implements ModInitializer {
  public static final String MOD_ID = Constants.MOD_ID;
  public static final String MOD_NAME = Constants.MOD_NAME;
  private static Confeats INSTANCE;
  private static Logger LOGGER;

  @Override
  public void onInitialize() {
    INSTANCE = this;
  }

  public static Confeats getInstance() {
    return INSTANCE;
  }

  public static Logger getLogger() {
    if (LOGGER == null) {
      LOGGER = LoggerFactory.getLogger(MOD_NAME)  ;
    }
    return LOGGER;
  }
}
