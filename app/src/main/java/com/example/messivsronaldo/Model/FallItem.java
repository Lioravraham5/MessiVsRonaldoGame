package com.example.messivsronaldo.Model;

import java.util.Random;

public abstract class FallItem {

    protected int rowNumber = 0;
    protected int columnNumber = (new Random()).nextInt(3);

    public FallItem(){

    }

    public int getRowNumber() {
        return rowNumber;
    }

    public FallItem setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
        return this;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public FallItem setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
        return this;
    }
}
