package no.ntnu.idi.tdt4240.observer;

import java.util.HashMap;

public interface LeaderboardObserver {
    void create(HashMap<Integer, Integer> leaderboard);

    void updateLeaderboard(HashMap<Integer, Integer> leaderboard);
}
