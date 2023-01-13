package org.aind.omezarr.viewer;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ImageSliceViewModel {
    public final IntegerProperty multiScaleIndexProperty = new SimpleIntegerProperty(0);

    public final IntegerProperty datasetIndexProperty = new SimpleIntegerProperty(0);

    public final IntegerProperty timeIndexProperty = new SimpleIntegerProperty(0);

    public final IntegerProperty channelIndexProperty = new SimpleIntegerProperty(0);

    public final IntegerProperty zIndexProperty = new SimpleIntegerProperty(0);
}
