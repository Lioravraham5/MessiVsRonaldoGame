package com.example.messivsronaldo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class EndGame extends AppCompatActivity {

    private ShapeableImageView end_game_IMG_background;
    private MaterialTextView end_game_IMG_gameOver;
    private MaterialButton end_game_BTN_back_to_menu;
    private MaterialButton end_game_BTN_records_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        findViews();
        initializationViews();

        end_game_BTN_back_to_menu.setOnClickListener(view -> moveToMainActivity());
        end_game_BTN_records_table.setOnClickListener(view -> moveToRecordsTableActivity());

    }

    private void moveToRecordsTableActivity() {
        Intent RecordsTableActivityIntent = new Intent(this, RecordsTableActivity.class);
        startActivity(RecordsTableActivityIntent);

        //kill this activity:
        finish();

    }

    private void moveToMainActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);

        //kill this activity:
        finish();

    }

    private void findViews() {
        //background:
        end_game_IMG_background = findViewById(R.id.end_game_IMG_background);

        //Text: Game Over!
        end_game_IMG_gameOver = findViewById(R.id.end_game_LBL_gameOver);

        //buttons
        end_game_BTN_back_to_menu = findViewById(R.id.end_game_BTN_back_to_menu);
        end_game_BTN_records_table= findViewById(R.id.end_game_BTN_records_table);

    }

    private void initializationViews() {

        initBackgroundView();
    }

    private void initBackgroundView() {
        Glide
                .with(this)
                .load(R.drawable.soccer_field_background)
                .into(end_game_IMG_background);
    }
}