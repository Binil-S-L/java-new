package sudoku.problemdomain;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import sudoku.constants.Messages;

import java.util.Optional;

/**
 * JavaFX implementation of the Sudoku UI.
 *
 * Layout:
 *   VBox
 *     GridPane (9x9 TextFields)
 *     Button "New Game"
 *
 * Grid cells:
 *   - Given (pre-filled) cells: grey background, dark text, not editable.
 *   - User cells:               white background, blue text, editable (1-9 only).
 *
 * Thick borders separate the nine 3x3 boxes.
 *
 * The `updating` flag prevents the change-listener on TextFields from firing
 * back into the EventListener while the board is being programmatically updated.
 */
public class UserInterfaceImpl implements IUserInterfaceContract.View {

    private static final int GRID_BOUNDARY = SudokuGame.GRID_BOUNDARY;

    // grid[x][y]: x = row (0-8 top-to-bottom), y = col (0-8 left-to-right)
    private final TextField[][] textFields = new TextField[GRID_BOUNDARY][GRID_BOUNDARY];

    private IUserInterfaceContract.EventListener listener;

    // True while we are programmatically filling the board; suppresses user callbacks
    private boolean updating = false;

    public UserInterfaceImpl(Stage primaryStage) {
        GridPane gridPane = buildGrid();

        Button newGameButton = new Button(Messages.NEW_GAME);
        newGameButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        newGameButton.setOnAction(e -> {
            if (listener != null) listener.onDialogClick();
        });

        VBox root = new VBox(16, gridPane, newGameButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f0f0;");

        Scene scene = new Scene(root);
        primaryStage.setTitle("Sudoku");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // -------------------------------------------------------------------------
    // IUserInterfaceContract.View
    // -------------------------------------------------------------------------

    @Override
    public void setListener(IUserInterfaceContract.EventListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateSquare(int x, int y, int input) {
        updating = true;
        try {
            TextField tf = textFields[x][y];
            tf.setText(input == 0 ? "" : String.valueOf(input));
        } finally {
            updating = false;
        }
    }

    @Override
    public void updateBoard(SudokuGame game) {
        updating = true;
        try {
            int[][] grid = game.getGridState();
            for (int x = 0; x < GRID_BOUNDARY; x++) {
                for (int y = 0; y < GRID_BOUNDARY; y++) {
                    int val = grid[x][y];
                    TextField tf = textFields[x][y];
                    if (val == 0) {
                        // User-editable cell
                        tf.setText("");
                        styleUserCell(tf);
                    } else {
                        // Given cell
                        tf.setText(String.valueOf(val));
                        styleGivenCell(tf);
                    }
                }
            }
        } finally {
            updating = false;
        }
    }

    @Override
    public void showDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Messages.GAME_COMPLETE_TITLE);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType newGameType = new ButtonType("New Game");
        alert.getButtonTypes().setAll(newGameType);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && listener != null) {
            listener.onDialogClick();
        }
    }

    @Override
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // -------------------------------------------------------------------------
    // Grid construction
    // -------------------------------------------------------------------------

    private GridPane buildGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        for (int x = 0; x < GRID_BOUNDARY; x++) {
            for (int y = 0; y < GRID_BOUNDARY; y++) {
                TextField tf = new TextField();
                tf.setPrefWidth(52);
                tf.setPrefHeight(52);
                tf.setAlignment(Pos.CENTER);
                tf.setFont(Font.font("Arial", FontWeight.BOLD, 18));

                styleUserCell(tf);
                applyBorder(tf, x, y);

                final int row = x;
                final int col = y;

                tf.textProperty().addListener((obs, oldVal, newVal) -> {
                    if (updating) return;
                    if (listener == null) return;

                    if (newVal == null || newVal.isEmpty()) {
                        listener.onSudokuInput(row, col, 0);
                        return;
                    }

                    // Accept only a single digit 1-9
                    if (newVal.matches("[1-9]")) {
                        listener.onSudokuInput(row, col, Integer.parseInt(newVal));
                    } else {
                        // Revert invalid input without triggering another event
                        updating = true;
                        try {
                            tf.setText(oldVal != null && oldVal.matches("[1-9]") ? oldVal : "");
                        } finally {
                            updating = false;
                        }
                    }
                });

                textFields[x][y] = tf;
                // GridPane.add(node, colIndex, rowIndex)
                grid.add(tf, y, x);
            }
        }

        return grid;
    }

    // -------------------------------------------------------------------------
    // Cell styling helpers
    // -------------------------------------------------------------------------

    private void styleGivenCell(TextField tf) {
        tf.setEditable(false);
        tf.setStyle(
                "-fx-background-color: #cccccc;" +
                "-fx-text-fill: #222222;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 18px;" +
                "-fx-alignment: center;"
        );
    }

    private void styleUserCell(TextField tf) {
        tf.setEditable(true);
        tf.setStyle(
                "-fx-background-color: #ffffff;" +
                "-fx-text-fill: #1a6fe8;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 18px;" +
                "-fx-alignment: center;"
        );
    }

    /**
     * Applies thick borders on the edges of each 3x3 box and thin borders
     * for inner cell separators.
     *
     * Border insets order: top, right, bottom, left
     */
    private void applyBorder(TextField tf, int row, int col) {
        double top    = (row % 3 == 0) ? 3.0 : 0.5;
        double left   = (col % 3 == 0) ? 3.0 : 0.5;
        double bottom = (row == GRID_BOUNDARY - 1) ? 3.0 : 0.5;
        double right  = (col == GRID_BOUNDARY - 1) ? 3.0 : 0.5;

        Color color = Color.web("#333333");

        tf.setBorder(new Border(new BorderStroke(
                color,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(top, right, bottom, left)
        )));
    }
}
