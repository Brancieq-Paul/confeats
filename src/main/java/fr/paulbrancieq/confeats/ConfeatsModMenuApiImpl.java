package fr.paulbrancieq.confeats;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import fr.paulbrancieq.confeats.client.gui.OptionsScreenBuilder;

public class ConfeatsModMenuApiImpl implements ModMenuApi {
  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return (screen) -> {
      try {
        return (new OptionsScreenBuilder("config/confeats/menu.json")).generateScreen(screen);
      } catch (Exception e) {
        Confeats.getLogger().error("Failed to load config screen", e);
        return null;
      }
    };
  }
}
