package no.ntnu.idi.tdt4240.desktop.client;

import no.ntnu.idi.tdt4240.client.IGPGSClient;
import no.ntnu.idi.tdt4240.client.IRiskyTurn;

// TODO: make this work properly
public class DesktopMockGPGSClient implements IGPGSClient {
    private IRiskyTurn turnData;

    public DesktopMockGPGSClient() {
        turnData = new DesktopRiskyTurn();
    }

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
        return turnData;
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
