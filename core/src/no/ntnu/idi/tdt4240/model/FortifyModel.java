package no.ntnu.idi.tdt4240.model;


public class FortifyModel {

    /**
     * Move a specific number of troops from a territory to another.
     * @param from
     * @param to
     * @param n     Number of troops to move
     * @return      A boolean indicating if the call was successful or not.
     */
    public static boolean move(TeamModel team, Territory from, Territory to, int n){

        // no need to check if it is valid??
        /*if (!isMyTurn()){
            System.out.println("Not your turn yet!");
            return false;
        }

        // TODO: check if the team already moved or not ??

        */
        if (from.getOwnerID() != team.ID || to.getOwnerID() != team.ID){
            System.out.println("You have selected a territory you don't own.");
            return false;
        }
        if (from.getNumTroops() == 1){
            System.out.println("There are insufficient troops to move");
            return false;
        }

        // Perform fortification
        from.setNumTroops(from.getNumTroops() - n);
        to.setNumTroops(to.getNumTroops() + n);

        return true;
    }
}
