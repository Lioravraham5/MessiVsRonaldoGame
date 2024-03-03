package com.example.messivsronaldo.Model;

import java.util.Comparator;

public class ComparePlayersRecordsByScore implements Comparator<PlayerRecord> {
    @Override
    public int compare(PlayerRecord p1, PlayerRecord p2) {
        if (p1.getScore() < p2.getScore()){
            return 1;
        }
        else if (p1.getScore() > p2.getScore()) {
            return -1;
        }
        else
            return 0;
    }
}
