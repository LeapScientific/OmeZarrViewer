package org.aind.omezarr.viewer;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import org.aind.omezarr.OmeZarrDataset;
import org.aind.omezarr.OmeZarrGroup;
import org.aind.omezarr.image.OmeZarrImage;
import org.aind.omezarr.image.OmeZarrImageStack;
import org.aind.omezarr.image.TCZYXRasterZStack;
import org.aind.omezarr.util.PerformanceMetrics;
import ucar.ma2.InvalidRangeException;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

public class OmeZarrViewModel {
    public final StringProperty filesetLocation = new SimpleStringProperty("Select an Ome-Zarr fileset");

    public final ObjectProperty<WritableImage> imageProperty = new SimpleObjectProperty<>();

    public final ImageSliceViewModel imageSliceViewModel = new ImageSliceViewModel();

    public final BooleanProperty isLoadingProperty = new SimpleBooleanProperty(false);

    public final StringProperty lastLoadDurationProperty = new SimpleStringProperty("---");

    private OmeZarrGroup fileset;

    private BufferedImage currentImage;

    private Duration loadDuration;

    private final ColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY), false, true, Transparency.OPAQUE, DataBuffer.TYPE_USHORT);

    public OmeZarrViewModel() {
        imageSliceViewModel.datasetIndexProperty.addListener((o, p, n) -> {
            try {
                var prevDatasetShape = fileset.getAttributes().getMultiscales()[0].getDatasets().get(p.intValue()).getShape();

                var newDatasetShape = fileset.getAttributes().getMultiscales()[0].getDatasets().get(n.intValue()).getShape();

                if (prevDatasetShape[2] == 1) {
                    imageSliceViewModel.zIndexProperty.set((int) (newDatasetShape[2] / 2.0));
                } else {
                    double percent = (1.0 * imageSliceViewModel.zIndexProperty.get() / prevDatasetShape[2]);

                    imageSliceViewModel.zIndexProperty.set((int) (newDatasetShape[2] * percent));
                }

                updateCurrentSlice();
            } catch (Exception ex) {

            }
        });
    }

    public StringProperty getFilesetLocation() {
        return filesetLocation;
    }

    public ObjectProperty<WritableImage> getImageProperty() {
        return imageProperty;
    }

    public void tryLoadOmeZarr(File directory) {
        Path path = Paths.get(directory.getAbsolutePath());

        if (!Files.exists(path)) {
            return;
        }

        try {
            isLoadingProperty.set(true);

            fileset = OmeZarrGroup.open(path);

            filesetLocation.setValue(path.toString());

            var dataIndex = fileset.getAttributes().getMultiscales()[0].getDatasets().size() - 1;

            imageSliceViewModel.datasetIndexProperty.set(dataIndex);

            isLoadingProperty.set(false);

            updateCurrentSlice();

        } catch (Exception ex) {
            fileset = null;

            System.out.println(ex.getMessage());
        }
    }

    private void updateCurrentSlice() {
        if (isLoadingProperty.get()) {
            return;
        }

        new Thread(() -> {
            try {
                OmeZarrDataset dataset = fileset.getAttributes().getMultiscales()[0].getDatasets().get(imageSliceViewModel.datasetIndexProperty.get());

                if (dataset.isValid()) {

                    int zIndex = imageSliceViewModel.zIndexProperty.get();

                    // loadSingleImage(dataset, zIndex);

                    // loadMultireadImageStack(dataset, zIndex);

                    loadAsStack(dataset, zIndex);
                } else {
                    loadDuration = null;

                    currentImage = null;
                }

                Platform.runLater(() -> {
                    if (currentImage != null) {
                        imageProperty.setValue(SwingFXUtils.toFXImage(currentImage, null));
                    } else {
                        imageProperty.setValue(null);
                    }

                    if (loadDuration != null) {
                        lastLoadDurationProperty.set(loadDuration.toString());
                    } else {
                        lastLoadDurationProperty.set("failed");
                    }
                });
            } catch (Exception ex) {
                imageProperty.setValue(null);

                System.out.println(ex.getMessage());
            }
        }).start();
    }

    private void loadSingleImage(OmeZarrDataset dataset, int zIndex) throws IOException, InvalidRangeException {
        OmeZarrImage omeZarrImage = new OmeZarrImage(dataset, imageSliceViewModel.timeIndexProperty.get(), imageSliceViewModel.channelIndexProperty.get(), zIndex);

        currentImage = omeZarrImage.asImage(true);
    }

    private void loadMultireadImageStack(OmeZarrDataset dataset, int zIndex) throws IOException, InvalidRangeException {
        OmeZarrImageStack stack = new OmeZarrImageStack(dataset);

        int[] offset = {0, 0, 0, 0, 0};

        var slices = stack.asSlices(dataset.getShape(), offset, true);

        currentImage = new BufferedImage(colorModel, slices[zIndex], colorModel.isAlphaPremultiplied(), null);
    }

    private final boolean loadSubset = false;

    private void loadAsStack(OmeZarrDataset dataset, int zIndex) throws IOException {
        if (loadSubset) {
            loadAsStackSubset(dataset, zIndex);

            return;
        }

        int[] fullSize = dataset.getShape();

        int[] shape = {1, 1, fullSize[2], fullSize[3], fullSize[4]};
        int[] offset = {0, 0, 0, 0, 0};

        if (shape[2] > 213) {
            shape[2] = 213;

            if (zIndex >= 213) {
                zIndex = 212;
            }
        }
        if (shape[3] > 175) {
            shape[3] = 175;
        }
        if (shape[4] > 408) {
            shape[4] = 408;
        }

        var metrics = new PerformanceMetrics();

        var start = Instant.now();

        var slices = TCZYXRasterZStack.fromDataset(dataset, shape, offset, false, true, null, metrics);

        loadDuration = Duration.between(start, Instant.now());

        currentImage = new BufferedImage(colorModel, slices[zIndex], colorModel.isAlphaPremultiplied(), null);
    }

    private final int subsetCount = 1;

    private void loadAsStackSubset(OmeZarrDataset dataset, int zIndex) throws IOException {
        int[] fullSize = dataset.getShape();

        int subset = Math.min(fullSize[2], subsetCount);
        int subsetOffset = Math.max(0, (subset - 1) / 2);

        int[] shape = {1, 1, subset, fullSize[3], fullSize[4]};
        int[] offset = {0, 0, Math.max(0, zIndex - subsetOffset), 0, 0};

        var metrics = new PerformanceMetrics();

        var slices = TCZYXRasterZStack.fromDataset(dataset, shape, offset, false, true, null, metrics);

        currentImage = new BufferedImage(colorModel, slices[subsetOffset], colorModel.isAlphaPremultiplied(), null);
    }
}
