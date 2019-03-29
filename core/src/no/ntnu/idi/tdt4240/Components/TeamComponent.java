package no.ntnu.idi.tdt4240.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class TeamComponent implements Component {
    private static int playerCount = 0;
    public int playerID;
    public int score = 0;
    public TeamComponent(){
        this.playerID = ++playerCount;
    }
}
