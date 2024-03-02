package com.example.messivsronaldo.Views;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.messivsronaldo.Adapters.PlayerRecordAdapter;
import com.example.messivsronaldo.Interfaces.CallBackRecordClick;
import com.example.messivsronaldo.Logic.GameManager;
import com.example.messivsronaldo.Model.PlayersRecordsDatabase;
import com.example.messivsronaldo.R;
import com.example.messivsronaldo.Utilities.SharedPreferencesManager;
import com.google.gson.Gson;

public class ListFragment extends Fragment {

    private PlayersRecordsDatabase playersRecordsDatabase;
    private RecyclerView fragment_list_RCW_records;
    private CallBackRecordClick callBackRecordClick;

    public ListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false); //view we create of the list fragments
        findViews(view);
        initializeViews(getContext());

        return view;
    }

    private void initializeViews(Context context) {
        String databaseFromSp = SharedPreferencesManager.getInstance().getString(GameManager.KEY_PLAYER_RECORDS_DATABASE, "");
        playersRecordsDatabase = new Gson().fromJson(databaseFromSp, PlayersRecordsDatabase.class);

        if (playersRecordsDatabase != null){
            PlayerRecordAdapter playerAdapter = new PlayerRecordAdapter(context,playersRecordsDatabase.getTopTenPlayersRecords() ,callBackRecordClick);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            fragment_list_RCW_records.setLayoutManager(linearLayoutManager);
            fragment_list_RCW_records.setAdapter(playerAdapter);
        }


    }

    private void findViews(View view) {
        fragment_list_RCW_records = view.findViewById(R.id.fragment_list_RCW_records);
    }

    public void setCallBackRecordClick(CallBackRecordClick callBackRecordClick) {
        if(callBackRecordClick != null)
            this.callBackRecordClick = callBackRecordClick;
    }
}