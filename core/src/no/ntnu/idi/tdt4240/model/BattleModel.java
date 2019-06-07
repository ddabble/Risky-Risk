package no.ntnu.idi.tdt4240.model;

import java.util.Arrays;
import java.util.Random;

public class BattleModel {
    public static int[] fight(int attackerID, int defenderID, int attackerTroops, int defenderTroops) {
        int[] attDice;
        int[] defDice;
        int[] winner = new int[2];
        winner[0] = 1337;
        while (winner[0] == 1337) {
            if (defenderTroops == 0) {
                winner[0] = attackerID;
                winner[1] = attackerTroops;
                return winner;
            }
            if (attackerTroops >= 3) {
                attDice = rollDice(new int[3]);
            } else {
                attDice = rollDice(new int[attackerTroops]);
            }
            if (defenderTroops > 2) {
                defDice = rollDice(new int[2]);
            } else {
                defDice = rollDice(new int[1]);
            }
            Arrays.sort(defDice);
            Arrays.sort(attDice);

            if (mostDice(attDice, defDice) == 1) {
                System.out.println("attTroops: " + attackerTroops + " attRoll: " + attDice[attDice.length - 1]
                                   + " defTroops: " + defenderTroops + " defRoll: " + defDice[defDice.length - 1]);

                if (attDice[attDice.length - 1] > defDice[defDice.length - 1]) {
                    defenderTroops--;
                    if (defenderTroops == 0) {
                        winner[0] = attackerID;
                        winner[1] = attackerTroops;
                        break;
                    }
                } else if (attDice[attDice.length - 1] <= defDice[defDice.length - 1]) {
                    attackerTroops--;
                    if (attackerTroops == 0) {
                        winner[0] = defenderID;
                        winner[1] = defenderTroops;
                        break;
                    }
                }
            }
            // Max of 2 fights per dice roll.
            else {
                for (int roll = 1; roll < mostDice(attDice, defDice) + 1; roll++) {
                    System.out.println("attTroops: " + attackerTroops + " attRoll: " + attDice[attDice.length - roll]
                                       + " defTroops: " + defenderTroops + " defRoll: " + defDice[defDice.length - roll]);

                    if (attDice[attDice.length - roll] > defDice[defDice.length - roll]) {
                        defenderTroops--;
                        if (defenderTroops == 0) {
                            winner[0] = attackerID;
                            winner[1] = attackerTroops;
                            break;
                        }
                    } else if (attDice[attDice.length - roll] <= defDice[defDice.length - roll]) {
                        attackerTroops--;
                        if (attackerTroops == 0) {
                            winner[0] = defenderID;
                            winner[1] = defenderTroops;
                            break;
                        }
                    }
                }
            }
        }
        return winner;
    }

    private static int mostDice(int[] attDice, int[] defDice) {
        return Math.min(Math.min(attDice.length, defDice.length), 2);
    }

    private static int[] rollDice(int[] dice) {
        Random ran = new Random();
        for (int i = 0; i < dice.length; i++) {
            dice[i] = ran.nextInt(6) + 1;
        }
        System.out.println("rolling");
        return dice;
    }
}
