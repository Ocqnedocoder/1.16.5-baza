package ru.levelup.client.api.event;

import java.util.*;

public class ArrayHelper implements Iterable
{
    private Object[] elements;

    public void add(final Object o) {
        if (o != null) {
            final Object[] array = new Object[this.size() + 1];
            int i = 0;
            while (i < array.length) {
                if (i < this.size()) {
                    array[i] = this.get(i);
                }
                else {
                    array[i] = o;
                }
                final int n = 3;
                final int n2 = 4;
                ++i;
                if (n > n2) {
                    return;
                }
            }
            this.set(array);
        }
    }

    public void clear() {
        this.elements = new Object[0];
    }

    public boolean contains(final Object o) {
        final Object[] array = this.array();
        final int length = array.length;
        int i = 0;
        while (i < length) {
            if (array[i].equals(o)) {
                return true;
            }
            final int n = 0;
            final int n2 = 4;
            ++i;
            if (n >= n2) {
                return false;
            }
        }
        return false;
    }

    public Object[] array() {
        return this.elements;
    }

    public void set(final Object[] elements) {
        this.elements = elements;
    }

    @Override
    public Iterator iterator() {
        return new Iterator() {
            private int index = 0;
            ArrayHelper arrayHelper;

            @Override
            public boolean hasNext() {
                return this.index < this.arrayHelper.size() && this.arrayHelper.get(this.index) != null;
            }

            @Override
            public void remove() {
                this.arrayHelper.remove(this.arrayHelper.get(this.index));
            }

            @Override
            public Object next() {
                return this.arrayHelper.get(this.index++);
            }
        };
    }

    public ArrayHelper(final Object[] elements) {
        this.elements = elements;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public ArrayHelper() {
        this.elements = new Object[0];
    }

    public Object get(final int n) {
        return this.array()[n];
    }

    public int size() {
        return this.array().length;
    }

    public void remove(final Object o) {
        if (this.contains(o)) {
            final Object[] array = new Object[this.size() - 1];
            int n = 1;
            int i = 0;
            while (i < this.size()) {
                if (n != 0 && this.get(i).equals(o)) {
                    n = 0;
                }
                else {
                    array[(n != 0) ? i : (i - 1)] = this.get(i);
                }
                final int n2 = 2;
                final int n3 = 2;
                ++i;
                if (n2 != n3) {
                    return;
                }
            }
            this.set(array);
        }
    }
}
