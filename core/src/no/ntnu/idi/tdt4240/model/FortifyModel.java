package no.ntnu.idi.tdt4240.model;

import no.ntnu.idi.tdt4240.data.Territory;

public class FortifyModel {
    /**
     * Move a specific number of troops from a territory to another.
     *
     * @param playerID This will be used to validate the move
     * @param from
     * @param to
     * @param n Number of troops to move
     *
     * @return A boolean indicating if the call was successful or not.
     */
    public static boolean move(int playerID, Territory from, Territory to, int n) {
        // no need to check if it is valid??
    /*if (!isMyTurn()){
        System.out.println("Not your turn yet!");
        return false;
    }

    // TODO: check if the team already moved or not ??

    */
        if (from.getOwnerID() != playerID || to.getOwnerID() != playerID) {
            System.out.println("You have selected a territory you don't own.");
            return false;
        }
        if (from.getNumTroops() == 1) {
            System.out.println("There are insufficient troops to move");
            return false;
        }

        // Perform fortification
        from.setNumTroops(from.getNumTroops() - n);
        to.setNumTroops(to.getNumTroops() + n);

        System.out.println("Moved " + n + " troops from t1 to t2");
        return true;
    }
}
