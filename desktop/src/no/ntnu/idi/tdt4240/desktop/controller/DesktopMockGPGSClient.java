package no.ntnu.idi.tdt4240.desktop.controller;

import no.ntnu.idi.tdt4240.controller.IGPGSClient;
import no.ntnu.idi.tdt4240.controller.IRiskyTurn;
import no.ntnu.idi.tdt4240.util.TerritoryMap;

// TODO: make this work properly
public class DesktopMockGPGSClient implements IGPGSClient {
    @Override
    public boolean matchActive() {
        return false;
    }

    @Override
    public void pauseGame() {

    }

    @Override
    public void setMatchNotActive() {

    }

    @Override
    public boolean isDoingTurn() {
        return false;
    }

    @Override
    public IRiskyTurn getmRiskyTurn() {
        return new IRiskyTurn() {
            @Override
            public void getTerritoryMapData(TerritoryMap map) {

            }

            @Override
            public int getTurnCounter() {
                return 0;
            }

            @Override
            public void updateData(TerritoryMap map, int currentPlayer) {

            }

            @Override
            public boolean isDataInitialized() {
                return false;
            }

            @Override
            public int getNumPlayers() {
                return 2;
            }

            @Override
            public void setNumPlayers(int numPlayers) {

            }

            @Override
            public int getCurrentPlayer() {
                return 0;
            }

            @Override
            public void persistNumPlayers() {

            }
        };
    }

    @Override
    public void startSignInIntent() {

    }

    @Override
    public void setSignInAttemptHandler(SignInAttemptHandler handler) {

    }

    @Override
    public void setMatchDataReceivedHandler(MatchDataReceivedHandler handler) {

    }

    @Override
    public void setGameUIStartHandler(GameUIStartHandler handler) {

    }

    @Override
    public boolean isSignedIn() {
        return false;
    }

    @Override
    public void signOut() {

    }

    @Override
    public void onStartMatchClicked() {

    }

    @Override
    public void onCheckGamesClicked() {

    }

    @Override
    public void onDoneClicked() {

    }

    @Override
    public void onCancelClicked() {

    }

    @Override
    public void onLeaveClicked() {

    }

    @Override
    public void onFinishClicked() {

    }
}
