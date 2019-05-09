package no.ntnu.idi.tdt4240.controller;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.data.Player;
import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.model.BattleModel;
import no.ntnu.idi.tdt4240.model.BoardModel;
import no.ntnu.idi.tdt4240.model.GameModel;
import no.ntnu.idi.tdt4240.model.PhaseModel;
import no.ntnu.idi.tdt4240.model.TerritoryModel;
import no.ntnu.idi.tdt4240.model.TroopModel;
import no.ntnu.idi.tdt4240.model.TurnModel;
import no.ntnu.idi.tdt4240.view.BoardView;
import no.ntnu.idi.tdt4240.view.GameView;
import no.ntnu.idi.tdt4240.view.PhaseView;
import no.ntnu.idi.tdt4240.view.TroopView;

public class GameController implements Screen {
    private final GameModel model;
    private final BoardModel boardModel;
    private final TroopModel troopModel;
    private final PhaseModel phaseModel;
    private final Player player;
    private final TurnModel turn;
    private final GameView view;
    private final PhaseView phaseView;
    private final BoardView boardView;
    private final TroopView troopView;

    public GameController(RiskyRisk game) {
        model = new GameModel();
        boardModel = model.getBoardModel();
        troopModel = model.getTroopModel();
        phaseModel = model.getPhaseModel();

        view = new GameView(this, game);
        phaseView = view.getPhaseView();
        boardView = view.getBoardView();
        troopView = view.getTroopView();

        player = new Player();

        //Temporary first player ID
        turn = new TurnModel(1, 7);
    }

    public int getPlayerColor(int playerID) {
        return model.getMultiplayerModel().getPlayerColor(playerID);
    }

    public Color getPlayerRGBAColor(int playerID){
        return model.getMultiplayerModel().getPlayerRGBAColor(playerID);
    }

    public Player getPlayer(){ return player; }
    public Territory getSelectedTerritory() {
        return troopModel.getSelectedTerritory();
    }

    public void setSelectedTerritory(Territory territory) {
        troopModel.setSelectedTerritory(territory);
    }

    public void nextTurnButtonClicked(){
        turn.takeTurn();
        phaseView.updateRenderedCurrentPlayer(turn.getCurrentPlayerID(), getPlayerRGBAColor(turn.getCurrentPlayerID()));
        phaseView.removeTurnButton();
        phaseModel.nextPhase();
        updatePhase();
        setSelectedTerritory(null);
    }


    public void nextPhaseButtonClicked() {
        phaseModel.nextPhase();
        if (phaseModel.getPhase().getName() == "Fortify"){
            phaseView.addTurnButton();
        }
        updatePhase();
        setSelectedTerritory(null);
    }

    public void cancelButtonClicked(){
        player.cancelAttack();
        phaseView.removeAttackButton();
        phaseView.removeCancelButton();
    }

    public void attackButtonClicked(){
        int[] winner = BattleModel.fight(player.getFromTerritory().getOwnerID(),
                player.getToTerritory().getOwnerID(),
                player.getFromTerritory().getNumTroops() - 1,
                player.getToTerritory().getNumTroops());
        phaseView.removeCancelButton();
        phaseView.removeAttackButton();
        player.getToTerritory().setOwnerID(winner[0]);
        player.getToTerritory().setNumTroops(winner[1]);
        player.getFromTerritory().setNumTroops(1);
        troopView.onTerritoryChangeNumTroops(player.getFromTerritory());
        troopView.onTerritoryChangeNumTroops(player.getToTerritory());
        boardView.initColorLookupArray(TerritoryModel.getInstance().TERRITORY_MAP);
        //Clears the attack HashMap after the attack has gone through.
        player.cancelAttack();
        System.out.println(winner.toString());
    }

    public void boardClicked(Vector2 touchWorldPos) {
        Vector2 mapPos = boardView.worldPosToMapTexturePos(touchWorldPos);
        Territory territory = boardModel.getTerritory(mapPos);
        troopView.onSelectTerritory(territory);
        if (territory != null) {
            System.out.println(territory.name);

            // Update territory based on the phase we are in
            if (phaseModel.getPhase().getName() == "Place") {
                if (player.getTroopsToPlace() > 0 && territory.getOwnerID() == turn.getCurrentPlayerID()) {
                    phaseModel.getPhase().territoryClicked(territory);
                    player.setTroopsToPlace(player.getTroopsToPlace() - 1);
                    troopView.onTerritoryChangeNumTroops(territory);
                    phaseView.updateRenderedVariables(phaseModel.getPhase().getName());
                } else {
                    System.out.println("No troops left to place");
                }
            }
            if (phaseModel.getPhase().getName() == "Attack"){
                //Temporary playerID

                if (territory.getOwnerID() == turn.getCurrentPlayerID() && territory.getNumTroops() > 1){
                    if (player.getAttack() == null || player.getAttack().size() == 0){
                        player.setAttackFrom(territory);
                        phaseView.addCancelButton();
                    }
                }
                if (player.getAttack() != null){
                    if (player.getAttack().size() == 1 && territory.getOwnerID() != turn.getCurrentPlayerID()){
                        System.out.println(player.getFromTerritory().getNeighbors().toString());
                        if (player.getFromTerritory().getNeighbors().contains(territory)){
                            player.setAttackTo(territory);
                            phaseView.addAttackButton();
                        }
                    }
                }
            }
        } else
            System.out.println("None");
    }

    @Override
    public void show() {
        model.init();
        view.show(boardModel.getMapTexture(), troopModel.getCircleTexture(), troopModel.getCircleSelectTexture());
        updatePhase();
        phaseView.updateRenderedCurrentPlayer(turn.getCurrentPlayerID(), getPlayerRGBAColor(turn.getCurrentPlayerID()));
    }

    public void updatePhase() {
        String curPhase = phaseModel.getPhase().getName();
        String nextPhase = phaseModel.getPhase().next().getName();
        view.updatePhase(curPhase, nextPhase);
        if (phaseModel.getPhase().getName() == "Place"){
            List<Territory> territories = TerritoryModel.getInstance().TERRITORY_MAP.getAllTerritories();
            int territoriesOwned = 0;
            for (Territory territory : territories)
                // Temporary ID Check
                if (territory.getOwnerID() ==  turn.getCurrentPlayerID())
                    territoriesOwned++;
            if (territoriesOwned == 0){
                turn.takeTurn();
                updatePhase();

            }
            else{
                player.setTroopsToPlace(territoriesOwned);
                phaseView.updateRenderedVariables("Place");
            }

        }



    }

    @Override
    public void render(float delta) {
        view.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        view.resize(width, height);
    }

    @Override
    public void pause() {
        view.pause();
    }

    @Override
    public void resume() {
        view.resume();
    }

    @Override
    public void hide() {
        view.hide();
        model.reset();
    }

    @Override
    public void dispose() {
        view.dispose();
    }

    /*
    public void setNumberOfPlayers(int num) {
        model.gameSettings.setNumberOfPlayers(num);
        view.setNumberOfPlayers(model.gameSettings.getNumberOfPlayers());
    }

    public int getNumberOfPlayers() {return model.gameSettings.getNumberOfPlayers();}

    // The role of the controller is to translate inputs into changes and relay this back
    // Below is the translation of clicks to model changes
    */
}
