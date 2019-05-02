package no.ntnu.idi.tdt4240.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class PlayerModel {
    private int numPlayers;
    private final int[] Colors = new int[]{0xFE796F, 0xFECFFF, 0xF4FE6F, 0xACFE6F, 0x6FFEC1, 0xAB6FFE, 0xFE6FC2, 0xFEBB6F};
    private HashMap<Integer, Integer> Players = new HashMap();


    public PlayerModel(int numPlayers){
        this.numPlayers = numPlayers;
        assignPlayers();

    }


    private void assignPlayers(){
        for (int i = 0; i< numPlayers; i++){
            Players.put(i, Colors[i]);
        }
    }

    public HashMap<Integer, Integer> getPlayers() {
        return Players;
    }

    public static int getKeyFromValue(Map hm, int value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                int i = (int) o;
                return i;
            }
        }
        return 1337;
    }

    public HashMap<Integer, Integer> randomizePlayerPriority(){
        HashMap<Integer, Integer> playersWithPriority = new HashMap<>();
        int roll;
        boolean keepRolling;
        for (int i = 0; i < numPlayers; i++){
            keepRolling = true;
            while (keepRolling) {
                roll = rollForPriority();
                if (!playersWithPriority.containsValue(roll)){
                    playersWithPriority.put(i, roll);
                    keepRolling = false;
                }

            }

        }

        return playersWithPriority;
    }

    public int rollForPriority() {
        Random ran = new Random();
        int die = ran.nextInt(15);
        return die;
    }
}


