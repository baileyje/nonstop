package io.nonstop.core.accept;

public class Language extends SingleValue {

    public static SingleValueParser<Language> parser = new SingleValueParser<Language>() {
        @Override
        Language create(final String value, final float weight, final int index) {
            return new Language(value, weight, index);
        }
    };

    public Language(final String value) {
        super(value);
    }

    public Language(final String value, final float weight, final int index) {
        super(value, weight, index);
    }

    public String getLanguage() {
        return getValue();
    }

    /**
     * Determines whether a provided string is satisfied by this language.
     *
     * @param languageStr content language string
     * @return true if this language satisfies the content language provided, false otherwise
     */
    public boolean satisfies(final String languageStr) {
        if (getValue().equals("*")) {
            return true;
        }
        final Language other = parser.parse(languageStr);
        return this.equals(other);
    }
}
