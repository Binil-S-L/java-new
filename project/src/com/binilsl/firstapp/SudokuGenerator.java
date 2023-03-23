package com.binilsl.firstapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {

    public static SudokuPuzzle generateRandomSudoku(SudokuPuzzleType puzzleType){
        SudokuPuzzle puzzle = new SudokuPuzzle(puzzleType.getRows(), puzzleType.getColumns(), puzzleType.getBoxWidth(),puzzleType.getBoxHeight(), puzzleType.getValidValue());

        Random randomGenerator = new Random();
        List<String> notUsedValidValue = new ArrayList<>(Arrays.asList(puzzle.getVALIDVALUE()));
        for (int r = 0; r < puzzle.getNumRows(); r++){
            int randomValue = randomGenerator.nextInt(notUsedValidValue.size());
            puzzle.makeMove(r, 0, notUsedValidValue.get(randomValue));
            notUsedValidValue.remove(randomValue);
        }
        return puzzle;
    }
}
