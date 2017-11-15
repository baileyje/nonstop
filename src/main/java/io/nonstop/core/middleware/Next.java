package io.nonstop.core.middleware;

public interface Next {

    void proceed();

    void fail(final Exception e);

    void fail(final String message);

}
