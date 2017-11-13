package io.nonstop.core.accept;

abstract class SingleValue extends Resolvable {

    private final String value;

    SingleValue(final String value) {
        this(value, 1.0f, 0);
    }

    SingleValue(final String value, final float weight, final int index) {
        super(weight, index);
        this.value = value != null ? value : "UNKNOWN";
    }

    String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof SingleValue)) return false;
        final SingleValue other = SingleValue.class.cast(obj);
        return value.equals(other.value);
    }

    @Override
    public String toString() {
        return value;
    }

}
