package org.sfu.dka101.operations.optional;

import org.sfu.dka101.operations.Transformer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class VignetteTransform implements Transformer {
    @Override
    public BufferedImage transform(BufferedImage image) {
        ColorModel colorModel = image.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster writableRaster = image.copyData(null);
        BufferedImage convertedImage = new BufferedImage(colorModel, writableRaster, isAlphaPremultiplied, null);

        int radius = (int) (Math.min(image.getWidth(), image.getHeight()) / 1.25);
        float darknessFactor = 0.99f;
        float centerX = image.getWidth() / 2f;
        float centerY = image.getHeight() / 2f;

        for (int row = 0; row < image.getWidth(); row++) {
            for (int col = 0; col < image.getHeight(); col++) {
                float dx = row - centerX;
                float dy = col - centerY;
                double distance = Math.sqrt(dx * dx + dy * dy);
                distance = Math.min(distance, radius);

                float darkness = (float) (1.0 - (distance / radius));
                darkness = Math.max(darkness, 0);
                darkness *= darknessFactor;

                Color pixelColor = new Color(image.getRGB(row, col));
                int redValue = (int) (pixelColor.getRed() * darkness);
                int greenValue = (int) (pixelColor.getGreen() * darkness);
                int blueValue = (int) (pixelColor.getBlue() * darkness);
                Color darkenedColor = new Color(redValue, greenValue, blueValue);
                convertedImage.setRGB(row, col, darkenedColor.getRGB());
            }
        }

        return convertedImage;
    }
}
