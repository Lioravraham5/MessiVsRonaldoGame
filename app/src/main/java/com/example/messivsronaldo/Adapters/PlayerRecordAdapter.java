package com.example.messivsronaldo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messivsronaldo.Interfaces.CallBackRecordClick;
import com.example.messivsronaldo.Model.PlayerRecord;
import com.example.messivsronaldo.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class PlayerRecordAdapter extends RecyclerView.Adapter<PlayerRecordAdapter.PlayerRecordViewHolder> {

    private Context context;
    private ArrayList<PlayerRecord> playersRecords;
    private CallBackRecordClick callBackRecordClick;

    public PlayerRecordAdapter(Context context, ArrayList<PlayerRecord> playersRecords, CallBackRecordClick callBackRecordClick) {
        this.context = context;
        this.playersRecords = playersRecords;
        this.callBackRecordClick = callBackRecordClick;
    }

    public void setCallBackRecordClick(CallBackRecordClick callBackRecordClick) {
        this.callBackRecordClick = callBackRecordClick;
    }

    @NonNull
    @Override
    public PlayerRecordAdapter.PlayerRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_record_item, parent, false);
        return new PlayerRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerRecordAdapter.PlayerRecordViewHolder holder, int position) {
        PlayerRecord playerRecord = gePlayerRecord(position);
        holder.player_record_item_LBL_name.setText(playerRecord.getName()); //set playerRecord name on view
        holder.player_record_item_LBL_score.setText(String.valueOf(playerRecord.getScore())); //set playerRecord score on view
    }

    @Override
    public int getItemCount() {
        if (playersRecords == null)
            return 0;
        else{
            return playersRecords.size();
        }
    }

    public PlayerRecord gePlayerRecord(int position){
        return playersRecords.get(position);
    }

    public class PlayerRecordViewHolder extends RecyclerView.ViewHolder{

        private MaterialTextView player_record_item_LBL_name;
        private MaterialTextView player_record_item_LBL_score;
        private MaterialButton player_record_item_BTN_location;


        public PlayerRecordViewHolder(@NonNull View itemView) {
            super(itemView);

            //connect between PlayerRecord object data to views in player_record_item.xml
            player_record_item_LBL_name = itemView.findViewById(R.id.player_record_item_LBL_name);
            player_record_item_LBL_score = itemView.findViewById(R.id.player_record_item_LBL_score);
            player_record_item_BTN_location = itemView.findViewById(R.id.player_record_item_BTN_location);

            player_record_item_BTN_location.setOnClickListener(v->
            {
                if (callBackRecordClick != null) {
                    PlayerRecord p = gePlayerRecord(getAdapterPosition());
                    callBackRecordClick.getPlayerRecordMapLocation(p);
                }
            });
        }
    }
}
