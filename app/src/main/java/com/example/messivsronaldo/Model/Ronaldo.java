package com.example.messivsronaldo.Model;

public class Ronaldo extends FallItem implements Comparable<Ronaldo> {

    public Ronaldo() {

    }

    @Override
    public int compareTo(Ronaldo r) {
        if(this.rowNumber > r.rowNumber)
            return 1;
        else if (this.rowNumber < r.rowNumber)
            return -1;
        else
            return 0;
    }
}
