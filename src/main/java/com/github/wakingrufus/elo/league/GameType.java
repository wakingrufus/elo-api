package com.github.wakingrufus.elo.league;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@AllArgsConstructor
public enum GameType {
    CHESS(1, "Chess"), FOOSBALL(2, "Foosball"), TABLE_TENNIS(3, "Table Tennis");

    private final int id;
    private final String typeName;

    public static GameType forTypeName(String name) {
        GameType gameType = null;
        for (GameType gt : EnumSet.allOf(GameType.class)) {
            if (gt.getTypeName().equals(name)) {
                gameType = gt;
            }
        }
        return gameType;
    }

    public static GameType forId(int id) {
        GameType gameType = null;
        for (GameType gt : EnumSet.allOf(GameType.class)) {
            if (gt.getId() == id) {
                gameType = gt;
            }
        }
        return gameType;
    }

    @JsonCreator
    public static GameType fromNode(JsonNode node) {
        GameType gt = null;
        if (node.has("id")) {
            gt = GameType.forId(node.get("id").asInt());
        }
        if (gt == null && node.has("typeName")) {
            gt = GameType.forTypeName(node.get("typeName").asText());
        }
        return gt;
    }
}
