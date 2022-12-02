package org.aind.omezarr.viewer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        var scene = new Scene(new AppView(), 1280, 960);

        stage.setTitle("OME Zarr Viewer");

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
