package io.nonstop.core.accept;

public interface ResolvableParser<T extends Resolvable> {

    T parse(String string);

    T parse(String string, int index);

}
