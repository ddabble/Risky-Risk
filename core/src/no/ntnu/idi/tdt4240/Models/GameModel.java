package no.ntnu.idi.tdt4240.Models;

public class GameModel {

    public GameSettings gameSettings;

    public GameModel() {
        gameSettings = new GameSettings();
    }

    public void setup() {
        reset();

        for(int i = 0; i < gameSettings.numberOfPlayers; i++) {

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
}
