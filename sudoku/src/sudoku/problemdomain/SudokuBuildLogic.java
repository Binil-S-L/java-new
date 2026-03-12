package sudoku.problemdomain;

import sudoku.constants.GameState;
import sudoku.constants.Messages;

import java.io.IOException;

/**
 * Wires storage, game state, and UI together.
 * Acts as the EventListener (controller) for the UI.
 */
public class SudokuBuildLogic implements IUserInterfaceContract.EventListener {

    private final IStorage storage;
    private final IUserInterfaceContract.View view;

    // The current grid as the user is editing it (given cells are preserved)
    private SudokuGame currentGame;

    // Tracks which cells were given at puzzle start (these cannot be changed)
    private final boolean[][] givenCells = new boolean[SudokuGame.GRID_BOUNDARY][SudokuGame.GRID_BOUNDARY];

    private SudokuBuildLogic(IStorage storage, IUserInterfaceContract.View view) {
        this.storage = storage;
        this.view = view;
    }

    /**
     * Entry point called from SudokuApplication.
     * Loads (or generates) a game, registers this object as the listener, and
     * renders the initial board.
     */
    public static void build(IUserInterfaceContract.View view) throws IOException {
        IStorage storage = new LocalStorageImpl();
        SudokuBuildLogic logic = new SudokuBuildLogic(storage, view);

        view.setListener(logic);

        SudokuGame game = storage.getGameData();
        logic.currentGame = game;
        logic.markGivenCells(game.getGridState());
        view.updateBoard(game);
    }

    // -------------------------------------------------------------------------
    // EventListener implementation
    // -------------------------------------------------------------------------

    @Override
    public void onSudokuInput(int x, int y, int input) {
        // Ignore attempts to overwrite a given cell
        if (givenCells[x][y]) return;

        try {
            int[][] updatedGrid = currentGame.getGridState();
            updatedGrid[x][y] = input;

            GameState newState = SudokuUtilities.checkForCompletion(updatedGrid);
            currentGame = new SudokuGame(newState, updatedGrid);

            storage.updateGameData(currentGame);

            if (newState == GameState.COMPLETE) {
                view.showDialog(Messages.GAME_COMPLETE);
            } else {
                view.updateSquare(x, y, input);
            }
        } catch (IOException e) {
            view.showError(Messages.ERROR);
        }
    }

    @Override
    public void onDialogClick() {
        // Start a new game
        try {
            SudokuGame newGame = SudokuUtilities.generateNewPuzzle();
            storage.updateGameData(newGame);
            currentGame = newGame;
            markGivenCells(newGame.getGridState());
            view.updateBoard(newGame);
        } catch (IOException e) {
            view.showError(Messages.ERROR);
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Records which cells are pre-filled (given) so they cannot be overwritten.
     */
    private void markGivenCells(int[][] grid) {
        for (int x = 0; x < SudokuGame.GRID_BOUNDARY; x++) {
            for (int y = 0; y < SudokuGame.GRID_BOUNDARY; y++) {
                givenCells[x][y] = (grid[x][y] != 0);
            }
        }
    }
}
