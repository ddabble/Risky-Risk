package no.ntnu.idi.tdt4240.observer;

import java.util.List;
import java.util.Map;

public interface LeaderboardObserver {
    void create();

    void updateLeaderboard(List<Map.Entry<Integer, Integer>> numTerritoriesPerPlayer_sorted);
}
