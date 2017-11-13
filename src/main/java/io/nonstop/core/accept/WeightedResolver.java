package io.nonstop.core.accept;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility class used to resolve the ordered Accept type list from request headers.
 *
 * @author John Bailey
 */
public class WeightedResolver {

    /**
     * Resolve the ordered accept list from the provided header list.
     *
     * @param values List of accept header values
     * @return ordered list of content types accepted
     */
    public static <T extends Resolvable> List<T> resolve(final Iterable<String> values, final ResolvableParser<T> parser)  {
        final List<T> resolved = new LinkedList<>();
        int index = 0;
        for (String valueStr : values) {
            for (String value : valueStr.split(",")) {
                final T item = parser.parse(value, index++);
                if(item != null) {
                    resolved.add(item);
                }
            }
        }
        Collections.sort(resolved);
        return resolved;
    }
}
