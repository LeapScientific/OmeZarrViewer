package org.aind.omezarr.viewer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class ViewerPane extends BorderPane {
    private ViewerViewModel viewModel;

    private ImageView imageView;

    public ViewerPane(ViewerViewModel viewModel) {
        this.viewModel = viewModel;

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
        datasetSlider.setShowTickLabels(true);
        datasetSlider.setShowTickMarks(true);
        datasetSlider.minProperty().bind(viewModel.getOmeZarrViewModel().get().minDatasetIndexProperty);
        datasetSlider.maxProperty().bind(viewModel.getOmeZarrViewModel().get().maxDatasetIndexProperty);;
        datasetSlider.valueProperty().bindBidirectional(viewModel.getOmeZarrViewModel().get().imageSliceViewModel.datasetIndexProperty);

        var loadDurationLabel = new Label();
        loadDurationLabel.textProperty().bind(viewModel.getOmeZarrViewModel().get().lastLoadDurationProperty);

        var loadProgressIndicator = new ProgressIndicator();
        loadProgressIndicator.visibleProperty().bind(viewModel.getOmeZarrViewModel().get().isLoadingProperty);

        var controlsHeader = new HBox(datasetLabel, datasetSlider, loadDurationLabel, loadProgressIndicator);

        var header = new VBox(buttonHeader, controlsHeader);

        setTop(header);

        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(widthProperty());
        imageView.fitHeightProperty().bind(heightProperty());
        imageView.imageProperty().bind(viewModel.getOmeZarrViewModel().get().getImageProperty());

        setCenter(imageView);

        viewModel.initialize();
    }
}
