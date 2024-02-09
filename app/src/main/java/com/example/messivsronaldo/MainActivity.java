package com.example.messivsronaldo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.messivsronaldo.Logic.GameManager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.messivsronaldo.Model.FallItem;
import com.example.messivsronaldo.Model.Ronaldo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    //field:
    private final long TIMER_DELAY = 500;
    private final String RIGHT = "Right";
    private final String LEFT = "Left";
    private final int ROWS_FALLITEM_MATRIX = 6;
    private final int COLUMNS_FALLITEM_MATRIX =3;
    private final String CRASHED_MESSAGE = "Crashed!";

    private ShapeableImageView main_IMG_background;
    private ImageButton main_BTN_left;
    private ImageButton main_BTN_right;
    private ShapeableImageView[][] main_IMG_FallItemsPositionsMatrix;
    private ShapeableImageView[] main_IMG_playerPositionsArray;
    private ShapeableImageView[] main_IMG_hearts;
    private GameManager gameManager;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameManager = new GameManager();

        findViews();
        initializationViews();

        main_BTN_right.setOnClickListener(view -> movePlayer(RIGHT)); //right click
        main_BTN_left.setOnClickListener(view -> movePlayer(LEFT)); //left click

        startTimer();


    }

    private void refreshUI() {
        //game lost
        if(gameManager.isGameLost())
            gameOver();

        //game on
        else{
            if (gameManager.getNumOfEncounters() != 0)
                main_IMG_hearts[main_IMG_hearts.length - gameManager.getNumOfEncounters()].setVisibility(View.INVISIBLE); //main_IMG_hearts.length - gameManager.getNumOfEncounters = index of the heart that we want to disappear
            addFallItemToTheGameAndUpdatePositions();
        }

    }

    private void gameOver() {
        timer.cancel();

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
        }, TIMER_DELAY, TIMER_DELAY);
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
        for (int i = 0; i < main_IMG_playerPositionsArray.length; i++) {
            main_IMG_playerPositionsArray[i].setVisibility(View.INVISIBLE);
        }

        //visible the current player position:
        int currentPositionPlayer = gameManager.getMessi().getPositionIndex();
        main_IMG_playerPositionsArray[currentPositionPlayer].setVisibility(View.VISIBLE);
    }

    private void addFallItemToTheGameAndUpdatePositions(){

        //check encounter
        if(gameManager.isThereEncounter()){
            vibrate();
            toast(CRASHED_MESSAGE);
        }

        //invisible all the fall Items
        for (FallItem item: gameManager.getCurrrentFallItemsList()) {
            int row = item.getRowNumber();
            int col = item.getColumnNumber();
            main_IMG_FallItemsPositionsMatrix[row][col].setVisibility(View.INVISIBLE);
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
            main_IMG_FallItemsPositionsMatrix[row][col].setVisibility(View.VISIBLE);
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
        for (int i = 0; i < ROWS_FALLITEM_MATRIX; i++) {
            for (int j = 0; j < COLUMNS_FALLITEM_MATRIX; j++) {
                Glide
                        .with(this)
                        .load(R.drawable.ronaldo)
                        .into(main_IMG_FallItemsPositionsMatrix[i][j]);
            }
        }
    }

    private void initBackgroundView() {
        Glide
                .with(this)
                .load(R.drawable.soccer_field_background)
                .into(main_IMG_background);
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
                main_IMG_FallItemsPositionsMatrix[i][j].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void initPlayerVisibility() {
        main_IMG_playerPositionsArray[0].setVisibility(View.INVISIBLE);
        main_IMG_playerPositionsArray[1].setVisibility(View.VISIBLE);
        main_IMG_playerPositionsArray[2].setVisibility(View.INVISIBLE);
    }

    private void initHeartsVisibility() {
        for (int i = 0; i < main_IMG_hearts.length; i++) {
            main_IMG_hearts[i].setVisibility(View.VISIBLE);
        }
    }

    private void initHeartsViews() {
        for (int i = 0; i < main_IMG_hearts.length; i++) {
            Glide
                    .with(this)
                    .load(R.drawable.heart)
                    .into(main_IMG_hearts[i]);
        }
    }

    public  void initPlayerViews(){
        for (int i = 0; i < main_IMG_playerPositionsArray.length; i++) {
            Glide
                    .with(this)
                    .load(R.drawable.messi)
                    .into(main_IMG_playerPositionsArray[i]);
        }
    }

    private void findViews() {

        //background:
        main_IMG_background = findViewById(R.id.main_IMG_background);

        //buttons:
        main_BTN_left = findViewById(R.id.main_BTN_left);
        main_BTN_right = findViewById(R.id.main_BTN_right);

        //hearts;
        main_IMG_hearts = new ShapeableImageView[] {
          findViewById(R.id.main_IMG_heart1), findViewById(R.id.main_IMG_heart2), findViewById(R.id.main_IMG_heart3),
        };

        //obsticals Matrix positions
        main_IMG_FallItemsPositionsMatrix = new ShapeableImageView[][] {
                        {findViewById(R.id.main_IMG_obsticlePos00), findViewById(R.id.main_IMG_obsticlePos01), findViewById(R.id.main_IMG_obsticlePos02)},
                        {findViewById(R.id.main_IMG_obsticlePos10), findViewById(R.id.main_IMG_obsticlePos11), findViewById(R.id.main_IMG_obsticlePos12)},
                        {findViewById(R.id.main_IMG_obsticlePos20), findViewById(R.id.main_IMG_obsticlePos21), findViewById(R.id.main_IMG_obsticlePos22)},
                        {findViewById(R.id.main_IMG_obsticlePos30), findViewById(R.id.main_IMG_obsticlePos31), findViewById(R.id.main_IMG_obsticlePos32)},
                        {findViewById(R.id.main_IMG_obsticlePos40), findViewById(R.id.main_IMG_obsticlePos41), findViewById(R.id.main_IMG_obsticlePos42)},
                        {findViewById(R.id.main_IMG_obsticlePos50), findViewById(R.id.main_IMG_obsticlePos51), findViewById(R.id.main_IMG_obsticlePos52)}
        };

        //player array positions
        main_IMG_playerPositionsArray = new ShapeableImageView[] {
                findViewById(R.id.main_IMG_messiPos0), findViewById(R.id.main_IMG_messiPos1), findViewById(R.id.main_IMG_messiPos2)
        };


    }
}