package com.example.messivsronaldo.Model;

import java.util.ArrayList;
import java.util.Comparator;

public class PlayersRecordsDatabase {

    public static final int MAX_RECORDS = 10;
    private ArrayList<PlayerRecord> topTenPlayersRecords = new ArrayList<PlayerRecord>();
    private ComparePlayersRecordsByScore comparePlayersRecordsByScore = new ComparePlayersRecordsByScore();

    public PlayersRecordsDatabase(){

    }

    public ArrayList<PlayerRecord> getTopTenPlayersRecords() {
        return topTenPlayersRecords;
    }

    public PlayersRecordsDatabase setTopTenPlayersRecords(ArrayList<PlayerRecord> topTenPlayersRecords) {
        this.topTenPlayersRecords = topTenPlayersRecords;
        return this;
    }

    public void addPLayerRecord(PlayerRecord playerRecord){
        if(topTenPlayersRecords.size() == MAX_RECORDS){
            removeLastRecord();
        }
        topTenPlayersRecords.add(playerRecord);
        sortTopTenPlayersRecords();

    }

    private void removeLastRecord() {
        topTenPlayersRecords.remove(topTenPlayersRecords.size() - 1);
    }

    public void sortTopTenPlayersRecords(){
        topTenPlayersRecords.sort(comparePlayersRecordsByScore);
    }

    @Override
    public String toString() {
        return "PlayersRecordsDatabase{" +
                "topTenPlayersRecords=" + topTenPlayersRecords +
                '}';
    }
}
