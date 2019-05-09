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
    private final Player playerModel;
    private final TurnModel turnModel;
    private final GameView view;
    private final PhaseView phaseView;
    private final BoardView boardView;
    private final TroopView troopView;

    private final PhaseController phaseController;

    public GameController(RiskyRisk game) {
        model = new GameModel();
        boardModel = model.getBoardModel();
        troopModel = model.getTroopModel();
        phaseModel = model.getPhaseModel();
        playerModel = model.getPlayerModel();
        turnModel = model.getTurnModel();

        view = new GameView(this, game);
        phaseView = view.getPhaseView();
        boardView = view.getBoardView();
        troopView = view.getTroopView();

        phaseController = new PhaseController(model, view);

    }

    public int getPlayerColor(int playerID) {
        return model.getMultiplayerModel().getPlayerColor(playerID);
    }

    public Player getPlayer(){ return playerModel; }
    public Territory getSelectedTerritory() {
        return troopModel.getSelectedTerritory();
    }

    public void setSelectedTerritory(Territory territory) {
        troopModel.setSelectedTerritory(territory);
    }

    public void nextTurnButtonClicked(){
        phaseController.nextTurnButtonClicked();
        updatePhase();
        setSelectedTerritory(null);
        phaseView.onSelectedTerritoriesChange(null, null);


    }

    public void nextPhaseButtonClicked() {
        phaseController.clearRenderedButtons();
        phaseModel.nextPhase();
        if (phaseModel.getPhase().getName() == "Fortify"){
            phaseView.addTurnButton();
        }
        updatePhase();
        setSelectedTerritory(null);
        phaseView.onSelectedTerritoriesChange(null, null);

    }

    public void cancelButtonClicked(){
        if (phaseModel.getPhase().getName() == "Attack"){
            playerModel.cancelAttack();
            phaseController.clearRenderedButtons();
            phaseView.onSelectedTerritoriesChange(null, null);
        }
        if (phaseModel.getPhase().getName() == "Fortify"){
            phaseController.cancelFortify();
            phaseView.onSelectedTerritoriesChange(null, null);
        }

    }

    public void attackButtonClicked(){
        phaseController.attackButtonClicked();
    }

    public void fortifyButtonClicked(){
        phaseController.fortifyButtonClicked();
    }
    public void boardClicked(Vector2 touchWorldPos) {
        Vector2 mapPos = boardView.worldPosToMapTexturePos(touchWorldPos);
        Territory territory = boardModel.getTerritory(mapPos);
        troopView.onSelectTerritory(territory);
        if (territory != null) {
            System.out.println(territory.name);

            // Update territory based on the phase we are in
            //phaseModel.getPhase().territoryClicked(territory);
            phaseController.territoryClicked(territory);
            //troopView.onTerritoryChangeNumTroops(territory);
        } else
            System.out.println("None");
    }

    @Override
    public void show() {
        model.init();
        view.show(boardModel.getMapTexture(), troopModel.getCircleTexture(), troopModel.getCircleSelectTexture());
        updatePhase();
        phaseController.updateRenderedCurrentPlayer();
    }

    public void updatePhase() {
        String curPhase = phaseModel.getPhase().getName();
        String nextPhase = phaseModel.getPhase().next().getName();
        view.updatePhase(curPhase, nextPhase);
        if (phaseModel.getPhase().getName() == "Place"){
            phaseController.updateTroopsToPlace();
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
