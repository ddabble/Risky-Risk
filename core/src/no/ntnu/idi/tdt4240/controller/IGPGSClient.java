package no.ntnu.idi.tdt4240.controller;

public interface IGPGSClient {

    // Check if a match is active so we can enter game view
    boolean matchActive();
    void pauseGame();
    boolean isDoingTurn();

    IRiskyTurn getmRiskyTurn();

    void startSignInIntent();
    boolean isSignedIn();
    void signOut();

    void onStartMatchClicked();
    void onCheckGamesClicked();


    void onDoneClicked(String data);
    void onCancelClicked();
    void onLeaveClicked();
    void onFinishClicked();
}
