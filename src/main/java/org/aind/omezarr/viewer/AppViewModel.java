package org.aind.omezarr.viewer;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.util.prefs.Preferences;

public class AppViewModel {
    private final StringProperty javaVersion = new SimpleStringProperty("");

    private final StringProperty javafxVersion = new SimpleStringProperty("");

    private final ObjectProperty<OmeZarrViewModel> omeZarrViewModel = new SimpleObjectProperty<>(new OmeZarrViewModel());

    private final static String LAST_DIRECTORY_PREF = "LAST_DIRECTORY_PREF";

    public StringProperty getJavaVersion() {
        return javaVersion;
    }

    public StringProperty getJavafxVersion() {
        return javafxVersion;
    }

    public ObjectProperty<OmeZarrViewModel> getOmeZarrViewModel() {
        return omeZarrViewModel;
    }

    public AppViewModel() {
    }

    public void initialize() {
        javaVersion.setValue(SystemInfo.javaVersion());

        javafxVersion.setValue(SystemInfo.javafxVersion());

        Preferences prefs = Preferences.userRoot().node("org.aind.omezarr.viewer");

        String directory = prefs.get(LAST_DIRECTORY_PREF, "");

        if (!directory.isBlank()) {
            tryLoadOmeZarr(new File(directory));
        }
    }

    public void tryLoadOmeZarr(File directory) {
        if (directory != null) {
            omeZarrViewModel.get().tryLoadOmeZarr(directory);

            Preferences prefs = Preferences.userRoot().node("org.aind.omezarr.viewer");

            prefs.put(LAST_DIRECTORY_PREF, directory.getAbsolutePath());
        }
    }
}
