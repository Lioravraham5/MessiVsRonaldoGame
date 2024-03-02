package com.example.messivsronaldo.Model;

public class Messi {
    private final int MAX_RIGHT_POSITION = 4;
    private final int MAX_LEFT_POSITION = 0;
    private int positionIndex;

    public Messi(){

    }

    public int getPositionIndex() {
        return positionIndex;
    }

    public Messi setPosition(int positionIndex) {
        this.positionIndex = positionIndex;
        return this;
    }

    public boolean moveMessiRight(){
        if(!(positionIndex == MAX_RIGHT_POSITION)){
            positionIndex++;
            return true;
        }
        else
            return false;
    }

    public boolean moveMessiLeft(){
        if (!(positionIndex == MAX_LEFT_POSITION)){
            positionIndex--;
            return true;
        }
        else
            return false;
    }
}
