package io.nonstop.core.middleware;

import java.util.*;
import java.util.regex.Pattern;

/**
 * A single step in a middleware chain.
 *
 * @author John Bailey
 */
public class Step {

    final String path;

    final Matcher matcher;

    final Middleware middleware;

    final boolean end;

    public Step(final String path, final boolean end, final Middleware middleware) {
        this.path = path;
        this.middleware = middleware;
        this.matcher = new Matcher(path, end);
        this.end = end;
    }

    MatchResult matches(final String path) {
        // Handle the match all case first
        if (this.path.equals(Chain.DEFAULT_PATH)) {
            return new MatchResult(path, Collections.emptyMap());
        }
        // Else check apply matching
        return matcher.matches(path);
    }

    static class Matcher {

        private static final Pattern FIELD_PATTERN = Pattern.compile(":(\\w+)");

        private final Pattern pattern;

        private final List<String> paraNames = new ArrayList<>();

        Matcher(final String path, final boolean end) {
            final StringBuilder builder = new StringBuilder("^");
            final java.util.regex.Matcher matcher = FIELD_PATTERN.matcher(path);
            int current = 0;
            while (matcher.find()) {
                builder.append(path.substring(current, matcher.start()));
                final String name = path.substring(matcher.start() + 1, matcher.end());
                paraNames.add(name);
                builder.append("(?<" + name + ">[^\\/]+?)");
                current = matcher.end();
            }
            if (paraNames.isEmpty()) {
                builder.append(path);
            }
//            builder.append("\\/?");
            if (end) {
                builder.append("$");
            } else {
                builder.append("(?=\\/|$)");
            }
            this.pattern = Pattern.compile(builder.toString());
        }

        MatchResult matches(final String path) {
            final java.util.regex.Matcher matcher = pattern.matcher(path);
            if (matcher.find()) {
                final String remaining = path.substring(matcher.end());
                final Map<String, String> params = new HashMap<>();
                for (String name : paraNames) {
                    params.put(name, matcher.group(name));
                }
                return new MatchResult(remaining, params);
            }
            return null;
        }

    }

    static class MatchResult {

        String remainingPath;

        Map<String, String> params;

        MatchResult(final String remainingPath, final Map<String, String> params) {
            this.remainingPath = remainingPath;
            this.params = params;
        }

    }
}
