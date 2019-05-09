package no.ntnu.idi.tdt4240.model;

/**
 * The GameModel class is the first entry point when clicking on the GameView.
 * Since a click is affected by Phase and the Board, the GameModel has a reference to both
 */
public class GameModel {
    public final GameSettings gameSettings;

    private final MultiplayerModel multiplayerModel;
    private final BoardModel boardModel;
    private final TroopModel troopModel;
    private final PhaseModel phaseModel;

    private boolean hasInit = false;

    public GameModel() {
        gameSettings = new GameSettings();

        multiplayerModel = new MultiplayerModel(8);
        boardModel = new BoardModel();
        troopModel = new TroopModel();
        phaseModel = new PhaseModel();
    }

    public MultiplayerModel getMultiplayerModel() {
        return multiplayerModel;
    }

    public BoardModel getBoardModel() {
        return boardModel;
    }

    public TroopModel getTroopModel() {
        return troopModel;
    }

    public PhaseModel getPhaseModel() {
        return phaseModel;
    }


    public void init() {
        if (hasInit)
            reset();

        // TODO: make all models singletons?
        TerritoryModel.init();

        multiplayerModel.init();
        boardModel.init();
        troopModel.init();

        for (int i = 0; i < gameSettings.numberOfPlayers; i++) {
            // TODO:
        }

        hasInit = true;
    }

    public void reset() {
        troopModel.reset();
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
