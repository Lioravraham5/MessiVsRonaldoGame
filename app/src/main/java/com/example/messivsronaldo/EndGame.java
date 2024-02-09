package com.example.messivsronaldo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class EndGame extends AppCompatActivity {

    private ShapeableImageView end_game_IMG_background;
    private MaterialTextView end_game_IMG_gameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        findViews();
        initializationViews();
    }

    private void findViews() {
        //background:
        end_game_IMG_background = findViewById(R.id.end_game_IMG_background);

        //Text: Game Over!
        end_game_IMG_gameOver = findViewById(R.id.end_game_LBL_gameOver);

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