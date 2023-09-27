package org.aind.omezarr.viewer;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.File;
import java.util.prefs.Preferences;

public class ViewerViewModel {
    private final ObjectProperty<OmeZarrViewModel> omeZarrViewModel = new SimpleObjectProperty<>(new OmeZarrViewModel());

    public ObjectProperty<OmeZarrViewModel> getOmeZarrViewModel() {
        return omeZarrViewModel;
    }
}
