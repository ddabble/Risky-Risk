package no.ntnu.idi.tdt4240;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.GamesClientStatusCodes;
import com.google.android.gms.games.InvitationsClient;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.TurnBasedMultiplayerClient;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.InvitationCallback;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchUpdateCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;

import no.ntnu.idi.tdt4240.controller.IGPGSClient;
import no.ntnu.idi.tdt4240.controller.IRiskyTurn;
import no.ntnu.idi.tdt4240.model.SettingsModel;

public class GPGSClient implements IGPGSClient {
    private static final String TAG = "GPGS";
    // For our intents
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_SELECT_PLAYERS = 10000;
    private static final int RC_LOOK_AT_MATCHES = 10001;

    //For popups
    private View mView;

    // Client used to sign in with Google APIs
    private GoogleSignInClient mGoogleSignInClient = null;
    //function for handling a sign-in attempt
    private SignInAttemptHandler signInAttemptHandler;
    //function for handling incoming match data
    private MatchDataReceivedHandler matchDataReceivedHandle;
    //function for starting up the game ui when we receive a match object
    private GameUIStartHandler gameUIStartHandler;

    // Client used to interact with the TurnBasedMultiplayer system.
    private TurnBasedMultiplayerClient mTurnBasedMultiplayerClient = null;

    // Client used to interact with the Invitation system.
    private InvitationsClient mInvitationsClient = null;

    private AlertDialog mAlertDialog;

    // Should I be showing the turn API?
    private boolean isDoingTurn = false;

    // This is the current match we're in; null if not loaded
    private TurnBasedMatch mMatch;
    private Activity mActivity;

    // This is the current match data after being unpersisted.
    // Do not retain references to match data once you have
    // taken an action on the match, such as takeTurn()
    private RiskyTurn mTurnData;
    private boolean matchActive = false;

    private String mDisplayName;
    private String mPlayerId;

