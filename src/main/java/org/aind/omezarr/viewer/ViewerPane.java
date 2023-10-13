package org.aind.omezarr.viewer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ViewerPane extends BorderPane {

    public ViewerPane(ViewerViewModel viewModel) {

        var datasetLabel = new Label();
        datasetLabel.textProperty().bind(viewModel.getOmeZarrViewModel().get().imageSliceViewModel.datasetIndexProperty.asString());

        var datasetSlider = new Slider();
        datasetSlider.setShowTickLabels(true);
        datasetSlider.setShowTickMarks(true);
        datasetSlider.minProperty().bind(viewModel.getOmeZarrViewModel().get().minDatasetIndexProperty);
        datasetSlider.maxProperty().bind(viewModel.getOmeZarrViewModel().get().maxDatasetIndexProperty);
        datasetSlider.valueProperty().bindBidirectional(viewModel.getOmeZarrViewModel().get().imageSliceViewModel.datasetIndexProperty);

        var datasetHeader = new HBox(datasetLabel, datasetSlider);
        datasetHeader.setSpacing(8);

        var loadDurationLabel = new Label();
        loadDurationLabel.textProperty().bind(viewModel.getOmeZarrViewModel().get().lastLoadDurationProperty);

        var loadProgressIndicator = new ProgressIndicator();
        loadProgressIndicator.visibleProperty().bind(viewModel.getOmeZarrViewModel().get().isLoadingProperty);

        var durationHeader = new HBox(loadDurationLabel, loadProgressIndicator);
        durationHeader.setSpacing(8);

        var channelLabel = new Label();
        channelLabel.textProperty().bind(viewModel.getOmeZarrViewModel().get().imageSliceViewModel.channelIndexProperty.asString());

        var channelSlider = new Slider();
        channelSlider.setShowTickLabels(true);
        channelSlider.setShowTickMarks(true);
        channelSlider.minProperty().bind(viewModel.getOmeZarrViewModel().get().minChannelIndexProperty);
        channelSlider.maxProperty().bind(viewModel.getOmeZarrViewModel().get().maxChannelIndexProperty);
        channelSlider.valueProperty().bindBidirectional(viewModel.getOmeZarrViewModel().get().imageSliceViewModel.channelIndexProperty);

        var channelHeader = new HBox(channelLabel, channelSlider);
        channelHeader.setSpacing(8);

        var controlsHeader = new VBox(datasetHeader, durationHeader, channelHeader);
        controlsHeader.setSpacing(8);

        var header = new VBox(controlsHeader);

        setTop(header);

        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(widthProperty());
        imageView.fitHeightProperty().bind(heightProperty());
        imageView.imageProperty().bind(viewModel.getOmeZarrViewModel().get().getImageProperty());

        setCenter(imageView);
    }
}
