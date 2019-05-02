package no.ntnu.idi.tdt4240.controller;
import java.util.ArrayList;
import java.util.List;

import no.ntnu.idi.tdt4240.model.TeamModel;
import no.ntnu.idi.tdt4240.model.GameModel;


public class GameController {
    public static GameController instance;
    private GameViewer viewer;
    private GameModel model;
    private boolean gameEnded = false;
    private int whoseTurn = 1; // Team ID starts from 1

    private List<TeamModel> teams = new ArrayList<TeamModel>();

    public GameController(GameViewer viewer, GameModel model) {
        instance = this;
        //-----------------
        // GAME START
        this.viewer = viewer;
        this.model = model;
        // Create teams


        //-----------------
        // GAME SETUP
        // TODO: roll dice to detemine who starts the game
        for (TeamModel team : teams) {
            // TODO: draw cards
            // TODO: place soldiers
        }

        //-----------------
        // GAME PHASE LOOP
        while (!gameEnded){
            //-----------------
            // ATTACK PHASE
            // set state to attack
            for (TeamModel team : teams) {
                if (!team.hasLost()){
                    //do something
                }
            }

            //-----------------
            // FORTIFY PHASE
            // set state to fortify
            for (TeamModel team : teams) {
                if (!team.hasLost()){
                    setWhoseTurn(team.ID);
                }

            }
            break;
        }

        //-----------------
        // GAME OVER
        // TODO: show stats and leaderboard
    }




    public void setNumberOfPlayers(int num) {
        model.gameSettings.setNumberOfPlayers(num);
        viewer.setNumberOfPlayers(model.gameSettings.getNumberOfPlayers());
    }
    public int getNumberOfPlayers() {return model.gameSettings.getNumberOfPlayers();}

    public int getWhoseTurn() {
        return whoseTurn;
    }

    public void setWhoseTurn(int whoseTurn) {
        this.whoseTurn = whoseTurn;
    }

    public void nextTurn(){
        whoseTurn++;
        if (whoseTurn > TeamModel.getTeamCount()){
            whoseTurn = 1; // starts from the first teams
            nextPhase();
        }
    }
    public void nextPhase(){
        // increment the phase
    }

    /*
    The RenderSystem should maybe just have its own camera? especially if
    the ECS handles its own inputs
    public void setCamera(Camera cam){
        //or w/e idk
        model.renderSystem.setCamera(cam);
    } */
}
