package ru.levelup.client.api.event;

import java.lang.reflect.Method;

public class Data
{
    public final Method target;
    public final Object source;
    public final byte priority;

    public Data(final Object source, final Method target, final byte priority) {
        this.source = source;
        this.target = target;
        this.priority = priority;
    }
}
