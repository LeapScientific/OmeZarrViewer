package org.aind.omezarr.viewer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class AppViewController {

    private final AppViewModel viewModel = new AppViewModel();

    @FXML
    public BorderPane appContent;

    @FXML
    public Label javaVersionLabel;

    @FXML
    public Label javaFxVersionLabel;

    @FXML
    public Label selectedDataPathLabel;

    @FXML
    public ToggleButton explorerMenuButton;

    public ToggleButton viewerMenuButton;

    public ToggleButton exportMenuButton;

    private VBox explorerContainerView;

    private VBox exportContainerView;

    @FXML
    public void initialize() {
        viewModel.initialize();

        javaVersionLabel.textProperty().bind(viewModel.getJavaVersion());
        javaFxVersionLabel.textProperty().bind(viewModel.getJavafxVersion());

        selectedDataPathLabel.textProperty().bind(viewModel.getFilesetLocation());
        selectedDataPathLabel.setUnderline(true);

        viewerMenuButton.setSelected(true);
        onViewerMenuClicked();
    }

    public AppViewController() {
    }

    @FXML
    public void onExplorerMenuClicked() throws IOException {
        viewerMenuButton.setSelected(false);
        exportMenuButton.setSelected(false);

        if (explorerContainerView == null) {
            explorerContainerView =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ExplorerContainerView.fxml")));

        }
        appContent.setCenter(explorerContainerView);
    }

    @FXML
    public void onViewerMenuClicked() {
        explorerMenuButton.setSelected(false);
        exportMenuButton.setSelected(false);

        var anchorPane = new AnchorPane();
        var viewer = new ViewerPane(viewModel.getViewerViewModel());

        AnchorPane.setTopAnchor(viewer, 0.0);
        AnchorPane.setLeftAnchor(viewer, 0.0);
        AnchorPane.setRightAnchor(viewer, 0.0);
        AnchorPane.setBottomAnchor(viewer, 0.0);

        anchorPane.getChildren().add(viewer);
        appContent.setCenter(anchorPane);
    }

    @FXML
    public void onExportMenuClicked() throws IOException {
        explorerMenuButton.setSelected(false);
        viewerMenuButton.setSelected(false);
        appContent.setCenter(null);

        if (exportContainerView == null) {
            exportContainerView =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ExportContainerView.fxml")));

        }
        appContent.setCenter(exportContainerView);
    }

    @FXML
    public void onSelectDataClicked(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(viewModel.getFilesetLocation().get()));
        viewModel.tryLoadOmeZarr(directoryChooser.showDialog(selectedDataPathLabel.getScene().getWindow()));}
}
