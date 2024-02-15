package fr.paulbrancieq.confeats.client.gui;

import fr.paulbrancieq.accessoptions.OptionsAccessHandler;
import fr.paulbrancieq.accessoptions.commons.options.Option;
import fr.paulbrancieq.confeats.Confeats;
import fr.paulbrancieq.confeats.client.exceptions.YACLOptionBuilderException;
import fr.paulbrancieq.confeats.client.gui.gson.GsonOption;

public class YACLOptionBuilder<T> {
  private final Option<?, T> option;
  private final OptionsAccessHandler optionsAccessHandler;
  @SuppressWarnings("unchecked")
  public YACLOptionBuilder(Class<T> type, GsonOption gsonOption, OptionsAccessHandler optionsAccessHandler) throws YACLOptionBuilderException.OptionNotFound {
    this.optionsAccessHandler = optionsAccessHandler;
    this.option = (Option<?, T>) gsonOption.getOption(optionsAccessHandler);
    if (option == null) {
      Confeats.getLogger().warn("Option " + gsonOption + " from storage " + gsonOption.getStorage() + " not found");
      throw new YACLOptionBuilderException.OptionNotFound(gsonOption);
    }
    if (this.option.getOriginalValue().getClass() != type) {
      throw new IllegalArgumentException("The type of the option does not match the type of the builder.");
    }
  }
}
