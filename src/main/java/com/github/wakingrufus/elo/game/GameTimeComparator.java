package com.github.wakingrufus.elo.game;

import java.util.Comparator;


public class GameTimeComparator implements Comparator<Game> {
    @Override
    public int compare(Game game, Game t1) {
        return (int) (t1.getEntryDate().toEpochSecond() - game.getEntryDate().toEpochSecond());
    }
}
