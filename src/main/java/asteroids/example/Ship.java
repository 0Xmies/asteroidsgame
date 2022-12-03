package asteroids.example;

import javafx.scene.shape.Polygon;

public class Ship extends Character {

    public Ship(int x, int y) {
        super(new Polygon(-15, -15, 45, 0, -15, 15), x, y);
    }
}
