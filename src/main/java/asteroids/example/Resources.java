package asteroids.example;

import java.net.URL;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public final class Resources {
    private GameState gameState;
    private Image image;
    private AudioClip audioClip;
    private Thread load;
    private WritableImage writableImage;
    private Background borderPaneBackground;
    private Pane pane;
    private BorderPane welcomePane;
    private SnapshotParameters sceneParameters;
    private static int width, height;

    public Resources(GameState gameState) {
        this.gameState = gameState;

        width = 1200;
        height = 700;

        sceneParameters = new SnapshotParameters();
        sceneParameters.setViewport(new Rectangle2D(0, 0, width, height));

        pane = new Pane();
        pane.setPrefSize(width, height);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        pane.getChildren().add(gameState.getPointsLabel());

        constructWelcome();
    }

    public void loadImage() {
        load = new Thread(() -> {
            try {
                URL path = getClass().getResource("chessBoard.png");
                image = new Image(path.toString());
                gameState.setLoadingDone(true);
                load = null;
            } catch (Exception e) {
                e.getMessage();
                load = null;
            }
        });

        load.start();
    }

    public void loadAudioClip() {
        try {
            URL path = Resources.class.getResource("plasmaBlast.mp3");
            audioClip = new AudioClip(path.toString());
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public boolean currentLoadDone() {
        return load == null;
    }

    public Image getImage() {
        return image;
    }

    public AudioClip getAudioClip() {
        return audioClip;
    }

    public Thread getThread() {
        return load;
    }

    public WritableImage getWritableImage() {
        return writableImage;
    }

    public void updateWritableImage() {
        writableImage = pane.snapshot(sceneParameters, null);
        borderPaneBackground = new Background(new BackgroundImage(writableImage, null, null, null, null));
        welcomePane.setBackground(borderPaneBackground);
    }

    public Background getBorderPaneBackground() {
        return borderPaneBackground;
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

    private void constructWelcome() {
        welcomePane = new BorderPane();

        VBox box = new VBox();
        box.setAlignment(Pos.TOP_CENTER);

        Button changeSceneButton = new Button("Start the game!");
        changeSceneButton.setStyle("-fx-font-weight: bold; -fx-font-size:30");
        changeSceneButton.setAlignment(Pos.CENTER);
        changeSceneButton.setPrefSize(300, 120);
        changeSceneButton.setOnMouseClicked((event) -> {
            gameState.getScene().setRoot(getPane());
            gameState.setStarted(true);
        });

        changeSceneButton.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.SPACE) {
                gameState.getScene().setRoot(getPane());
                gameState.setStarted(true);
            }
        });

        Label gameInfo = new Label();
        gameInfo.setText("--- Welcome ---\nMovement: arrows\nShooting: spacebar\n\n\nReset game: R\nTries: " + gameState.getTries());
        gameInfo.setStyle("-fx-font-weight: bold");
        gameInfo.setTextFill(Color.DARKBLUE);
        gameInfo.setFont(Font.font(55));
        gameInfo.setAlignment(Pos.TOP_CENTER);

        box.getChildren().addAll(gameInfo, changeSceneButton);
        welcomePane.setTop(box);

        welcomePane.setBackground(borderPaneBackground);
        welcomePane.setPrefSize(width, height);
        gameState.getScene().setRoot(welcomePane);
    }

    public BorderPane getWelcomePane() {
        return welcomePane;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setWidth(int newWidth) {
        width = newWidth;
    }

    public static void setHeight(int newHeight) {
        height = newHeight;
    }

    public void setWelcomePane(BorderPane welcomePane) {
        this.welcomePane = welcomePane;
    }
}
