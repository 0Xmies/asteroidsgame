package asteroids.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class App extends Application {   //test

    @Override
    public void start(Stage stage) throws IOException {
        GameState gameState = GameState.getInstance();
        Resources resources = new Resources(gameState);

        Ship ship = new Ship(Resources.getWidth() / 2, Resources.getHeight() / 2);
        resources.getPane().getChildren().add(ship.getCharacter());

        try {
            resources.loadAudioClip();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        List<Character> asteroids = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Random rnd = new Random();

            Asteroid asteroid = new Asteroid(rnd.nextInt(Resources.getWidth()), rnd.nextInt(Resources.getHeight()));
            if (asteroid.collide(ship)) {

                i--;
                continue;
            }

            asteroids.add(asteroid);
        }

        asteroids.forEach(asteroid -> resources.getPane().getChildren().add(asteroid.getCharacter()));

        List<Character> projectiles = new ArrayList<>();
        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();

        gameState.getScene().setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.SPACE && gameState.isSpaceReleased() && !gameState.isGameOver()) {
                Projectile projectile = new Projectile(
                        (int) ship.getCharacter().getTranslateX() + 15,
                        (int) ship.getCharacter().getTranslateY());

                projectile.getCharacter().setRotate(ship.getCharacter().getRotate());

                projectile.accelerate();
                projectile.setMovement(projectile.getMovement().normalize().multiply(3).add(ship.getMovement()));

                projectiles.add(projectile);
                resources.getPane().getChildren().add(projectile.getCharacter());

                gameState.setSpaceReleased(false);
                if (!gameState.isGameOver()) {
                    resources.getAudioClip().play();
                }

            } else {
                pressedKeys.put(event.getCode(), Boolean.TRUE);
            }

            if (event.getCode() == KeyCode.R) {
                cleanUp(gameState);
                restart(stage, gameState);
            }
        });

        gameState.getScene().setOnKeyReleased((event) -> {
            if (event.getCode() == KeyCode.SPACE) {
                gameState.setSpaceReleased(true);
            }
            
            pressedKeys.put((event.getCode()), Boolean.FALSE);
        });

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (asteroids.size() < 20) {
                    Random rnd = new Random();
                    Asteroid asteroid = new Asteroid(rnd.nextInt(Resources.getWidth()), rnd.nextInt(40));
                    resources.getPane().getChildren().add(asteroid.getCharacter());
                    asteroids.add(asteroid);
                }

                if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }

                if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }

                if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate();
                }

                if (pressedKeys.getOrDefault(KeyCode.DOWN, false)) {
                    ship.reverseAccelerate();
                }

                Character.checkContact(asteroids, projectiles);

                if (gameState.isStarted()) {
                    asteroids.forEach(asteroid -> {
                        if (ship.collide(asteroid)) {
                            asteroid.getCharacter().setFill(Color.RED);
                            gameState.setGameOver(true);
                            stop();
                        }
                    });

                } else {
                    asteroids.forEach(asteroid -> {
                        if (ship.collide(asteroid)) {
                            asteroid.reverseMovement();
                        }
                    });

                    resources.updateWritableImage();
                }

                projectiles.stream()
                        .filter(p -> !p.isAlive())
                        .forEach(p -> resources.getPane().getChildren().remove(p.getCharacter()));

                projectiles.removeAll(projectiles.stream()
                        .filter(p -> !p.isAlive())
                        .collect(Collectors.toList()));

                asteroids.stream()
                        .filter(a -> !a.isAlive())
                        .forEach(a -> {
                            gameState.incrementPointsValue();
                            gameState.setPointsLabel(gameState.getPointsValue());
                            resources.getPane().getChildren().remove(a.getCharacter());
                        });

                asteroids.removeAll(asteroids.stream()
                        .filter(a -> !a.isAlive())
                        .collect(Collectors.toList()));

                if (gameState.getPointsValue() >= 800 && !gameState.isLoadingDone() && resources.currentLoadDone()) {
                    try {
                        resources.loadImage();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                if (gameState.getPointsValue() >= 1000 && gameState.isLoadingDone()
                        && !gameState.isChangingBackgroundDone()) {
                    try {
                        resources.getPane().setBackground(
                                new Background(new BackgroundImage(resources.getImage(), null, null, null, null)));
                        ship.getCharacter().setFill(Color.GREEN);
                        gameState.setChangingBackgroundDone(true);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                projectiles.forEach(p -> p.move());
                asteroids.forEach(asteroid -> asteroid.move());

                ship.autoSlow();
                ship.move();
            }

        }.start();

        startGame(stage, gameState);
    }

    private void startGame(Stage stage, GameState gameState) {
        stage.setTitle("Earth Guardian - destroy the asteroids!");
        stage.setScene(gameState.getScene());
        stage.show();
    }

    private void cleanUp(GameState currentState) {
        currentState.setTries(currentState.getTries() + 1);
        currentState.setStarted(true); // instanly get rid of background generation
        currentState.reset();
    }

    private void restart(Stage stage, GameState gameState) {
        try {
            this.start(stage);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}