package org.aind.omezarr.viewer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AppViewModel {
    private final StringProperty javaVersion = new SimpleStringProperty(System.getProperty("java.version"));

    private final StringProperty javafxVersion = new SimpleStringProperty(System.getProperty("javafx.version"));

    private final ViewerViewModel viewerViewModel = new ViewerViewModel();

    public StringProperty getJavaVersion() {
        return javaVersion;
    }

    public StringProperty getJavafxVersion() {
        return javafxVersion;
    }

    public ViewerViewModel getViewerViewModel() {
        return viewerViewModel;
    }

    public AppViewModel() {
    }

    public void initialize() {
        viewerViewModel.initialize();
    }
}
