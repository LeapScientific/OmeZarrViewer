package org.aind.omezarr.viewer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.util.prefs.Preferences;

public class AppViewModel {
    private final StringProperty javaVersion = new SimpleStringProperty("");

    private final StringProperty javafxVersion = new SimpleStringProperty("");

    private final StringProperty filesetLocation = new SimpleStringProperty("Select an Ome-Zarr fileset");

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

    public AppViewModel() {
        Preferences prefs = Preferences.userRoot().node("org.aind.omezarr.viewer");

        String directory = prefs.get(LAST_DIRECTORY_PREF, "");

        if (!directory.isBlank()) {
            filesetLocation.setValue(directory);
        }

        javaVersion.setValue(SystemInfo.javaVersion());

        javafxVersion.setValue(SystemInfo.javafxVersion());
    }

    public void setFilesetDirectory(File directory) {
        if (directory != null) {
            filesetLocation.setValue(directory.getAbsolutePath());

            Preferences prefs = Preferences.userRoot().node("org.aind.omezarr.viewer");

            prefs.put(LAST_DIRECTORY_PREF, directory.getAbsolutePath());
        }
    }
}
