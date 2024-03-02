package com.example.messivsronaldo.Logic;

import android.util.Log;

import com.example.messivsronaldo.Model.Cup;
import com.example.messivsronaldo.Model.FallItem;
import com.example.messivsronaldo.Model.Messi;
import com.example.messivsronaldo.Model.PlayerRecord;
import com.example.messivsronaldo.Model.PlayersRecordsDatabase;
import com.example.messivsronaldo.Model.Ronaldo;
import com.example.messivsronaldo.R;
import com.example.messivsronaldo.Utilities.SharedPreferencesManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;

public class GameManager {

    public static final String KEY_PLAYER_RECORDS_DATABASE="PLAYERS_RECORDS_DATABASE";

    public static final int INITIAL_PLAYER_POSITION_INDEX = 2;
    public static final int ROW_ENCOUNTER_INDEX = 6;
    public static final int ROWS_OF_FALL_ITEMS_MATRIX = 7;
    public static final int COLUMNS_OF_FALL_ITEMS_MATRIX = 5;
    public static final int NUMBER_OF_POSITIONS_IN_PLAYER_ARRAY = 5;
    public static final int SCORE_FOR_CUP_ENCOUNTER = 10;
    public static final int RONALDO_IMAGE = R.drawable.ronaldo;
    public static final int CUP_IMAGE = R.drawable.world_cup;

    private ArrayList<FallItem> currrentFallItemsList = new ArrayList<>();
    private Messi messi = new Messi().setPosition(INITIAL_PLAYER_POSITION_INDEX); //create a player.
    private int life = 3; //maximum life in game
    private int numOfEncounters = 0; //number of current mistakes
    private boolean isItemFallInPreviousRound = false;
    private int currentScore = 0;

    private  PlayersRecordsDatabase playersRecordsDatabase;
    private Gson gson = new Gson();

    public GameManager(){

    }

    public ArrayList<FallItem> getCurrrentFallItemsList() {
        return currrentFallItemsList;
    }

    public Messi getMessi() {
        return messi;
    }

    public int getLife() {
        return life;
    }

    public int getNumOfEncounters() {
        return numOfEncounters;
    }

    public void addFallItem(FallItem item) {
        currrentFallItemsList.add(0,item);
    }

    public void removeOldestFallItem(){

        currrentFallItemsList.remove(currrentFallItemsList.size() - 1);
    }

    public boolean isGameLost(){
        return getNumOfEncounters() == getLife();
    }

    public boolean isThereEncounter(){
        if(currrentFallItemsList.isEmpty())
            return false;
        else {
            FallItem item = currrentFallItemsList.get(currrentFallItemsList.size() - 1);

            //if the item that fall reached to the row encounter
            if (item.getRowNumber() == ROW_ENCOUNTER_INDEX) {
                //if the item and the player in the same column and row:
                if (messi.getPositionIndex() == item.getColumnNumber()) {
                    //if the player encounter ronaldo item
                    if(item instanceof Ronaldo){
                        numOfEncounters++;
                        return true;
                    }
                    // the player encounter a cup item
                    else {
                        currentScore += SCORE_FOR_CUP_ENCOUNTER;
                        return false; // the encounter is not with ronaldo item
                    }

                } else
                    return false;
            } else
                return false;
        }
    }

    public void addAndUpdateFallItems(){

        //increase all the row values of the items if there are items in the currrentFallItemsList
        if(currrentFallItemsList.size() != 0){
            for (FallItem item: currrentFallItemsList) {
                item.setRowNumber(item.getRowNumber()+1);
            }

            //if the priority item is came to the end
            if(currrentFallItemsList.get(currrentFallItemsList.size() - 1) != null){
                if(currrentFallItemsList.get(currrentFallItemsList.size() - 1).getRowNumber() > ROW_ENCOUNTER_INDEX){
                    currrentFallItemsList.remove(currrentFallItemsList.size() - 1);
                }
            }
        }

        //item wasn't added to the arrayList in the previous round
        if(!isItemFallInPreviousRound) {
            //enter new fall item
            isItemFallInPreviousRound = true;
            FallItem newItem = createNewFallItem(); //return ronaldo item or cup item
            currrentFallItemsList.add(0,newItem);
        }

        //item was added to the arrayList in the previous round
        else {
            isItemFallInPreviousRound = false;
        }


    }

    public FallItem createNewFallItem() {
        Random r= new Random();
        FallItem newFallItem;
        int randomNumber = (r.nextInt(9) + 1); // 1<=randomMuber<=9
        if(randomNumber > 3)
            newFallItem = new Ronaldo(); // 1/3 probability of new ronaldo
        else
            newFallItem = new Cup(); // 2/3 probability of new cup

        return newFallItem;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void addPlayerRecordToDatabase(PlayerRecord playerRecord){ //write to share preferences

        getUpdatePlayersRecordsDatabase();
        playersRecordsDatabase.addPLayerRecord(playerRecord);

        String databaseToSp = gson.toJson(playersRecordsDatabase); //convert to json
        Log.d("json to database"  ,databaseToSp); //log for check
        SharedPreferencesManager.getInstance().putString(KEY_PLAYER_RECORDS_DATABASE, databaseToSp); // save in share preferences


    }

    public void getUpdatePlayersRecordsDatabase() { //read from share preferences
        String databaseFromSp = SharedPreferencesManager.getInstance().getString(KEY_PLAYER_RECORDS_DATABASE, "");
        Log.d("Players records database"  ,databaseFromSp); //log for check

        playersRecordsDatabase = gson.fromJson(databaseFromSp, PlayersRecordsDatabase.class);
        if(playersRecordsDatabase == null){
            playersRecordsDatabase = new PlayersRecordsDatabase();
        }
    }


}
