package fr.paulbrancieq.confeats.client.gui.gson;

import java.util.Collection;

public class GsonYACLScreen {
  @SuppressWarnings("unused")
  private String title;
  @SuppressWarnings("unused")
  private Collection<GsonConfigCategory> categories;
  public String getTitle() {
    return title;
  }
  public Collection<GsonConfigCategory> getCategories() {
    return categories;
  }
}
