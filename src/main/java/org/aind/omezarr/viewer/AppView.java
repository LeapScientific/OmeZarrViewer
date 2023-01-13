package org.aind.omezarr.viewer;

import java.io.File;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;

public class AppView extends BorderPane {

    private final AppViewModel viewModel = new AppViewModel();

    private ImageView imageView;

    public AppView() {
        var javaVersionLabel = new Label();
        javaVersionLabel.textProperty().bind(viewModel.getJavaVersion());

        var javaFxVersionLabel = new Label();
        javaFxVersionLabel.textProperty().bind(viewModel.getJavafxVersion());

        var filesetLocationLabel = new Label();
        filesetLocationLabel.textProperty().bind(viewModel.getOmeZarrViewModel().get().getFilesetLocation());
        filesetLocationLabel.setAlignment(Pos.CENTER_LEFT);

        var button = new Button("Select Fileset");
        button.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File(viewModel.getOmeZarrViewModel().get().getFilesetLocation().get()));
            viewModel.tryLoadOmeZarr(directoryChooser.showDialog(this.getScene().getWindow()));
        });
        button.setAlignment(Pos.CENTER_RIGHT);

        var buttonBox = new HBox(button);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(buttonBox, Priority.ALWAYS);

        var buttonHeader = new HBox(filesetLocationLabel, buttonBox);
        HBox.setMargin(buttonHeader, new Insets(8.0d));
        VBox.setMargin(buttonHeader, new Insets(8.0d));

        var datasetLabel = new Label();
        datasetLabel.textProperty().bind(viewModel.getOmeZarrViewModel().get().imageSliceViewModel.datasetIndexProperty.asString());

        var datasetSlider = new Slider();
        datasetSlider.setMax(14);
        datasetSlider.valueProperty().bindBidirectional(viewModel.getOmeZarrViewModel().get().imageSliceViewModel.datasetIndexProperty);

        var loadDurationLabel = new Label();
        loadDurationLabel.textProperty().bind(viewModel.getOmeZarrViewModel().get().lastLoadDurationProperty);

        var controlsHeader = new HBox(datasetLabel, datasetSlider, loadDurationLabel);

        var header = new VBox(buttonHeader, controlsHeader);

        setTop(header);

        var footer = new HBox(new Label("Java Version:"), javaVersionLabel, new Label("JavaFX Version:"), javaFxVersionLabel);
        HBox.setHgrow(footer, Priority.ALWAYS);
        VBox.setMargin(footer, new Insets(8.0d));

        setBottom(footer);

        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(widthProperty());
        imageView.fitHeightProperty().bind(heightProperty());
        imageView.imageProperty().bind(viewModel.getOmeZarrViewModel().get().getImageProperty());

        setCenter(imageView);

        viewModel.initialize();
    }
}
