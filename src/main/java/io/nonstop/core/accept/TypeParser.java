package io.nonstop.core.accept;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TypeParser implements ResolvableParser<Type> {

    private static final Pattern mimePattern = Pattern.compile("^\\s*([^\\s/;]+)/([^;\\s]+)\\s*(?:;q=(.*))?$");

    @Override
    public Type parse(final String typeStr) {
        return parse(typeStr, 0);
    }

    @Override
    public Type parse(final String typeStr, final int index) {
        final Matcher matcher = mimePattern.matcher(typeStr);
        if(matcher.matches()) {
            String type = matcher.group(1);
            String subtype = matcher.group(2);
            float weight = 1.0f;
            if (matcher.group(3) != null) {
                try {
                    weight = Float.parseFloat(matcher.group(3));
                } catch (NumberFormatException nfe) { /* IGNORED */ }
            }
            return new Type(type, subtype, weight, index);
        }
        return null;
    }

}
