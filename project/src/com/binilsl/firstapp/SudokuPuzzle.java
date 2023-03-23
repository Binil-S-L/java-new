package com.binilsl.firstapp;

public class SudokuPuzzle {

    protected String[][] board;
    private final int ROWS;
    private final int COLUMNS;
    private final int BOXWIDTH;
    private final int BOXHEIGHT;
    private final String[] VALIDVALUE;

    public SudokuPuzzle(int rows, int columns, int boxwidth, int boxheight, String [] validValue){
        this.ROWS = rows;
        this.COLUMNS = columns;
        this.BOXHEIGHT = boxheight;
        this.BOXWIDTH = boxwidth;
        this.VALIDVALUE = validValue;
        this.board = new String[ROWS][COLUMNS];
        initializeBoard();
    }

    private void initializeBoard() {

        for (int row = 0; row < this.ROWS; row++){
            for (int col = 0; col < this.COLUMNS; col++){

                this.board[row][col] = "";
            }
        }
    }

    public int getNumRows() {
        return ROWS;
    }

    public int getNumColumns() {
        return COLUMNS;
    }

    public int getBOXWIDTH() {
        return BOXWIDTH;
    }

    public int getBOXHEIGHT() {
        return BOXHEIGHT;
    }

    public String[] getVALIDVALUE() {
        return VALIDVALUE;
    }

    @Override
    public String toString() {
        String str = "Game Board:\n";
        for (int row = 0; row < this.ROWS; row++){
            for (int col = 0; col < this.COLUMNS; col++){

                str +=  this.board[row][col] + " ";
            }
            str += "\n";
        }

        }
    public void makeMove (int row; int col; String value;) {

        this.board[row][col] = value;
    }



}
