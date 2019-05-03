package no.ntnu.idi.tdt4240.model;

public class GameModel {
    public GameSettings gameSettings;

    private BoardModel boardModel;

    private boolean hasInit = false;

    public GameModel() {
        gameSettings = new GameSettings();

        TerritoryModel.init();
        boardModel = new BoardModel(TerritoryModel.getInstance());
    }

    public BoardModel getBoardModel() {
        return boardModel;
    }

    public void init() {
        if (hasInit)
            reset();

        boardModel.init();

        for (int i = 0; i < gameSettings.numberOfPlayers; i++) {

        }

        hasInit = true;
    }

    public void reset() {
        boardModel.reset();
    }

    public class GameSettings {
        private int numberOfPlayers;

        public int getNumberOfPlayers() {return numberOfPlayers;}

        public void setNumberOfPlayers(int num) {
            if (num > 6) {
                numberOfPlayers = 6;
            } else if (num < 2) {
                numberOfPlayers = 2;
            } else {
                numberOfPlayers = num;
            }
        }
    }
}
