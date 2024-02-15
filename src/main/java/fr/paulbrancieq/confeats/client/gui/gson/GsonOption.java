package fr.paulbrancieq.confeats.client.gui.gson;

import fr.paulbrancieq.accessoptions.OptionsAccessHandler;
import fr.paulbrancieq.accessoptions.commons.options.Option;
import fr.paulbrancieq.accessoptions.commons.storage.OptionsStorage;
import fr.paulbrancieq.confeats.Confeats;

public class GsonOption {
  @SuppressWarnings("unused")
  private String storage;
  @SuppressWarnings("unused")
  private String id;
  private String inGuiId;
  public Option<?, ?> getOption(OptionsAccessHandler optionsAccessHandler) {
    OptionsStorage<?> storage = optionsAccessHandler.getOptionsStorage(this.storage);
    if (storage == null) {
      Confeats.getLogger().warn("Storage " + this.storage + " not found");
      return null;
    }
    Option<?, ?> option = storage.getOption(this.id);
    if (option == null) {
      Confeats.getLogger().warn("Option " + this.id + " not found in storage " + this.storage);
    }
    return option;
  }

  public String getStorage() {
    return storage;
  }

  public String getId() {
    return id;
  }
  public String getInGuiId() {
    return inGuiId;
  }
}