    public GPGSClient(Activity activity, View view) {
        // Create the Google API Client with access to Games
        // Create the client used to sign in.
        mActivity = activity;
        mView = view;
        mGoogleSignInClient = GoogleSignIn.getClient(mActivity, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
    }

    protected void onResume() {
        // Since the state of the signed in user can change when the activity is not active
        // it is recommended to try and sign in silently from when the app resumes.
        signInSilently();
    }

    protected void onPause() {
        // Unregister the invitation callbacks; they will be re-registered via
        // onResume->signInSilently->onConnected.
        if (mInvitationsClient != null) {
            mInvitationsClient.unregisterInvitationCallback(mInvitationCallback);
        }

        if (mTurnBasedMultiplayerClient != null) {
            mTurnBasedMultiplayerClient.unregisterTurnBasedMatchUpdateCallback(mMatchUpdateCallback);
        }
    }

    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        String name = googleSignInAccount.getId();
        Log.d(TAG, "onConnected(): " + name + " connected to Google APIs");

        mTurnBasedMultiplayerClient = Games.getTurnBasedMultiplayerClient(mActivity, googleSignInAccount);
        mInvitationsClient = Games.getInvitationsClient(mActivity, googleSignInAccount);

        Games.getPlayersClient(mActivity, googleSignInAccount)
             .getCurrentPlayer()
             .addOnSuccessListener(
                     new OnSuccessListener<Player>() {
                         @Override
                         public void onSuccess(Player player) {
                             mDisplayName = player.getDisplayName();
                             mPlayerId = player.getPlayerId();

                             setViewVisibility();
                         }
                     }
             )
             .addOnFailureListener(createFailureListener("There was a problem getting the player!"));

        Log.d(TAG, "onConnected(): Connection successful");

        // Retrieve the TurnBasedMatch from the connectionHint
        GamesClient gamesClient = Games.getGamesClient(mActivity, googleSignInAccount);

        //Popups
        gamesClient.setGravityForPopups(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        gamesClient.setViewForPopups(mView);

        gamesClient.getActivationHint()
                   .addOnSuccessListener(new OnSuccessListener<Bundle>() {
                       @Override
                       public void onSuccess(Bundle hint) {
                           if (hint != null) {
                               TurnBasedMatch match = hint.getParcelable(Multiplayer.EXTRA_TURN_BASED_MATCH);

                               if (match != null) {
                                   updateMatch(match);
                               }
                           }
                       }
                   })
                   .addOnFailureListener(createFailureListener(
                           "There was a problem getting the activation hint!"));

        setViewVisibility();

        // As a demonstration, we are registering this activity as a handler for
        // invitation and match events.

        // This is *NOT* required; if you do not register a handler for
        // invitation events, you will get standard notifications instead.
        // Standard notifications may be preferable behavior in many cases.
        mInvitationsClient.registerInvitationCallback(mInvitationCallback);

        // Likewise, we are registering the optional MatchUpdateListener, which
        // will replace notifications you would get otherwise. You do *NOT* have
        // to register a MatchUpdateListener.
        mTurnBasedMultiplayerClient.registerTurnBasedMatchUpdateCallback(mMatchUpdateCallback);
    }

    private void onDisconnected() {
        Log.d(TAG, "onDisconnected()");

        mTurnBasedMultiplayerClient = null;
        mInvitationsClient = null;

        setViewVisibility();
    }

    // This is a helper function that will do all the setup to create a simple failure message.
    // Add it to any task and in the case of an failure, it will report the string in an alert
    // dialog.
    private OnFailureListener createFailureListener(final String string) {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handleException(e, string);
            }
        };
    }

    // Displays your inbox. You will get back onActivityResult where
    // you will need to figure out what you clicked on.
    public void onCheckGamesClicked() {
        mTurnBasedMultiplayerClient.getInboxIntent()
                                   .addOnSuccessListener(new OnSuccessListener<Intent>() {
                                       @Override
                                       public void onSuccess(Intent intent) {
                                           mActivity.startActivityForResult(intent, RC_LOOK_AT_MATCHES);
                                       }
                                   })
                                   .addOnFailureListener(createFailureListener("Error with get_inbox_intent"));
    }

    // Open the create-game UI. You will get back an onActivityResult
    // and figure out what to do.
    public void onStartMatchClicked() {//View view) {
        mTurnBasedMultiplayerClient.getSelectOpponentsIntent(SettingsModel.MIN_NUM_PLAYERS - 1, SettingsModel.MAX_NUM_PLAYERS - 1, true)
                                   .addOnSuccessListener(new OnSuccessListener<Intent>() {
                                       @Override
                                       public void onSuccess(Intent intent) {
                                           mActivity.startActivityForResult(intent, RC_SELECT_PLAYERS);
                                       }
                                   })
                                   .addOnFailureListener(createFailureListener("Error with get_select_opponents"));
    }

    public void onCancelClicked() {
        mTurnBasedMultiplayerClient.cancelMatch(mMatch.getMatchId())
                                   .addOnSuccessListener(new OnSuccessListener<String>() {
                                       @Override
                                       public void onSuccess(String matchId) {
                                           onCancelMatch(matchId);
                                       }
                                   })
                                   .addOnFailureListener(createFailureListener("There was a problem cancelling the match!"));

        isDoingTurn = false;
        matchActive = false;
        //setViewVisibility();
    }

    // Leave the game during your turn. Note that there is a separate
    // mTurnBasedMultiplayerClient.leaveMatch() if you want to leave NOT on your turn.
    public void onLeaveClicked() {
        String nextParticipantId = getNextParticipantId();

        mTurnBasedMultiplayerClient.leaveMatchDuringTurn(mMatch.getMatchId(), nextParticipantId)
                                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {
                                           onLeaveMatch();
                                       }
                                   })
                                   .addOnFailureListener(createFailureListener("There was a problem leaving the match!"));

        isDoingTurn = false;
        matchActive = false;
        //setViewVisibility();
    }

    // Finish the game. Sometimes, this is your only choice.
    public void onFinishClicked() {
        mTurnBasedMultiplayerClient.finishMatch(mMatch.getMatchId())
                                   .addOnSuccessListener(new OnSuccessListener<TurnBasedMatch>() {
                                       @Override
                                       public void onSuccess(TurnBasedMatch turnBasedMatch) {
                                           onUpdateMatch(turnBasedMatch);
                                       }
                                   })
                                   .addOnFailureListener(createFailureListener("There was a problem finishing the match!"));

        isDoingTurn = false;
        matchActive = false;
        //setViewVisibility();
    }

    // Upload your new gamestate, then take a turn, and pass it on to the next
    // player.
    public void onDoneClicked() {
        String nextParticipantId = getNextParticipantId();
        // Create the next turn
        mTurnData.turnCounter += 1;

        //We update the turn object from the PhasePresenter (which call the BoardModel, which updates RiskyTurn)
        //so we don't have to do it here
        //mTurnData.data = null; //changed from string to null as mTurnData no longer uses string to hold data

        mTurnBasedMultiplayerClient.takeTurn(mMatch.getMatchId(),
                                             mTurnData.persist(), nextParticipantId)
                                   .addOnSuccessListener(new OnSuccessListener<TurnBasedMatch>() {
                                       @Override
                                       public void onSuccess(TurnBasedMatch turnBasedMatch) {
                                           onUpdateMatch(turnBasedMatch);
                                       }
                                   })
                                   .addOnFailureListener(createFailureListener("There was a problem taking a turn!"));

        mTurnData = null;
    }

    // Sign-in, Sign out behavior

    // Update the visibility based on what state we're in.
    private void setViewVisibility() {
        boolean isSignedIn = mTurnBasedMultiplayerClient != null;

        if (!isSignedIn) {
            if (mAlertDialog != null) {
                mAlertDialog.dismiss();
            }
            return;
        }
    }

    // Switch to gameplay view.
    private void setGameplayUI() {
        isDoingTurn = true;
        setViewVisibility();
        //give a callback to the game, if one is registered
        if (gameUIStartHandler != null) {
            gameUIStartHandler.onGameUIStart();
        }
    }

    // Generic warning/info dialog
    private void showWarning(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);

        // set title
        alertDialogBuilder.setTitle(title).setMessage(message);

        // set dialog message
        alertDialogBuilder.setCancelable(false)
                          .setPositiveButton(
                                  "OK",
                                  new DialogInterface.OnClickListener() {
                                      @Override
                                      public void onClick(DialogInterface dialog, int id) {
                                          // if this button is clicked, close
                                          // current activity
                                      }
                                  });

        // create alert dialog
        mAlertDialog = alertDialogBuilder.create();

        // show it
        mAlertDialog.show();
    }

    // Rematch dialog
    private void askForRematch() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);

        alertDialogBuilder.setMessage("Do you want a rematch?");

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(
                        "Sure, rematch!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                rematch();
                            }
                        })
                .setNegativeButton(
                        "No.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {}
                        });

        alertDialogBuilder.show();
    }

    // Check if already signed in (synh)
    public boolean isSignedIn() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mActivity);
        boolean hasPermissions = GoogleSignIn.hasPermissions(account);

        boolean isSignedIn = account != null && hasPermissions;

        if (isSignedIn) {
            Log.d(TAG, "isSignedin(): already signed in");
            signInSilently();
        }
        return isSignedIn;
    }

    public void setSignInAttemptHandler(SignInAttemptHandler signInAttemptHandler) {
        this.signInAttemptHandler = signInAttemptHandler;
    }

    public void setMatchDataReceivedHandler(MatchDataReceivedHandler matchDataReceivedHandler) {
        this.matchDataReceivedHandle = matchDataReceivedHandler;
    }

    public void setGameUIStartHandler(GameUIStartHandler gameUIStartHandler) {
        this.gameUIStartHandler = gameUIStartHandler;
    }

    /**
     * Start a sign in activity.  To properly handle the result, call tryHandleSignInResult from
     * your Activity's onActivityResult function
     */
    public void startSignInIntent() {
        mActivity.startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    /**
     * Try to sign in without displaying dialogs to the user.
     * <p>
     * If the user has already signed in previously, it will not show dialog.
     */
    private void signInSilently() {
        Log.d(TAG, "signInSilently()");

        mGoogleSignInClient.silentSignIn().addOnCompleteListener(
                mActivity,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInSilently(): success");
                            onConnected(task.getResult());
                        } else {
                            Log.d(TAG, "signInSilently(): failure", task.getException());
                            onDisconnected();
                        }
                    }
                });
    }

    public void signOut() {
        Log.d(TAG, "signOut()");

        mGoogleSignInClient.signOut().addOnCompleteListener(
                mActivity,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "signOut(): success");
                        } else {
                            handleException(task.getException(), "signOut() failed!");
                        }

                        onDisconnected();
                    }
                });
    }

    /**
     * Since a lot of the operations use tasks, we can use a common handler for whenever one fails.
     *
     * @param exception The exception to evaluate.  Will try to display a more descriptive reason for
     * the exception.
     * @param details Will display alongside the exception if you wish to provide more details for
     * why the exception happened
     */
    private void handleException(Exception exception, String details) {
        int status = 0;

        if (exception instanceof TurnBasedMultiplayerClient.MatchOutOfDateApiException) {
            TurnBasedMultiplayerClient.MatchOutOfDateApiException matchOutOfDateApiException =
                    (TurnBasedMultiplayerClient.MatchOutOfDateApiException)exception;

            new AlertDialog.Builder(mActivity)
                    .setMessage("Match was out of date, updating with latest match data...")
                    .setNeutralButton(android.R.string.ok, null)
                    .show();

            TurnBasedMatch match = matchOutOfDateApiException.getMatch();
            updateMatch(match);

            return;
        }

        if (exception instanceof ApiException) {
            ApiException apiException = (ApiException)exception;
            status = apiException.getStatusCode();
        }

        if (!checkStatusCode(status)) {
            return;
        }

        String message = "Status exception error: " + details + " Status: " + status + " Exception:" + exception;

        new AlertDialog.Builder(mActivity)
                .setMessage(message)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }

    private void logBadActivityResult(int requestCode, int resultCode, String message) {
        Log.i(TAG, "Bad activity result(" + resultCode + ") for request (" + requestCode + "): "
                   + message);
    }

    public void setMatchNotActive() {
        matchActive = false;
    }

    // This function is what gets called when you return from either the Play
    // Games built-in inbox, or else the create game built-in interface.
    //@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(intent);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                onConnected(account);
                if (signInAttemptHandler != null) {
                    signInAttemptHandler.onSuccess();
                }
            } catch (ApiException apiException) {
                String message = apiException.getMessage();
                if (message == null || message.isEmpty()) {
                    message = "Sign in other error";
                }

                onDisconnected();
                if (signInAttemptHandler != null) {
                    signInAttemptHandler.onFailure();
                }

                if (!message.contains("12501:")) { //Cancel sign in
                    new AlertDialog.Builder(mActivity)
                            .setMessage(message)
                            .setNeutralButton(android.R.string.ok, null)
                            .show();
                }
            }

        } else if (requestCode == RC_LOOK_AT_MATCHES) {
            // Returning from the 'Select Match' dialog

            if (resultCode != Activity.RESULT_OK) {
                logBadActivityResult(requestCode, resultCode,
                                     "User cancelled returning from the 'Select Match' dialog.");
                return;
            }

            TurnBasedMatch match = intent
                    .getParcelableExtra(Multiplayer.EXTRA_TURN_BASED_MATCH);

            if (match != null) {
                updateMatch(match);
            }

            Log.d(TAG, "Match = " + match);
        } else if (requestCode == RC_SELECT_PLAYERS) {
            // Returning from 'Select players to Invite' dialog

            if (resultCode != Activity.RESULT_OK) {
                // user canceled
                logBadActivityResult(requestCode, resultCode,
                                     "User cancelled returning from 'Select players to Invite' dialog");
                return;
            }

            // get the invitee list
            ArrayList<String> invitees = intent
                    .getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

            // get automatch criteria
            Bundle autoMatchCriteria;

            int minAutoMatchPlayers = intent.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
            int maxAutoMatchPlayers = intent.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

            if (minAutoMatchPlayers > 0) {
                autoMatchCriteria = RoomConfig.createAutoMatchCriteria(minAutoMatchPlayers,
                                                                       maxAutoMatchPlayers, 0);
            } else {
                autoMatchCriteria = null;
            }

            TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
                                                            .addInvitedPlayers(invitees)
                                                            .setAutoMatchCriteria(autoMatchCriteria).build();

            // Start the match
            mTurnBasedMultiplayerClient.createMatch(tbmc)
                                       .addOnSuccessListener(new OnSuccessListener<TurnBasedMatch>() {
                                           @Override
                                           public void onSuccess(TurnBasedMatch turnBasedMatch) {
                                               onInitiateMatch(turnBasedMatch);
                                           }
                                       })
                                       .addOnFailureListener(createFailureListener("There was a problem creating a match!"));
        }
    }

    // startMatch() happens in response to the createTurnBasedMatch()
    // above. This is only called on success, so we should have a
    // valid match object. We're taking this opportunity to setup the
    // game, saving our initial state. Calling takeTurn() will
    // callback to OnTurnBasedMatchUpdated(), which will show the game
    // UI.
    private void startMatch(TurnBasedMatch match) {
        mTurnData = new RiskyTurn();
        // Some basic turn data
        mTurnData.data = null;
        Log.d(TAG, "Number of players in this match: " + (match.getParticipantIds().size() + match.getAvailableAutoMatchSlots()));
        mTurnData.setNumPlayers(match.getParticipantIds().size() + match.getAvailableAutoMatchSlots());
        mTurnData.persistNumPlayers();
        mMatch = match;

        String myParticipantId = mMatch.getParticipantId(mPlayerId);

        Log.d(TAG, "#############STARTING MATCH#########################");
        if (mTurnData.persist() != null) {
            Log.d(TAG, "Sending match data that looks like:\n" + Arrays.toString(mTurnData.persist()));
        }

        mTurnBasedMultiplayerClient.takeTurn(match.getMatchId(),
                                             mTurnData.persist(), myParticipantId)
                                   .addOnSuccessListener(new OnSuccessListener<TurnBasedMatch>() {
                                       @Override
                                       public void onSuccess(TurnBasedMatch turnBasedMatch) {
                                           updateMatch(turnBasedMatch);
                                       }
                                   })
                                   .addOnFailureListener(createFailureListener("There was a problem taking a turn!"));
    }

    // If you choose to rematch, then call it and wait for a response.
    private void rematch() {
        mTurnBasedMultiplayerClient.rematch(mMatch.getMatchId())
                                   .addOnSuccessListener(new OnSuccessListener<TurnBasedMatch>() {
                                       @Override
                                       public void onSuccess(TurnBasedMatch turnBasedMatch) {
                                           onInitiateMatch(turnBasedMatch);
                                       }
                                   })
                                   .addOnFailureListener(createFailureListener("There was a problem starting a rematch!"));
        mMatch = null;
        isDoingTurn = false;
    }

    /**
     * Get the next participant. In this function, we assume that we are
     * round-robin, with all known players going before all automatch players.
     * This is not a requirement; players can go in any order. However, you can
     * take turns in any order.
     *
     * @return participantId of next player, or null if automatching
     */
    private String getNextParticipantId() {
        String myParticipantId = mMatch.getParticipantId(mPlayerId);

        ArrayList<String> participantIds = mMatch.getParticipantIds();

        int desiredIndex = -1;

        for (int i = 0; i < participantIds.size(); i++) {
            if (participantIds.get(i).equals(myParticipantId)) {
                desiredIndex = i + 1;
            }
        }

        if (desiredIndex < participantIds.size()) {
            return participantIds.get(desiredIndex);
        }

        if (mMatch.getAvailableAutoMatchSlots() <= 0) {
            // You've run out of automatch slots, so we start over.
            return participantIds.get(0);
        } else {
            // You have not yet fully automatched, so null will find a new
            // person to play against.
            return null;
        }
    }

    // This is the main function that gets called when players choose a match
    // from the inbox, or else create a match and want to start it.
    private void updateMatch(TurnBasedMatch match) {
        matchActive = true;

        mMatch = match;

        int status = match.getStatus();
        int turnStatus = match.getTurnStatus();

        Log.d(TAG, "########REVEIVED MATCH DATA######################");
        if (match.getData() != null) {
            Log.d(TAG, "Match data received looks like this:\n" + Arrays.toString(match.getData()));
            Log.d(TAG, "Match data has length " + match.getData().length);
        }

        switch (status) {
            case TurnBasedMatch.MATCH_STATUS_CANCELED:
                matchActive = false;
                showWarning("Canceled!", "This game was canceled!");
                return;

            case TurnBasedMatch.MATCH_STATUS_EXPIRED:
                matchActive = false;
                showWarning("Expired!", "This game is expired.  So sad!");
                return;

            case TurnBasedMatch.MATCH_STATUS_AUTO_MATCHING:
                matchActive = false;
                showWarning("Waiting for auto-match...",
                            "We're still waiting for an automatch partner.");
                return;

            case TurnBasedMatch.MATCH_STATUS_COMPLETE:
                matchActive = false;
                if (turnStatus == TurnBasedMatch.MATCH_TURN_STATUS_COMPLETE) {
                    showWarning("Complete!",
                                "This game is over; someone finished it, and so did you!  " +
                                "There is nothing to be done.");
                    break;
                }

                // Note that in this state, you must still call "Finish" yourself,
                // so we allow this to continue.
                showWarning("Complete!",
                            "This game is over; someone finished it!  You can only finish it now.");
        }

        // OK, it's active. Check on turn status.
        switch (turnStatus) {
            case TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN:
                mTurnData = RiskyTurn.unpersist(mMatch.getData());
                setGameplayUI();
                return;

            case TurnBasedMatch.MATCH_TURN_STATUS_THEIR_TURN:
                // Should return results.
                showWarning("Alas...", "It's not your turn.");
                break;

            case TurnBasedMatch.MATCH_TURN_STATUS_INVITED:
                showWarning("Good initiative!",
                            "Still waiting for invitations.\n\nBe patient!");
        }

        mTurnData = null;

        setViewVisibility();
    }

    private void onCancelMatch(String matchId) {
        isDoingTurn = false;

        matchActive = false;

        showWarning("Match", "This match (" + matchId + ") was canceled.  " +
                             "All other players will have their game ended.");
    }

    private void onInitiateMatch(TurnBasedMatch match) {
        if (match.getData() != null) {
            // This is a game that has already started, so I'll just start
            updateMatch(match);
            return;
        }

        startMatch(match);
    }

    private void onLeaveMatch() {
        matchActive = false;

        isDoingTurn = false;
        showWarning("Left", "You've left this match.");
    }

    private void onUpdateMatch(TurnBasedMatch match) {
        if (match.canRematch()) {
            askForRematch();
        }

        isDoingTurn = (match.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN);

        if (isDoingTurn) {
            updateMatch(match);
            return;
        }

        setViewVisibility();
    }

    private InvitationCallback mInvitationCallback = new InvitationCallback() {
        // Handle notification events.
        @Override
        public void onInvitationReceived(@NonNull Invitation invitation) {
            Toast.makeText(
                    mActivity,
                    "An invitation has arrived from "
                    + invitation.getInviter().getDisplayName(), Toast.LENGTH_SHORT)
                 .show();
        }

        @Override
        public void onInvitationRemoved(@NonNull String invitationId) {
            Toast.makeText(
                    mActivity,
                    "An invitation was removed.", Toast.LENGTH_SHORT)
                 .show();
        }
    };

    private TurnBasedMatchUpdateCallback mMatchUpdateCallback = new TurnBasedMatchUpdateCallback() {
        @Override
        public void onTurnBasedMatchReceived(@NonNull TurnBasedMatch turnBasedMatch) {
            Toast.makeText(
                    mActivity,
                    "A match was updated.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onTurnBasedMatchRemoved(@NonNull String matchId) {
            Toast.makeText(
                    mActivity,
                    "A match was removed.", Toast.LENGTH_SHORT).show();
        }
    };

    private void showErrorMessage(String message) {
        showWarning("Warning", message);
    }

    // Returns false if something went wrong, probably. This should handle
    // more cases, and probably report more accurate results.
    private boolean checkStatusCode(int statusCode) {
        switch (statusCode) {
            case GamesCallbackStatusCodes.OK:
                return true;

            case GamesClientStatusCodes.MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER:
                showErrorMessage("status_multiplayer_error_not_trusted_tester");
                break;

            case GamesClientStatusCodes.MATCH_ERROR_ALREADY_REMATCHED:
                showErrorMessage("match_error_already_rematched");
                break;

            case GamesClientStatusCodes.NETWORK_ERROR_OPERATION_FAILED:
                showErrorMessage("network_error_operation_failed");
                break;

            case GamesClientStatusCodes.INTERNAL_ERROR:
                showErrorMessage("internal_error");
                break;

            case GamesClientStatusCodes.MATCH_ERROR_INACTIVE_MATCH:
                showErrorMessage("match_error_inactive_match");
                break;

            case GamesClientStatusCodes.MATCH_ERROR_LOCALLY_MODIFIED:
                showErrorMessage("match_error_locally_modified");
                break;

            default:
                showErrorMessage("unexpected_status");
                Log.d(TAG, "Did not have warning or string to deal with: "
                           + statusCode);
        }

        return false;
    }

    public boolean matchActive() {
        return matchActive;
    }

    public void pauseGame() {
        matchActive = false;
    }

    public boolean isDoingTurn() {
        return isDoingTurn;
    }

    public IRiskyTurn getmRiskyTurn() {
        return mTurnData;
    }
}
