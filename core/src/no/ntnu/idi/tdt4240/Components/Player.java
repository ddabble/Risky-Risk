package no.ntnu.idi.tdt4240.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class Player implements Component {
    public int playerID;
    public int score = 0;
    public Player(int playerID){
        this.playerID = playerID;
    }


}
