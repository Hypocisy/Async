package com.axalotl.async.parallelised;

public interface Task {

    void run(Runnable releaseLocks);

    void propagateException(Throwable t);

    LockToken[] lockTokens();

    int priority();
}
