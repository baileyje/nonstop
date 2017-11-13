package io.nonstop.core.accept;

/**
 * Class representing a accept encoding.
 *
 * @author John Bailey
 */
public class Encoding extends SingleValue {

    public static SingleValueParser<Encoding> parser = new SingleValueParser<Encoding>() {
        @Override
        Encoding create(final String value, final float weight, final int index) {
            return new Encoding(value, weight, index);
        }
    };

    public Encoding(final String value) {
        super(value);
    }

    public Encoding(final String value, final float weight, final int index) {
        super(value, weight, index);
    }

    public String getEncoding() {
        return getValue();
    }

    /**
     * Determines whether a provided string is satisfied by this encoding.
     *
     * @param encodingStr content encoding string
     * @return true if this encoding satisfies the content encoding provided, false otherwise
     */
    public boolean satisfies(final String encodingStr) {
        final Encoding other = parser.parse(encodingStr);
        if (getValue().equals("*")) {
            return true;
        }
        return this.equals(other);
    }
}
