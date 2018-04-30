package com.templecis.escaperoute;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClientStatusCodes;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.example.games.basegameutils.GameHelper;
import com.templecis.escaperoute.screens.EscaperGameScreen;
import com.templecis.escaperoute.screens.MenuScreen;
import com.templecis.escaperoute.util.ActionResolver;

import java.util.ArrayList;
import java.util.List;

public class AndroidLauncher extends AndroidApplication implements GameHelper.GameHelperListener, ActionResolver, RoomUpdateListener, RealTimeMessageReceivedListener, RoomStatusUpdateListener {

	private GameHelper gameHelper;
	private String mRoomId = "";
	private String mMyId = "";
	private Room mRoomCurrent;
	private ArrayList<Participant> mParticipants;

	private EscapeRouteMain erm;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useGyroscope = true;  //default is false
		config.useAccelerometer = true;
		config.useCompass = false;

		erm = new EscapeRouteMain(this);

		initialize(erm, config);

		if (gameHelper == null) {
			gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
			//gameHelper.enableDebugLog(true);
		}
		gameHelper.setup(this);
	}

	@Override
	public void onStart(){
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	public void onStop(){
		super.onStop();
		gameHelper.onStop();
	}


	@Override
	public boolean getSignedInGPGS() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void loginGPGS() {
		try {
			runOnUiThread(new Runnable(){
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex) {

		}
	}

	@Override
	public void submitScoreGPGS(int score) {

	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {

	}

	@Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		gameHelper.onActivityResult(request, response, data);
	}

	@Override
	public void getLeaderboardGPGS() {
		if (gameHelper.isSignedIn()) {
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), getResources().getString(R.string.app_id)), 100);
		}
		else if (!gameHelper.isConnecting()) {
			loginGPGS();
		}
	}

	@Override
	public void getAchievementsGPGS() {

	}

	@Override
	public void logoutGPGS() {
		gameHelper.signOut();
	}

	@Override
	public void waitingRoom() {

		final int MIN_OPPONENTS = 1, MAX_OPPONENTS = 1;
		Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS, MAX_OPPONENTS, 0);

		RoomConfig.Builder builder = RoomConfig.builder(this);

		// Players will only be matched with users running compatible versions of the app
		//builder.setVariant(MULTIPLAYER_VERSION);

		builder.setMessageReceivedListener(this);
		builder.setRoomStatusUpdateListener(this);
		builder.setAutoMatchCriteria(autoMatchCriteria);


		//erm.ms.startGame();

		Games.RealTimeMultiplayer.create(gameHelper.getApiClient(), builder.build());



		//startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), "CgkIx_bl-vkfEAIQAA"), 100);

		//startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), "CgkIx_bl-vkfEAIQAA"), 100);
	}


	@Override
	public void onSignInFailed() {
	}

	@Override
	public void onSignInSucceeded() {

	}


	@Override
	public void onJoinedRoom(int i, Room room) {
		showWaitingRoom(room);
		//finishActivity(251);
		//erm.ms.startGame();

		//Intent intent = Games.RealTimeMultiplayer.getWaitingRoomIntent(gameHelper.getApiClient(), room, 1);
		//startActivityForResult(intent, 386);
	}

	@Override
	public void onLeftRoom(int i, String s) {

	}



	void updateRoom(Room room) {
		if (room != null) mParticipants = room.getParticipants();
		if (mParticipants != null) {
			// Nothing
		}
	}

	@Override
	public void onRoomCreated(final int statusCode, Room room) {

		Gdx.app.debug("ANDROIDLAUNCHER", "On room created()");


		if(statusCode != GamesClientStatusCodes.SUCCESS){
			try {
				runOnUiThread(new Runnable(){
					public void run() {
						//Toast.makeText(getContext(), "ANDROIDLAUNCHER", "On room created() ERROR" + statusCode, Toast.LENGTH_LONG).show();


					}
				});
			} catch (final Exception ex) {

			}
			return;
		}
		showWaitingRoom(room);

	}

	@Override
	public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {

	}

	@Override
	public void onRoomConnecting(Room room) {

	}

	@Override
	public void onRoomAutoMatching(Room room) {

	}

	@Override
	public void onPeerInvitedToRoom(Room room, List<String> list) {

	}

	@Override
	public void onPeerDeclined(Room room, List<String> list) {

	}

	@Override
	public void onPeerJoined(Room room, List<String> list) {

	}

	@Override
	public void onPeerLeft(Room room, List<String> list) {

	}

	@Override
	public void onRoomConnected(int statusCode, Room room) {
		//dLog("onRoomConnected");
		mRoomCurrent = room;
		mParticipants = room.getParticipants();

		mMyId = room.getParticipantId(Games.Players.getCurrentPlayerId(gameHelper.getApiClient()));

		try {
			//bWaitRoomDismissedFromCode = true;
			finishActivity(251);
		} catch (Exception e) {
			//dLog("would have errored out in waiting room");
		}

		//aHelper.getGamesClient();
		//tell the Game the room is connected
		if (statusCode == GamesClientStatusCodes.SUCCESS) {
			erm.onRoomConnected(room.getParticipantIds(), mMyId, room.getCreationTimestamp() );
			Gdx.app.debug("ANDROIDLAUNCHER", "StartGame");
		} else {
			//leaveRoom();

		}


		//finishActivity(251);
		//erm.ms.startGame();

		//updateRoom(room);

		//Gdx.app.debug("ANDROIDLAUNCHER", "OnRoomConnected");


	}

	@Override
	public void onConnectedToRoom(Room room) {

		Gdx.app.debug("ANDROIDLAUNCHER", "onConnectedToRoom.");

		// get room ID, participants and my ID:
		mRoomId = room.getRoomId();
		mParticipants = room.getParticipants();
		mMyId = room.getParticipantId(Games.Players.getCurrentPlayerId(gameHelper.getApiClient()));


		// print out the list of participants (for debug purposes)
		Gdx.app.debug("ANDROIDLAUNCHER", "Room ID: " + mRoomId);
		Gdx.app.debug("ANDROIDLAUNCHER", "My ID " + mMyId);
		Gdx.app.debug("ANDROIDLAUNCHER", "<< CONNECTED TO ROOM>>");






	}

	@Override
	public void onDisconnectedFromRoom(Room room) {

	}

	@Override
	public void onPeersConnected(Room room, List<String> list) {

	}

	@Override
	public void onPeersDisconnected(Room room, List<String> list) {

	}

	@Override
	public void onP2PConnected(String s) {

	}

	@Override
	public void onP2PDisconnected(String s) {

	}

	void showWaitingRoom(Room room) {
		// minimum number of players required for our game
		// For simplicity, we require everyone to join the game before we start it
		// (this is signaled by Integer.MAX_VALUE).
		final int MIN_PLAYERS = Integer.MAX_VALUE;
		Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(gameHelper.getApiClient(), room, 2);

		// show waiting room UI
		startActivityForResult(i, 251);


	}
}
