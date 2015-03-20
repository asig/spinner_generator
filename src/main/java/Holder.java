// Copyright 2015 Andreas Signer. All rights reserved.

public class Holder<T> {
    private T value;

    public Holder(T initial) {
        this.value = initial;
    }

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}
