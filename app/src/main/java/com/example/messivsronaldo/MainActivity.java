package com.example.messivsronaldo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {

    private ShapeableImageView main_IMG_background;
    private MaterialButton main_BTN_start_game;
    private MaterialButton main_BTN_records_table;
    private ToggleButton main_BTN_easy_or_hard_difficulty;
    private ToggleButton main_BTN_buttons_or_sensors_mode;
    private EditText main_edit_text;

    MediaPlayer backgroundMusic;

    //for map
    public static final int FINE_PERMISSION_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        intializeViews();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        setCurrentLocation();

        main_BTN_start_game.setOnClickListener(view -> startGame());

        main_BTN_records_table.setOnClickListener(view -> moveRecordsTableActivity());
    }

    @Override
    protected void onResume() {
        super.onResume();
        playMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMusic();
    }

    private void moveRecordsTableActivity() {



        Intent recordsTableIntent = new Intent(this, RecordsTableActivity.class);
        startActivity(recordsTableIntent);

        //kill main activity:
        //finish();
    }


    private void startGame() {

        if (currentLocation == null){
            Toast.makeText(this, "Location permission denied, please allow the permission", Toast.LENGTH_LONG).show();
            return;
        }

        if(isNameEntered()){
            Intent startGameIntent = new Intent(this, GameActivity.class);
            startGameIntent.putExtra(GameActivity.DIFFICULTY, main_BTN_easy_or_hard_difficulty.getText());
            startGameIntent.putExtra(GameActivity.MODE, main_BTN_buttons_or_sensors_mode.getText());
            startGameIntent.putExtra(GameActivity.PLAYER_NAME, main_edit_text.getText().toString());
            startGameIntent.putExtra(GameActivity.LATITUDE, currentLocation.getLatitude());
            startGameIntent.putExtra(GameActivity.LONGITUDE, currentLocation.getLongitude());

            startActivity(startGameIntent);

            //kill main activity:
            finish();
        }

    }

    private boolean isNameEntered() {
        if(TextUtils.isEmpty(main_edit_text.getText())){
            main_edit_text.setError("You must enter a name");
            return false;
        }
        else
            return true;
    }

    private void findViews() {

        //background image:
        main_IMG_background = findViewById(R.id.main_IMG_background);

        //buttons:
        main_BTN_start_game = findViewById(R.id.main_BTN_start_game);
        main_BTN_records_table = findViewById(R.id.main_BTN_records_table);
        main_BTN_easy_or_hard_difficulty = findViewById(R.id.main_BTN_easy_or_hard_difficulty);
        main_BTN_buttons_or_sensors_mode = findViewById((R.id.main_BTN_buttons_or_sensors_mode));

        //edit text
        main_edit_text = findViewById(R.id.main_edit_text);


    }

    private void intializeViews() {
       //initialize background
        Glide
                .with(this)
                .load(R.drawable.soccer_field_background)
                .into(main_IMG_background);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setCurrentLocation();
            }
        } else {
            Toast.makeText(this, "Location permission denied, please allow the permission", Toast.LENGTH_LONG).show();
        }
    }

    private void setCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //request runtime permission from the user
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                currentLocation = location;
            }
        });

    }

    private void playMusic() {
        backgroundMusic = MediaPlayer.create(this, R.raw.start_manu_song);
        backgroundMusic.start();
    }

    public void stopMusic(){
        backgroundMusic.stop();
    }



}