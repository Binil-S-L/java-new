package sudoku.problemdomain;

import sudoku.constants.GameState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuUtilities {

    private static final int GRID_SIZE = SudokuGame.GRID_BOUNDARY;
    private static final int CELLS_TO_REMOVE = 45;

    /**
     * Deep-copies a 9x9 int array into a new array.
     */
    public static int[][] copyToNewArray(int[][] old) {
        int[][] copy = new int[GRID_SIZE][GRID_SIZE];
        for (int x = 0; x < GRID_SIZE; x++) {
            System.arraycopy(old[x], 0, copy[x], 0, GRID_SIZE);
        }
        return copy;
    }

    /**
     * Generates a new SudokuGame with a valid puzzle.
     * Fills the board using backtracking, then removes CELLS_TO_REMOVE cells.
     * The returned game has a grid where 0 means "empty / user-fillable".
     */
    public static SudokuGame generateNewPuzzle() {
        int[][] grid = new int[GRID_SIZE][GRID_SIZE];

        // Fill with a valid complete solution using backtracking
        fillBoard(grid);

        // The solved grid is the reference; we will remove cells to make the puzzle
        int[][] puzzle = copyToNewArray(grid);
        removeCells(puzzle);

        return new SudokuGame(GameState.NEW, puzzle);
    }

    /**
     * Checks whether the given 9x9 grid is fully and correctly filled.
     * Returns COMPLETE if valid and complete, ACTIVE otherwise.
     */
    public static GameState checkForCompletion(int[][] grid) {
        // Check all cells are non-zero
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                if (grid[x][y] == 0) {
                    return GameState.ACTIVE;
                }
            }
        }

        // Validate every row (x = row index, y = col index)
        for (int x = 0; x < GRID_SIZE; x++) {
            if (!isValidGroup(grid[x])) {
                return GameState.ACTIVE;
            }
        }

        // Validate every column
        for (int y = 0; y < GRID_SIZE; y++) {
            int[] col = new int[GRID_SIZE];
            for (int x = 0; x < GRID_SIZE; x++) {
                col[x] = grid[x][y];
            }
            if (!isValidGroup(col)) {
                return GameState.ACTIVE;
            }
        }

        // Validate every 3x3 box
        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                int[] box = new int[GRID_SIZE];
                int idx = 0;
                for (int x = boxRow * 3; x < boxRow * 3 + 3; x++) {
                    for (int y = boxCol * 3; y < boxCol * 3 + 3; y++) {
                        box[idx++] = grid[x][y];
                    }
                }
                if (!isValidGroup(box)) {
                    return GameState.ACTIVE;
                }
            }
        }

        return GameState.COMPLETE;
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Returns true if the 9-element array contains each of 1-9 exactly once.
     */
    private static boolean isValidGroup(int[] group) {
        boolean[] seen = new boolean[GRID_SIZE + 1];
        for (int val : group) {
            if (val < 1 || val > GRID_SIZE) return false;
            if (seen[val]) return false;
            seen[val] = true;
        }
        return true;
    }

    /**
     * Fills grid using recursive backtracking with randomised digit order.
     */
    private static boolean fillBoard(int[][] grid) {
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                if (grid[x][y] == 0) {
                    List<Integer> digits = shuffledDigits();
                    for (int digit : digits) {
                        if (isPlacementValid(grid, x, y, digit)) {
                            grid[x][y] = digit;
                            if (fillBoard(grid)) {
                                return true;
                            }
                            grid[x][y] = 0;
                        }
                    }
                    return false; // trigger backtrack
                }
            }
        }
        return true; // all cells filled
    }

    /**
     * Checks whether placing `digit` at (row, col) violates any Sudoku rule.
     */
    private static boolean isPlacementValid(int[][] grid, int row, int col, int digit) {
        // Check row
        for (int y = 0; y < GRID_SIZE; y++) {
            if (grid[row][y] == digit) return false;
        }
        // Check column
        for (int x = 0; x < GRID_SIZE; x++) {
            if (grid[x][col] == digit) return false;
        }
        // Check 3x3 box
        int boxRowStart = (row / 3) * 3;
        int boxColStart = (col / 3) * 3;
        for (int x = boxRowStart; x < boxRowStart + 3; x++) {
            for (int y = boxColStart; y < boxColStart + 3; y++) {
                if (grid[x][y] == digit) return false;
            }
        }
        return true;
    }

    /**
     * Returns a shuffled list of digits 1-9.
     */
    private static List<Integer> shuffledDigits() {
        List<Integer> digits = new ArrayList<>();
        for (int i = 1; i <= GRID_SIZE; i++) {
            digits.add(i);
        }
        Collections.shuffle(digits, new Random());
        return digits;
    }

    /**
     * Randomly removes CELLS_TO_REMOVE cells from a completed grid (sets them to 0).
     */
    private static void removeCells(int[][] grid) {
        Random random = new Random();
        int removed = 0;
        while (removed < CELLS_TO_REMOVE) {
            int x = random.nextInt(GRID_SIZE);
            int y = random.nextInt(GRID_SIZE);
            if (grid[x][y] != 0) {
                grid[x][y] = 0;
                removed++;
            }
        }
    }
}
