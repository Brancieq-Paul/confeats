package fr.paulbrancieq.confeats.client.exceptions;

import fr.paulbrancieq.confeats.client.gui.gson.GsonOption;

public class YACLOptionBuilderException extends ConfeatsException {
  private YACLOptionBuilderException(String message) {
    super(message);
  }

  public static class CannotValidateGsonOption extends YACLOptionBuilderException {
    private CannotValidateGsonOption(String message) {
      super(message);
    }
    public static class MissingOptionId extends CannotValidateGsonOption {
      public MissingOptionId(GsonOption gsonOption) {
        super("Option id is missing for option");
      }
    }
    public static class MissingOptionStorageId extends CannotValidateGsonOption {
      public MissingOptionStorageId(GsonOption gsonOption) {
        super("Option storage id is missing for option " + gsonOption.getId());
      }
    }
  }

  public static class OptionNotFound extends YACLOptionBuilderException {
    public OptionNotFound(GsonOption gsonOption) {
      super("Option " + gsonOption.getId() + " from storage " + gsonOption.getStorage() + " not found");
    }
  }
}
