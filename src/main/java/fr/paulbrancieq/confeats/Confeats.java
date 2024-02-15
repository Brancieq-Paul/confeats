package fr.paulbrancieq.confeats;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Confeats implements ModInitializer {
  @SuppressWarnings("unused")
  public static final String MOD_ID = Constants.MOD_ID;
  public static final String MOD_NAME = Constants.MOD_NAME;
  private static Confeats INSTANCE;
  private static Logger LOGGER;

  @Override
  public void onInitialize() {
    INSTANCE = this;
  }

  @SuppressWarnings("unused")
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
