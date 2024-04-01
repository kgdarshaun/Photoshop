package org.sfu.dka101.operations;

import org.sfu.dka101.enums.Operations;
import org.sfu.dka101.operations.core.AutoLevelTransform;
import org.sfu.dka101.operations.core.GrayscaleTransformer;
import org.sfu.dka101.operations.core.OrderedDitheringTransformer;
import org.sfu.dka101.operations.optional.LensBlurTransform;
import org.sfu.dka101.operations.optional.VignetteTransform;

public class TransformSelector {
    public static Transformer selectTransform(Operations operation) {
        Transformer transformer = null;
        switch (operation){
            case GRAYSCALE -> transformer = new GrayscaleTransformer();
            case ORDERED_DITHERING -> transformer = new OrderedDitheringTransformer();
            case AUTO_LEVEL -> transformer = new AutoLevelTransform();
            case LENS_BLUR -> transformer = new LensBlurTransform();
            case VIGNETTE -> transformer = new VignetteTransform();
        }
        return transformer;
    }
}
