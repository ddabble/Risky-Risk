package no.ntnu.idi.tdt4240;

public interface IGPGSClient {
    void startSignInIntent();

    void signOut();

    String getPlayerDisplayName();

    void onStartMatchClicked();
}
