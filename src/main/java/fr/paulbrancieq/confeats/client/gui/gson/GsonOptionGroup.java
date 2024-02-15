package fr.paulbrancieq.confeats.client.gui.gson;

import java.util.Collection;

public class GsonOptionGroup {
    @SuppressWarnings("unused")
    private String name;
    @SuppressWarnings("unused")
    private String description;
    @SuppressWarnings("unused")
    private Collection<GsonOption> options;
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Collection<GsonOption> getOptions() {
        return options;
    }
}
