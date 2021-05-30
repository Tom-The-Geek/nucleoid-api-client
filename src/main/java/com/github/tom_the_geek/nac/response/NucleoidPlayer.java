package com.github.tom_the_geek.nac.response;

import java.util.Objects;
import java.util.UUID;

public class NucleoidPlayer {
    public UUID id;
    public String name;

    @Override
    public String toString() {
        return "NucleoidPlayer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NucleoidPlayer that = (NucleoidPlayer) o;
        return id.equals(that.id) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
