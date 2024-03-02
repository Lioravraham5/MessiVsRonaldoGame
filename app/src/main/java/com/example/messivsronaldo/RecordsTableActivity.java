package com.example.messivsronaldo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.example.messivsronaldo.Interfaces.CallBackRecordClick;
import com.example.messivsronaldo.Model.PlayerRecord;
import com.example.messivsronaldo.Views.ListFragment;
import com.example.messivsronaldo.Views.MapFragment;
import com.google.android.material.imageview.ShapeableImageView;

public class RecordsTableActivity extends AppCompatActivity {

    private ShapeableImageView records_table_IMG_background;
    private FrameLayout records_table_FRAME_list_records;
    private FrameLayout records_table_FRAME_map;

    private MapFragment mapFragment;
    private ListFragment listFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_table);
        findViews();
        initilizeViews();
        initializeFragments();


    }

    private void initilizeViews() {
        //initialize background
        Glide
                .with(this)
                .load(R.drawable.soccer_field_background)
                .into(records_table_IMG_background);
    }

    private void initializeFragments() {
        listFragment = new ListFragment();
        mapFragment = new MapFragment();

        //set the callBack:
        listFragment.setCallBackRecordClick(new CallBackRecordClick() {
            @Override
            public void getPlayerRecordMapLocation(PlayerRecord playerRecord) {
                mapFragment.zoom(playerRecord.getLatitude(), playerRecord.getLongitude(), playerRecord.getName());
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.records_table_FRAME_list_records, listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.records_table_FRAME_map, mapFragment).commit();
    }

    private void findViews() {
        records_table_IMG_background = findViewById(R.id.records_table_IMG_background);
        records_table_FRAME_list_records = findViewById(R.id.records_table_FRAME_list_records);
        records_table_FRAME_map = findViewById(R.id.records_table_FRAME_map);
    }
}