package org.aind.omezarr.viewer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AppView extends BorderPane {

    private final AppViewModel viewModel = new AppViewModel();

    public AppView() {
        var javaVersionLabel = new Label();
        javaVersionLabel.textProperty().bind(viewModel.getJavaVersion());

        var javaFxVersionLabel = new Label();
        javaFxVersionLabel.textProperty().bind(viewModel.getJavafxVersion());

        TabPane tabPane = new TabPane();

        Tab tab1 = new Tab("Viewer", new ViewerPane(viewModel.getViewerViewModel()));
        Tab tab2 = new Tab("Conversion"  , new Label("Conversion"));

        tabPane.getTabs().add(tab1);
        tabPane.getTabs().add(tab2);

        setCenter(tabPane);

        var javaBox = new HBox(8, new Label("Java Version:"), javaVersionLabel);
        javaBox.setAlignment(Pos.BASELINE_LEFT);
        HBox.setMargin(javaBox, new Insets(8.0d));

        var javaFxBox = new HBox(8, new Label("JavaFX Version:"), javaFxVersionLabel);
        javaFxBox.setAlignment(Pos.BASELINE_RIGHT);
        HBox.setMargin(javaFxBox, new Insets(8.0d));
        HBox.setHgrow(javaFxBox, Priority.ALWAYS);

        var footer = new HBox(javaBox, javaFxBox);
        HBox.setHgrow(footer, Priority.ALWAYS);

        setBottom(footer);

        viewModel.initialize();
    }
}
