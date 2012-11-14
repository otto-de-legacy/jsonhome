package de.otto.jsonhome.registry.controller;

import java.net.URI;
import java.util.UUID;

/**
 * @author Guido Steinacker
 * @since 14.11.12
 */
public final class RegistryEntry {

    private final UUID uuid;
    private final String title;
    private final URI href;

    public RegistryEntry(final String title, final URI href) {
        this.uuid = UUID.randomUUID();
        this.title = title;
        this.href = href;
    }

    public RegistryEntry(final UUID uuid, final String title, final URI href) {
        this.uuid = uuid;
        this.title = title;
        this.href = href;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getTitle() {
        return title;
    }

    public URI getHref() {
        return href;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegistryEntry that = (RegistryEntry) o;

        if (href != null ? !href.equals(that.href) : that.href != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (href != null ? href.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RegistryEntry{" +
                "title='" + title + '\'' +
                ", href=" + href +
                '}';
    }
}
