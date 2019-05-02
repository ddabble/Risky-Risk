package no.ntnu.idi.tdt4240.controller;

import no.ntnu.idi.tdt4240.model.TeamModel;
import no.ntnu.idi.tdt4240.model.Territory;

/**
 * Since this is mulitplayer game, we need this class to handle user inputs
 */

public class TeamController {
    private TeamModel team;

    public TeamController(){
        team = new TeamModel();
    }

    void Attack(Territory from, Territory to){
        if (!isMyTurn())
            System.out.println("Not your turn yet!");
    }


    /**
     * Called when the user clicks the next button, mean the player wants to end his turn
     * @return      A boolean indicating if the call was successful or not.
     */
    public boolean endTurn(){
        // TODO: Check if everything is good in every phase.
        // TODO: Check if all troops in hand and place on the board.
        return false;
    }



    private boolean isMyTurn(){
        return (GameController.instance.getWhoseTurn() == team.ID);
    }
}
