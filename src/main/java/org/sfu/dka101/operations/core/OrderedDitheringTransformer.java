package org.sfu.dka101.operations.core;

import org.sfu.dka101.operations.Transformer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class OrderedDitheringTransformer implements Transformer {

    private static final int[][] DITHER_MATRIX = {
            { 6, 4, 1, 8 },
            { 7, 0, 5, 2 },
            { 3, 4, 7, 1 },
            { 5, 4, 8, 0 }
    };

    @Override
    public BufferedImage transform(BufferedImage image) {
        ColorModel colorModel = image.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster writableRaster = image.copyData(null);
        BufferedImage convertedImage = new BufferedImage(colorModel, writableRaster, isAlphaPremultiplied, null);

        for (int row=0; row < image.getWidth(); row++) {
            for (int col = 0; col < image.getHeight(); col++) {
                int pixelValue = image.getRGB(row, col);

                int ditherValue = (DITHER_MATRIX[row % DITHER_MATRIX.length][col % DITHER_MATRIX[0].length] * 32)-1;

                int newRed = new Color(pixelValue).getRed() > ditherValue ? 255 : 0;
                int newGreen = new Color(pixelValue).getGreen() > ditherValue ? 255 : 0;
                int newBlue = new Color(pixelValue).getBlue() > ditherValue ? 255 : 0;

                convertedImage.setRGB(row, col, new Color(newRed, newGreen, newBlue).getRGB());
            }
        }
        return convertedImage;
    }
}
