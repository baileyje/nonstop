package io.nonstop.core.accept;

/**
 * Class representing a accept type.
 *
 * @author John Bailey
 */
public class Type extends Resolvable {

    public static TypeParser parser = new TypeParser();

    private final String type;

    private final String subtype;

    Type(final String type, final String subtype) {
        this(type, subtype, 1.0f, 0);
    }

    Type(final String type, final String subtype, float weigth, final int index) {
        super(weigth, index);
        this.type = type;
        this.subtype = subtype;
    }

    public String getType() {
        return type;
    }

    public String getSubtype() {
        return subtype;
    }

    @Override
    public String toString() {
        return type + "/" + subtype;
    }

    /**
     * Determines whether a provided type string is satisfied by this type.
     *
     * @param typeStr content type string
     * @return true if this type satisfies the content type provided, false otherwise
     */
    public boolean satisfies(final String typeStr) {
        final Type other = parser.parse(typeStr);
        if (type.equals("*") && subtype.equals("*")) {
            return true;
        } else if (subtype.equals("*")) {
            return type.equals(other.type);
        }
        return this.equals(other);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Type)) return false;
        final Type other = Type.class.cast(obj);
        return type.equals(other.type) && subtype.equals(other.subtype);
    }
}
