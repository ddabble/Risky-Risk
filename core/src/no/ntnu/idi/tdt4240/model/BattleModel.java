package no.ntnu.idi.tdt4240.model;

import java.util.Arrays;
import java.util.Random;

public class BattleModel {
    public static int[] fight(int attackerID, int defenderID, int attackerTroops, int defenderTroops) {
        int[] attdies;
        int[] defdies;
        int[] winner = new int[2];
        winner[0] = 1337;
        while (winner[0] == 1337) {
            if (defenderTroops == 0){
                winner[0] = attackerID;
                winner[1] = attackerTroops;
                return winner;
            }
            if (attackerTroops >= 3) {
                attdies = rollDies(new int[3]);
            } else {
                attdies = rollDies(new int[attackerTroops]);
            }
            if (defenderTroops >= 2) {
                defdies = rollDies(new int[2]);
            } else {
                defdies = rollDies(new int[defenderTroops]);
            }
            Arrays.sort(defdies);
            Arrays.sort(attdies);
            if (mostDies(attdies, defdies) == 1) {
                System.out.println("attTroops: " + attackerTroops + " attRoll: " + attdies[attdies.length - 1] +
                                   " defTroops: " + defenderTroops + " defRoll: " + defdies[defdies.length - 1]);

                if (attdies[attdies.length - 1] > defdies[defdies.length - 1]) {
                    defenderTroops--;
                    if (defenderTroops == 0) {
                        winner[0] = attackerID;
                        winner[1] = attackerTroops;
                        break;
                    }
                } else if (attdies[attdies.length - 1] <= defdies[defdies.length - 1]) {
                    attackerTroops--;
                    if (attackerTroops == 0) {
                        winner[0] = defenderID;
                        winner[1] = defenderTroops;
                        break;
                    }
                }
            }
            for (int roll = 1; roll < mostDies(attdies, defdies); roll++) {
                System.out.println("attTroops: " + attackerTroops + " attRoll: " + attdies[attdies.length - roll] +
                                   " defTroops: " + defenderTroops + " defRoll: " + defdies[defdies.length - roll]);

                if (attdies[attdies.length - roll] > defdies[defdies.length - roll]) {
                    defenderTroops--;
                    if (defenderTroops == 0) {
                        winner[0] = attackerID;
                        winner[1] = attackerTroops;
                        break;
                    }
                } else if (attdies[attdies.length - roll] <= defdies[defdies.length - roll]) {
                    attackerTroops--;
                    if (attackerTroops == 0) {
                        winner[0] = defenderID;
                        winner[1] = defenderTroops;
                        break;
                    }
                }
            }
        }
        return winner;
    }

    private static int mostDies(int[] attdies, int[] defdies) {
        //System.out.println("mostDies: " + Math.min(Math.max(attdies.length, defdies.length), 2));
        return Math.min(Math.max(attdies.length, defdies.length), 1);
    }

    private static int[] rollDies(int[] dies) {
        Random ran = new Random();
        for (int i = 0; i < dies.length; i++) {
            dies[i] = ran.nextInt(6) + 1;
        }
        return dies;
    }
}
