package org.aind.omezarr.viewer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;

import java.io.File;
import java.util.prefs.Preferences;

public class AppViewModel {

    private final StringProperty javaVersion = new SimpleStringProperty(System.getProperty("java.version"));

    private final StringProperty javafxVersion = new SimpleStringProperty(System.getProperty("javafx.version"));

    private final StringProperty filesetLocation = new SimpleStringProperty("Select an Ome-Zarr data location to begin");

    private final ViewerViewModel viewerViewModel = new ViewerViewModel();

    private final static String LAST_DIRECTORY_PREF = "LAST_DIRECTORY_PREF";

    public StringProperty getJavaVersion() {
        return javaVersion;
    }

    public StringProperty getJavafxVersion() {
        return javafxVersion;
    }

    public StringProperty getFilesetLocation() {
        return filesetLocation;
    }

    public ViewerViewModel getViewerViewModel() {
        return viewerViewModel;
    }

    public AppViewModel() {
    }

    public void initialize() {
        Preferences prefs = Preferences.userRoot().node("org.aind.omezarr.viewer");

        String directory = prefs.get(LAST_DIRECTORY_PREF, "");

        if (!directory.isBlank()) {
            tryLoadOmeZarr(new File(directory));
        }
    }

    public void tryLoadOmeZarr(File directory) {
        if (directory != null) {
            viewerViewModel.getOmeZarrViewModel().get().tryLoadOmeZarr(directory);

            filesetLocation.setValue(directory.getAbsolutePath());

            Preferences prefs = Preferences.userRoot().node("org.aind.omezarr.viewer");

            prefs.put(LAST_DIRECTORY_PREF, directory.getAbsolutePath());
        }
    }
}
