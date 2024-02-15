package fr.paulbrancieq.confeats.client.gui.gson;

import java.util.Collection;

public class GsonOptionGroup {
    @SuppressWarnings("unused")
    private String name;
    @SuppressWarnings("unused")
    private String description;
    @SuppressWarnings("unused")
    private Collection<GsonOption> options;
    @JsonOptional
    @SuppressWarnings("unused")
    private String inGuiId;
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Collection<GsonOption> getOptions() {
        return options;
    }
    public String getInGuiId() {
        return inGuiId != null ? inGuiId : "%s-" + name.toLowerCase().replace(' ', '_');
    }
}
