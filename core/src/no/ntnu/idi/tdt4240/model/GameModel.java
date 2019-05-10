package no.ntnu.idi.tdt4240.model;

import no.ntnu.idi.tdt4240.data.Player;

/**
 * The GameModel class is the first entry point when clicking on the GameView.
 * Since a click is affected by Phase and the Board, the GameModel has a reference to both
 */
public class GameModel {
    private final TurnModel turnModel;
    private final Player playerModel;

    public GameModel() {
        playerModel = new Player();
        //Temporary first player ID
        turnModel = new TurnModel(0, 8);

    }

    public Player getPlayerModel() {
        return playerModel;
    }

    public TurnModel getTurnModel() {
        return turnModel;
    }
}
