package asteroids.example;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public final class GameState {
    private static GameState instance;
    private boolean spaceReleased = true;
    private boolean gameOver = false;
    private boolean loadingDone = false;
    private boolean changingBackgroundDone = false;
    private boolean gameStarted = false;
    private Label pointsLabel = new Label("0");
    private int pointsValue;
    public Scene scene;
    public int tries;

    private GameState(int tries) {
        pointsLabel.setFont(Font.font(100));
        scene = new Scene(new Parent() {
        
        });
        
        this.tries = tries;
    }

    private GameState() {
        this(0);
    }

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }

        return instance;
    }

    public boolean isSpaceReleased() {
        return spaceReleased;
    }

    public void setSpaceReleased(boolean isReleased) {
        spaceReleased = isReleased;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean isOver) {
        gameOver = isOver;
    }

    public boolean isLoadingDone() {
        return loadingDone;
    }

    public void setLoadingDone(boolean isDone) {
        loadingDone = isDone;
    }

    public boolean isChangingBackgroundDone() {
        return changingBackgroundDone;
    }

    public void setChangingBackgroundDone(boolean isDone) {
        changingBackgroundDone = isDone;
    }

    public Label getPointsLabel() {
        return pointsLabel;
    }

    public void setPointsLabel(int points) {
        pointsLabel.setText(String.valueOf(pointsValue));
    }

    public int getPointsValue() {
        return pointsValue;
    }

    public void incrementPointsValue() {
        pointsValue = pointsValue + 100;
    }

    public boolean isStarted() {
        return gameStarted;
    }

    public void setStarted(boolean isStarted) {
        gameStarted = isStarted;
    }

    public void reset() {
        int tries = instance.getTries();
        instance = new GameState(tries);
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public Scene getScene() {
        return scene;
    }
}
