package com.github.wakingrufus.elo.elo;

public class KfactorCalculator {
    public int calculateKfactor(int kfactorBase, int trialPeriod, int trialMultiplier, int playerGamesPlayed, int otherPlayerGamesPlayed) {
        int kFactor = kfactorBase;

        if (playerGamesPlayed < trialPeriod) {
            kFactor *= trialMultiplier;

        }
        if (otherPlayerGamesPlayed < trialPeriod) {
            kFactor /= trialMultiplier;
        }
        return kFactor;
    }
}
