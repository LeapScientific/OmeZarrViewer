package org.aind.omezarr.viewer;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.*;

import java.io.IOException;
import java.util.Objects;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // var scene = new Scene(new AppView(), 1280, 960);

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AppView.fxml")));
        stage.setTitle("OME Zarr Viewer");

        stage.setScene(new Scene(root, 1000, 800));

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
