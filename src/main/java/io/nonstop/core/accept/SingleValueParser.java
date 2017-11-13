package io.nonstop.core.accept;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class SingleValueParser<T extends Resolvable> implements ResolvableParser<T> {

    private static final Pattern pattern = Pattern.compile("^\\s*([^\\s;]+)\\s*(?:;q=(.*))?$");

    @Override
    public T parse(final String string) {
        return parse(string, 0);
    }

    @Override
    public T parse(final String string, final int index) {
        final Matcher matcher = pattern.matcher(string);
        if(matcher.matches()) {
            String encoding = matcher.group(1);
            float weight = 1.0f;
            if (matcher.group(2) != null) {
                try {
                    weight = Float.parseFloat(matcher.group(2));
                } catch (NumberFormatException nfe) { /* IGNORED */ }
            }
            return create(encoding, weight, index);
        }
        return null;
    }

    abstract T create(String value, float weight, int index);
}
