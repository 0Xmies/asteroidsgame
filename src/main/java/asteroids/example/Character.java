package asteroids.example;

import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public abstract class Character {
    private Polygon character;
    private Point2D movement;
    private boolean alive;

    public Character(Polygon polygon, int x, int y) {
        character = polygon;
        character.setTranslateX(x);
        character.setTranslateY(y);

        movement = new Point2D(0, 0);
        alive = true;
    }

    public Polygon getCharacter() {
        return character;
    }

    public void turnLeft() {
        character.setRotate(character.getRotate() - 5);
    }

    public void turnRight() {
        character.setRotate(character.getRotate() + 5);
    }

    public Point2D getMovement() {
        return movement;
    }

    public void reverseMovement() {
        movement = new Point2D(-movement.getX(), -movement.getY());
    }

    public void setMovement(Point2D speed) {
        movement = movement.add(speed);
    }

    public void setAlive(boolean isAlive) {
        alive = isAlive;
    }

    public boolean isAlive() {
        return alive;
    }

    public static void checkContact(List<Character> characters, List<Character> otherCharacters) {
        for (Character character : characters) {
            for (Character character2 : otherCharacters) {
                if (character.collide(character2)) {
                    character.setAlive(false);
                    character2.setAlive(false);
                }
            }
        }
    }

    public void move() {
        character.setTranslateX(character.getTranslateX() + movement.getX());
        character.setTranslateY(character.getTranslateY() + movement.getY());

        if (character.getTranslateX() < 0) {
            character.setTranslateX(character.getTranslateX() + Resources.getWidth());
        }

        if (character.getTranslateX() > Resources.getWidth()) {
            character.setTranslateX(character.getTranslateX() % Resources.getWidth());
        }

        if (character.getTranslateY() < 0) {
            character.setTranslateY(character.getTranslateY() + Resources.getHeight());
        }

        if (character.getTranslateY() > Resources.getHeight()) {
            character.setTranslateY(character.getTranslateY() % Resources.getHeight());
        }
    }

    private void accelerate(boolean reverse) {
        double changeX = Math.cos(Math.toRadians(character.getRotate()));
        double changeY = Math.sin(Math.toRadians(character.getRotate()));

        changeX *= 0.1;
        changeY *= 0.1;

        if (reverse) {
            changeX = -changeX;
            changeY = -changeY;
        }

        movement = movement.add(changeX, changeY);
    }

    public void accelerate() {
        accelerate(false);
    }

    public void reverseAccelerate() {
        accelerate(true);
    }

    public void autoSlow() {
        if (movement.getX() > 0) {
            movement = movement.add(-0.01, 0);
        }

        if (movement.getY() > 0) {
            movement = movement.add(0, -0.01);
        }

        if (movement.getX() < 0) {
            movement = movement.add(0.01, 0);
        }

        if (movement.getY() < 0) {
            movement = movement.add(0, 0.01);
        }
    }

    public boolean collide(Character other) {
        Shape collisionArea = Shape.intersect(character, other.getCharacter());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }
}
