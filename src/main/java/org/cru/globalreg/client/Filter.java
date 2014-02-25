package org.cru.globalreg.client;

public final class Filter {
    // TODO: this is a quick search filter implementation, the creation of filters probably needs to be a bit more
    // abstract and not so tightly coupled to the API param implementation
    private final String field;
    private final String value;

    public Filter(final String field, final String value) {
        this.field = field;
        this.value = value;
    }

    public final String getField() {
        return this.field;
    }

    public final String getValue() {
        return this.value;
    }
}
