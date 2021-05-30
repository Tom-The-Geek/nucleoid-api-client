package com.github.tom_the_geek.nac.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class NucleoidServerStatus {
    @SerializedName("game_version")
    public String gameVersion;
    @SerializedName("server_ip")
    public String serverIp;
    public List<NucleoidGame> games;
    public List<NucleoidPlayer> players;

    @Override
    public String toString() {
        return "NucleoidServerStatus{" +
                "gameVersion='" + gameVersion + '\'' +
                ", serverIp='" + serverIp + '\'' +
                ", games=" + games +
                ", players=" + players +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NucleoidServerStatus that = (NucleoidServerStatus) o;
        return gameVersion.equals(that.gameVersion) && serverIp.equals(that.serverIp) && games.equals(that.games) && players.equals(that.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameVersion, serverIp, games, players);
    }
}
