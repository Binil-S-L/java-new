package com.binilsl.firstapp;

public enum SudokuPuzzleType {
    NINEBYNINE(new String [] {"1", "2", "3", "4", "5", "6", "7", "8", "9"});

   private final int rows;
   private final int columns;
   private final int boxWidth;
   private final int boxHeight;
    private final String[] validValue;

    private SudokuPuzzleType(String [] validValue){
        this.validValue = validValue;
        this.rows = rows;
        this.columns = columns;
        this.boxHeight = boxHeight;
        this.boxWidth = boxWidth;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getBoxWidth() {
        return boxWidth;
    }

    public int getBoxHeight() {
        return boxHeight;
    }

    public String[] getValidValue() {
        return validValue;
    }

}
