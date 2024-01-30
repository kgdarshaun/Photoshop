package org.sfu.dka101.operations;

import org.sfu.dka101.enums.Operations;
import org.sfu.dka101.operations.core.GrayscaleTransformer;

public class TransformSelector {
    public static Transformer selectTransform(Operations operation) {
        Transformer transformer = null;
        switch (operation){
            case GRAYSCALE -> transformer = new GrayscaleTransformer();
        }
        return transformer;
    }
}
