package io.nonstop.core.accept;

/**
 * Class representing a accept charset.
 *
 * @author John Bailey
 */
public class Charset extends SingleValue {

    public static SingleValueParser<Charset> parser = new SingleValueParser<Charset>() {
        @Override
        Charset create(String value, float weight, int index) {
            return new Charset(value, weight, index);
        }
    };

    public Charset(String value) {
        super(value);
    }

    public Charset(String value, float weight, int index) {
        super(value, weight, index);
    }

    public String getCharset() {
        return getValue();
    }

    /**
     * Determines whether a provided string is satisfied by this charset.
     *
     * @param charsetStr content charset string
     * @return true if this encoding satisfies the content charset provided, false otherwise
     */
    public boolean satisfies(final String charsetStr) {
        if (getValue().equals("*")) {
            return true;
        }
        final Charset other = parser.parse(charsetStr);
        return this.equals(other);
    }
}
