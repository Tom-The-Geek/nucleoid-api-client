package com.github.tom_the_geek.nac.response;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class NucleoidGame {
    public String name;
    public String type;
    @SerializedName("player_count")
    public int playerCount;

    @Override
    public String toString() {
        return "NucleoidGame{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", playerCount=" + playerCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NucleoidGame that = (NucleoidGame) o;
        return playerCount == that.playerCount && name.equals(that.name) && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, playerCount);
    }
}
