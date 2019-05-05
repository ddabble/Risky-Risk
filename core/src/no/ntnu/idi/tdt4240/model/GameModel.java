package no.ntnu.idi.tdt4240.model;

/* The GameModel class is the first entry point when clicking on the GameView.
    Since a click is affected by Phase and the Board, the GameModel manages both
 */

import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.tdt4240.data.Territory;

public class GameModel {
    public final GameSettings gameSettings;

    private final PlayerModel playerModel;
    private final BoardModel boardModel;
    private PhaseModel phaseModel;
    private Territory selectedTerritory;

    private boolean hasInit = false;

    public GameModel() {
        gameSettings = new GameSettings();

        TerritoryModel.init();
        playerModel = new PlayerModel(TerritoryModel.getInstance(), 8);
        boardModel = new BoardModel(TerritoryModel.getInstance(), playerModel);
        phaseModel = new PhaseModel();
    }

    public BoardModel getBoardModel() {
        return boardModel;
    }

    public PhaseModel getPhaseModel() {
        return phaseModel;
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

    public Territory getSelectedTerritory() {
        return this.selectedTerritory;
    }


    public void onClickTerritory(Vector2 touchWorldPos) {
        Vector2 mapPos = boardModel.worldPosToMapTexturePos(touchWorldPos, boardModel.getMapSprite());
        Territory territory = boardModel.getTerritory(mapPos);
        if (territory != null) {
            System.out.println(territory.name);
            System.out.println("\tOwnerID: " + territory.getOwnerID());
            System.out.println("\tNumber of Troops: " + territory.getNumTroops());

            // Update territory based on the phase we are in
            phaseModel.getPhase().territoryClicked(territory);
            this.selectedTerritory = territory;
        } else {
            System.out.println("None");
            this.selectedTerritory = null;
        }
    }
}
