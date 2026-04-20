package org.example.BDindex;

import java.util.Objects;

public class AgeNameKey {
    private final int age;
    private final String name;

    public AgeNameKey(int age, String name) {
        this.age = age;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AgeNameKey key)) return false;

        return age == key.age && name.equals(key.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, name);
    }
}