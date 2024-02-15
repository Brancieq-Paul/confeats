package fr.paulbrancieq.confeats.client.gui.gson;

import java.util.Collection;

public class GsonConfigCategory {
    @SuppressWarnings("unused")
    @JsonOptional
    private String inGuiId;
    @SuppressWarnings("unused")
    private String name;
    @SuppressWarnings("unused")
    private String description;
    @SuppressWarnings("unused")
    private Collection<GsonOptionGroup> groups;
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Collection<GsonOptionGroup> getGroups() {
        return groups;
    }
    public String getInGuiId() {
        return inGuiId != null ? inGuiId : name.toLowerCase().replace(' ', '_');
    }
}
