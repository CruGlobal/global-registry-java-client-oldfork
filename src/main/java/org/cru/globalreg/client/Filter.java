package org.cru.globalreg.client;

public final class Filter {
    private final String[] path;
    private final String value;

    public Filter() {
        this(new String[0], "");
    }

    public Filter(final String[] path, final String value) {
        this.path = path;
        this.value = value;
    }

    public final Filter path(final String... path) {
        return new Filter(path != null ? path : new String[0], this.value);
    }

    public final Filter value(final String value) {
        return new Filter(this.path, value != null ? value : "");
    }

    public final boolean isValid() {
        return this.path.length > 0;
    }

    public final String[] getPath() {
        return this.path;
    }

    public String getFilter() {
        if (this.isValid()) {
            final StringBuilder sb = new StringBuilder("filters");
            for (final String field : this.path) {
                sb.append("[").append(field).append("]");
            }
            return sb.toString();
        }
        return null;
    }

    public final String getValue() {
        return this.value;
    }

}
