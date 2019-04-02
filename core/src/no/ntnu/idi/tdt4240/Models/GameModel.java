package no.ntnu.idi.tdt4240.Models;

public class GameModel {

    public enum State {
        START, //make the board instantiate stuff..
        SETUP, //draws cards, place soldiers
        // USE_CARDS,  // Use risk cards. We don't have that
        ATTACK,
        FORTIFY,
        REWARD,
        PLACING,
    }
    private State state;

    public GameSettings gameSettings;

    public GameModel() {
        gameSettings = new GameSettings();
    }

    public void setup() {
        reset();

        for(int i = 0; i < gameSettings.numberOfPlayers; i++) {
            // TODO: Add teams by number of connected players in a network
        }
    }

    private void reset() {
    }

    public class GameSettings{
        private int numberOfPlayers;
        public int getNumberOfPlayers() {return numberOfPlayers;}
        public void setNumberOfPlayers(int num) {
            if(num > 6) {
                numberOfPlayers = 6;
            } else if(num < 2) {
                numberOfPlayers = 2;
            } else {
                numberOfPlayers = num;
            }
        }
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        System.out.println(this.getState());
    }
}
