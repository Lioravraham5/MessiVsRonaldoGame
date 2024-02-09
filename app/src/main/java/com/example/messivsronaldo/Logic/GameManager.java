package com.example.messivsronaldo.Logic;

import com.example.messivsronaldo.Model.FallItem;
import com.example.messivsronaldo.Model.Messi;
import com.example.messivsronaldo.Model.Ronaldo;

import java.util.ArrayList;

public class GameManager {

    public static final int INITIAL_PLAYER_POSITION_INDEX = 1;
    public static final int ROW_ENCOUNTER_INDEX = 5;
    public static final int ROWS_OF_FALL_ITEMS_MATRIX = 6;
    public static final int COLUMNS_OF_FALL_ITEMS_MATRIX = 3;

    private ArrayList<FallItem> currrentFallItemsList = new ArrayList<>();
    private Messi messi = new Messi().setPosition(INITIAL_PLAYER_POSITION_INDEX); //create a player.
    private int life = 3; //maximum life in game
    private int numOfEncounters = 0; //number of current mistakes
    private boolean isItemFallInPreviousRound = false;


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
                    numOfEncounters++;
                    return true;
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
            FallItem newItem = new Ronaldo();
            currrentFallItemsList.add(0,newItem);
        }

        //item wasn't added to the arrayList in the previous round
        else {
            isItemFallInPreviousRound = false;
        }


    }






}
