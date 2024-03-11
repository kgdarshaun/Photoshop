package org.sfu.dka101.operations.core;

import org.sfu.dka101.operations.Transformer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class GrayscaleTransformer implements Transformer {
    public BufferedImage transform(BufferedImage image) {
        ColorModel colorModel = image.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster writableRaster = image.copyData(null);
        BufferedImage convertedImage = new BufferedImage(colorModel, writableRaster, isAlphaPremultiplied, null);

        for (int row=0; row < image.getWidth(); row++) {
            for (int col = 0; col < image.getHeight(); col++) {
                int rgbValue = image.getRGB(row, col);
                float rValue = new Color(rgbValue).getRed();
                float gValue = new Color(rgbValue).getGreen();
                float bValue = new Color(rgbValue).getBlue();

                int grayscale = (int) ((0.299 * rValue) + (0.587 * gValue) + (0.114 * bValue));
                convertedImage.setRGB(row, col, new Color(grayscale, grayscale, grayscale).getRGB());
            }
        }
        return convertedImage;
    }
}
