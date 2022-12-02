package org.aind.omezarr.viewer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import org.aind.omezarr.OmeZarrGroup;
import org.aind.omezarr.image.OmeZarrImage;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppView extends BorderPane {

    private final AppViewModel viewModel = new AppViewModel();

    private ImageView imageView;

    public AppView() {
        var javaVersionLabel = new Label();
        javaVersionLabel.textProperty().bind(viewModel.getJavaVersion());

        var javaFxVersionLabel = new Label();
        javaFxVersionLabel.textProperty().bind(viewModel.getJavafxVersion());

        var filesetLocationLabel = new Label();
        filesetLocationLabel.textProperty().bind(viewModel.getFilesetLocation());
        filesetLocationLabel.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button("Select Fileset");
        button.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            viewModel.setFilesetDirectory(directoryChooser.showDialog(this.getScene().getWindow()));
        });
        button.setAlignment(Pos.CENTER_RIGHT);

        HBox buttonBox = new HBox(button);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(buttonBox, Priority.ALWAYS);

        HBox header = new HBox(filesetLocationLabel, buttonBox);
        VBox.setMargin(header, new Insets(8.0d));

        setTop(header);

        HBox footer = new HBox(new Label("Java Version:"), javaVersionLabel, new Label("JavaFX Version:"), javaFxVersionLabel);
        HBox.setHgrow(footer, Priority.ALWAYS);
        VBox.setMargin(footer, new Insets(8.0d));

        setBottom(footer);

        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(widthProperty());
        imageView.fitHeightProperty().bind(heightProperty());

        setCenter(imageView);

        viewModel.getFilesetLocation().addListener((observableValue, s, t1) -> loadFileset());

        loadFileset();
    }

    private void loadFileset() {
        Path path = Paths.get(viewModel.getFilesetLocation().getValue());

        if (!Files.exists(path)) {
            return;
        }

        try {
            OmeZarrGroup fileset = OmeZarrGroup.open(path);
            OmeZarrImage omeZarrImage = new OmeZarrImage(fileset.getAttributes().getMultiscales()[0].getDatasets()[0], 0, 0, 0);
            BufferedImage image = omeZarrImage.asImage( true);
            WritableImage fxImage = javafx.embed.swing.SwingFXUtils.toFXImage(image, null);
            imageView.setImage(fxImage);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
