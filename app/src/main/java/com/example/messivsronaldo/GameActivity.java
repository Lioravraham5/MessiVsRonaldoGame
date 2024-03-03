package com.example.messivsronaldo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.messivsronaldo.Interfaces.CallBackMoves;
import com.example.messivsronaldo.Logic.GameManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.messivsronaldo.Model.Cup;
import com.example.messivsronaldo.Model.FallItem;
import com.example.messivsronaldo.Model.PlayerRecord;
import com.example.messivsronaldo.Model.Ronaldo;
import com.example.messivsronaldo.Utilities.TiltDetector;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends AppCompatActivity {

    //field:
    public static final String DIFFICULTY = "DIFFICULTY";
    public static final String MODE = "MODE";
    public static final String PLAYER_NAME = "PLAYER_NAME";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";

    public static final String SENSORS_BUTTON_TEXT = "SENSORS MODE";
    public static final String EASY_BUTTON_TEXT = "Easy Mode";
    public static final String HARD_BUTTON_TEXT = "Hard Mode";

    private final long TIMER_DELAY_EASY = 500;
    private final long TIMER_DELAY_HARD = 300;
    private final String RIGHT = "Right";
    private final String LEFT = "Left";
    private final String CRASHED_MESSAGE = "Crashed!";

    private ShapeableImageView game_IMG_background;
    private ImageButton game_BTN_left;
    private ImageButton game_BTN_right;
    private ShapeableImageView[][] game_IMG_FallItemsPositionsMatrix;
    private ShapeableImageView[] game_IMG_playerPositionsArray;
    private ShapeableImageView[] game_IMG_hearts;
    private MaterialTextView game_LBL_score;

    private GameManager gameManager;
    private Timer timer;
    private Intent previousScreen;
    private long timerDelay;

    private TiltDetector tiltDetector;

    private MediaPlayer crashSound;
    private MediaPlayer backgroundMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameManager = new GameManager();

        findViews();
        initializationViews();
        timerDelay = GameDifficulty();
        initializeTiltDetector();

        crashSound = MediaPlayer.create(this, R.raw.crash_sound);

        game_BTN_right.setOnClickListener(view -> movePlayer(RIGHT)); //right click
        game_BTN_left.setOnClickListener(view -> movePlayer(LEFT)); //left click


    }


    @Override
    protected void onStart() {
        super.onStart();
        startTimer(); //start timer
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkMode();
        playMusic();

    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel(); //stop timer
        stopMusic();
        stopTiltDetector();
    }


    private void refreshUI() {
        //game lost
        if (gameManager.isGameLost())
            gameOver();

            //game on
        else {
            game_LBL_score.setText(gameManager.getCurrentScore() + ""); //set score text
            if (gameManager.getNumOfEncounters() != 0)
                game_IMG_hearts[game_IMG_hearts.length - gameManager.getNumOfEncounters()].setVisibility(View.INVISIBLE); //main_IMG_hearts.length - gameManager.getNumOfEncounters = index of the heart that we want to disappear

            addFallItemToTheGameAndUpdatePositions();
        }

    }

    private void playMusic() {
        backgroundMusic = MediaPlayer.create(this, R.raw.game_song);
        backgroundMusic.start();
    }

    public void stopMusic(){
        backgroundMusic.stop();
    }

    private long GameDifficulty() {
        previousScreen = getIntent();
        String difficulty = previousScreen.getStringExtra(DIFFICULTY);
        if (difficulty.equals(EASY_BUTTON_TEXT)) { //easy mode
            return TIMER_DELAY_EASY;
        } else
            return TIMER_DELAY_HARD; //hard mode
    }

    private void checkMode(){
        previousScreen = getIntent();
        String mode = previousScreen.getStringExtra(MODE);
        if(mode.equalsIgnoreCase(SENSORS_BUTTON_TEXT) ) {
            game_BTN_left.setVisibility(View.INVISIBLE);
            game_BTN_right.setVisibility(View.INVISIBLE);
            tiltDetector.start();
        }
        else {
            game_BTN_left.setVisibility(View.VISIBLE);
            game_BTN_right.setVisibility(View.VISIBLE);
        }
    }

    private void stopTiltDetector() {
        previousScreen = getIntent();
        String mode = previousScreen.getStringExtra(MODE);
        if(mode.equals(SENSORS_BUTTON_TEXT) ) {
            tiltDetector.stop();
        }
    }

    private void initializeTiltDetector() {
        tiltDetector = new TiltDetector(this, new CallBackMoves() {
            @Override
            public void moveRight() {
                gameManager.getMessi().moveMessiRight();
                updatePlayerPosition();
            }

            @Override
            public void moveLeft() {
                gameManager.getMessi().moveMessiLeft();
                updatePlayerPosition();
            }
        });
    }

    private void gameOver() {

        // get player name from previous screen
        previousScreen = getIntent();
        String playerName = previousScreen.getStringExtra(PLAYER_NAME);
        double playerLatitude = previousScreen.getDoubleExtra(LATITUDE, 0.0);
        double playerLongitude = previousScreen.getDoubleExtra(LONGITUDE, 0.0);
        PlayerRecord playerRecord = new PlayerRecord();
        playerRecord.setName(playerName).setScore(gameManager.getCurrentScore()).setLatitude(playerLatitude).setLongitude(playerLongitude);

        gameManager.addPlayerRecordToDatabase(playerRecord); //add player record to the database

        //move to EndGame activity
        Intent endGameIntent = new Intent(this, EndGame.class);
        startActivity(endGameIntent);

        //kill main activity:
        finish();
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> refreshUI());
            }
        }, timerDelay, timerDelay);
    }

    private void movePlayer(String direction) {
        //move right
        if(direction.equals(RIGHT))
            gameManager.getMessi().moveMessiRight();

        //move left
        if(direction.equals(LEFT))
            gameManager.getMessi().moveMessiLeft();

        updatePlayerPosition();
    }


    private void updatePlayerPosition(){

        //invisible all the player position:
        for (int i = 0; i < game_IMG_playerPositionsArray.length; i++) {
            game_IMG_playerPositionsArray[i].setVisibility(View.INVISIBLE);
        }

        //visible the current player position:
        int currentPositionPlayer = gameManager.getMessi().getPositionIndex();
        game_IMG_playerPositionsArray[currentPositionPlayer].setVisibility(View.VISIBLE);
    }

    private void addFallItemToTheGameAndUpdatePositions(){

        //check encounter
        if(gameManager.isThereEncounter()){
            crashSound.start();
            vibrate();
            toast(CRASHED_MESSAGE);
        }

        //invisible all the fall Items
        for (FallItem item: gameManager.getCurrrentFallItemsList()) {
            int row = item.getRowNumber();
            int col = item.getColumnNumber();
            game_IMG_FallItemsPositionsMatrix[row][col].setVisibility(View.INVISIBLE);
        }

        //create new fall item, add it to the list, check encounter and update position
        gameManager.addAndUpdateFallItems();

        //visible all the fall items:
        updateFallItemsVisibility();
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    private void updateFallItemsVisibility() {

        for (FallItem item: gameManager.getCurrrentFallItemsList()) {
            int row = item.getRowNumber();
            int col = item.getColumnNumber();

            // if the item is a ronaldo, set ronaldo image
            if (item instanceof Ronaldo){
                game_IMG_FallItemsPositionsMatrix[row][col].setImageResource(GameManager.RONALDO_IMAGE);
            }

            // if the item is a cup, set cup image
            if (item instanceof Cup){
                game_IMG_FallItemsPositionsMatrix[row][col].setImageResource(GameManager.CUP_IMAGE);
            }

            //set the image of the fall item to visible
            game_IMG_FallItemsPositionsMatrix[row][col].setVisibility(View.VISIBLE);
        }
    }

    private void initializationViews() {

        initBackgroundView();
        initHeartsViews();
        initPlayerViews();
        initFallItemViews();
        initializationVisibility();

    }

    private void initFallItemViews() {
        for (int i = 0; i < GameManager.ROWS_OF_FALL_ITEMS_MATRIX; i++) {
            for (int j = 0; j < GameManager.COLUMNS_OF_FALL_ITEMS_MATRIX; j++) {
                game_IMG_FallItemsPositionsMatrix[i][j].setImageResource(R.drawable.ronaldo);
            }
        }
    }

    private void initBackgroundView() {
        Glide
                .with(this)
                .load(R.drawable.soccer_field_background)
                .into(game_IMG_background);
    }

    private void initializationVisibility() {

        //set initial hearts visibility
        initHeartsVisibility();

        //set initial player visibility
        initPlayerVisibility();

        //set initial fall items visibility
        initFalLItemsVisibility();

    }

    private void initFalLItemsVisibility() {
        for (int i = 0; i < GameManager.ROWS_OF_FALL_ITEMS_MATRIX; i++) {
            for (int j = 0; j < GameManager.COLUMNS_OF_FALL_ITEMS_MATRIX; j++) {
                game_IMG_FallItemsPositionsMatrix[i][j].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void initPlayerVisibility() {
        for (int i = 0; i < GameManager.NUMBER_OF_POSITIONS_IN_PLAYER_ARRAY; i++) {
            game_IMG_playerPositionsArray[i].setVisibility(View.INVISIBLE);
        }
        game_IMG_playerPositionsArray[GameManager.INITIAL_PLAYER_POSITION_INDEX].setVisibility(View.VISIBLE);
    }

    private void initHeartsVisibility() {
        for (int i = 0; i < game_IMG_hearts.length; i++) {
            game_IMG_hearts[i].setVisibility(View.VISIBLE);
        }
    }

    private void initHeartsViews() {
        for (int i = 0; i < game_IMG_hearts.length; i++) {
            Glide
                    .with(this)
                    .load(R.drawable.heart)
                    .into(game_IMG_hearts[i]);
        }
    }

    public  void initPlayerViews(){
        for (int i = 0; i < game_IMG_playerPositionsArray.length; i++) {
            Glide
                    .with(this)
                    .load(R.drawable.messi)
                    .into(game_IMG_playerPositionsArray[i]);
        }
    }

    private void findViews() {

        //background:
        game_IMG_background = findViewById(R.id.game_IMG_background);

        //buttons:
        game_BTN_left = findViewById(R.id.game_BTN_left);
        game_BTN_right = findViewById(R.id.game_BTN_right);

        //hearts;
        game_IMG_hearts = new ShapeableImageView[] {
          findViewById(R.id.game_IMG_heart1), findViewById(R.id.game_IMG_heart2), findViewById(R.id.game_IMG_heart3),
        };

        //score text
        game_LBL_score = findViewById(R.id.game_LBL_score);

        //obsticals Matrix positions
        game_IMG_FallItemsPositionsMatrix = new ShapeableImageView[][] {
                        {findViewById(R.id.game_IMG_obsticlePos00), findViewById(R.id.game_IMG_obsticlePos01), findViewById(R.id.game_IMG_obsticlePos02), findViewById(R.id.game_IMG_obsticlePos03), findViewById(R.id.game_IMG_obsticlePos04)},
                        {findViewById(R.id.game_IMG_obsticlePos10), findViewById(R.id.game_IMG_obsticlePos11), findViewById(R.id.game_IMG_obsticlePos12), findViewById(R.id.game_IMG_obsticlePos13), findViewById(R.id.game_IMG_obsticlePos14)},
                        {findViewById(R.id.game_IMG_obsticlePos20), findViewById(R.id.game_IMG_obsticlePos21), findViewById(R.id.game_IMG_obsticlePos22), findViewById(R.id.game_IMG_obsticlePos23), findViewById(R.id.game_IMG_obsticlePos24)},
                        {findViewById(R.id.game_IMG_obsticlePos30), findViewById(R.id.game_IMG_obsticlePos31), findViewById(R.id.game_IMG_obsticlePos32), findViewById(R.id.game_IMG_obsticlePos33), findViewById(R.id.game_IMG_obsticlePos34)},
                        {findViewById(R.id.game_IMG_obsticlePos40), findViewById(R.id.game_IMG_obsticlePos41), findViewById(R.id.game_IMG_obsticlePos42), findViewById(R.id.game_IMG_obsticlePos43), findViewById(R.id.game_IMG_obsticlePos44)},
                        {findViewById(R.id.game_IMG_obsticlePos50), findViewById(R.id.game_IMG_obsticlePos51), findViewById(R.id.game_IMG_obsticlePos52), findViewById(R.id.game_IMG_obsticlePos53), findViewById(R.id.game_IMG_obsticlePos54)},
                        {findViewById(R.id.game_IMG_obsticlePos60), findViewById(R.id.game_IMG_obsticlePos61), findViewById(R.id.game_IMG_obsticlePos62), findViewById(R.id.game_IMG_obsticlePos63), findViewById(R.id.game_IMG_obsticlePos64)}

        };

        //player array positions
        game_IMG_playerPositionsArray = new ShapeableImageView[] {
                findViewById(R.id.game_IMG_messiPos0), findViewById(R.id.game_IMG_messiPos1), findViewById(R.id.game_IMG_messiPos2), findViewById(R.id.game_IMG_messiPos3), findViewById(R.id.game_IMG_messiPos4)
        };

    }



}