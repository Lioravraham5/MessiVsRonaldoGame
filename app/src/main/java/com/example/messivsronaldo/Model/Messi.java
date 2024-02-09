package com.example.messivsronaldo.Model;

public class Messi {
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
        if(!(positionIndex == 2)){
            positionIndex++;
            return true;
        }
        else
            return false;
    }

    public boolean moveMessiLeft(){
        if (!(positionIndex == 0)){
            positionIndex--;
            return true;
        }

        else
            return false;
    }
}
