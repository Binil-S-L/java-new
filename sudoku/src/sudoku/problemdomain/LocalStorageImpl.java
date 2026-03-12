package sudoku.problemdomain;

import sudoku.constants.GameState;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Persists SudokuGame state to ~/.sudoku_game.dat using Java serialization.
 * If the file does not exist (first launch), returns a freshly generated game.
 */
public class LocalStorageImpl implements IStorage {

    private static final String SAVE_FILE = System.getProperty("user.home")
            + File.separator + ".sudoku_game.dat";

    @Override
    public void updateGameData(SudokuGame game) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(game);
        }
    }

    @Override
    public SudokuGame getGameData() throws IOException {
        Path path = Paths.get(SAVE_FILE);
        if (!Files.exists(path)) {
            // First launch – generate a brand-new puzzle
            return SudokuUtilities.generateNewPuzzle();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(SAVE_FILE))) {
            return (SudokuGame) ois.readObject();
        } catch (ClassNotFoundException e) {
            // Saved data is incompatible; start fresh
            return SudokuUtilities.generateNewPuzzle();
        }
    }
}
