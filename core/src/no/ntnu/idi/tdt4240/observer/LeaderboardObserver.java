package no.ntnu.idi.tdt4240.observer;

import java.util.Map;

public interface LeaderboardObserver {
    void create(Map<Integer, Integer> leaderboard);

    void updateLeaderboard(Map<Integer, Integer> leaderboard);
}
