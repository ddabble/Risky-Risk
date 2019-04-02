package no.ntnu.idi.tdt4240.Controllers;

import no.ntnu.idi.tdt4240.Models.GameClasses.TeamModel;
import no.ntnu.idi.tdt4240.Models.GameClasses.Territory;
import no.ntnu.idi.tdt4240.RiskyRisk;

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
     * Move a specific number of troops from a territory to another.
     * @param from
     * @param to
     * @param n     Number of troops to move
     * @return      A boolean indicating if the call was successful or not.
     */
    public boolean Foritfy(Territory from, Territory to, int n){

        // no need to check if it is valid??
        if (!isMyTurn()){
            System.out.println("Not your turn yet!");
            return false;
        }
        if (from.getOwner() != team.ID || to.getOwner() != team.ID){
            System.out.println("You have selected a territory you don't own.");
            return false;
        }
        if (from.getTroops() == 1){
            System.out.println("There are insufficient troops to move");
            return false;
        }

        // Perform fortification
        from.setTroops(from.getTroops()-n);
        to.setTroops(to.getTroops()+n);

        return true;
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
