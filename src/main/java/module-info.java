module asteroids.example {
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.media;

    opens asteroids.example to javafx.fxml;
    exports asteroids.example;
}
