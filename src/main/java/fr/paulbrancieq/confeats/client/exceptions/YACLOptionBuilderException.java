package fr.paulbrancieq.confeats.client.exceptions;

import fr.paulbrancieq.confeats.client.gui.gson.GsonOption;

public class YACLOptionBuilderException extends ConfeatsException {
  private YACLOptionBuilderException(String message) {
    super(message);
  }

  public static class OptionNotFound extends YACLOptionBuilderException {
    public OptionNotFound(GsonOption gsonOption) {
      super("Option " + gsonOption.getId() + " from storage " + gsonOption.getStorage() + " not found");
    }
  }
}
