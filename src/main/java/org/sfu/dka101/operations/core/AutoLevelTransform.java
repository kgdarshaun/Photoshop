package org.sfu.dka101.operations.core;

import org.sfu.dka101.operations.Transformer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class AutoLevelTransform implements Transformer {
    @Override
    public BufferedImage transform(BufferedImage image) {
        ColorModel colorModel = image.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster writableRaster = image.copyData(null);
        BufferedImage convertedImage = new BufferedImage(colorModel, writableRaster, isAlphaPremultiplied, null);

        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixelValues = new int[width * height];

        image.getRGB(0, 0, width, height, pixelValues, 0, width);

        int darkestRed = 255, darkestGreen = 255, darkestBlue = 255;
        int lightestRed = 0, lightestGreen = 0, lightestBlue = 0;
        for (int i = 0; i < pixelValues.length; i++) {
            int redValue = new Color(pixelValues[i]).getRed();
            int greenValue = new Color(pixelValues[i]).getGreen();
            int blueValue = new Color(pixelValues[i]).getBlue();

            if (redValue < darkestRed) darkestRed = redValue;
            if (redValue > lightestRed) lightestRed = redValue;
            if (greenValue < darkestGreen) darkestGreen = greenValue;
            if (greenValue > lightestGreen) lightestGreen = greenValue;
            if (blueValue < darkestBlue) darkestBlue = blueValue;
            if (blueValue > lightestBlue) lightestBlue = blueValue;
        }

        for (int i = 0; i < pixelValues.length; i++) {
            int redValue = new Color(pixelValues[i]).getRed();
            int greenValue = new Color(pixelValues[i]).getGreen();
            int blueValue = new Color(pixelValues[i]).getBlue();

            int newRedValue = (int) (255.0 / (lightestRed - darkestRed) * (redValue - darkestRed));
            int newGreenValue = (int) (255.0 / (lightestGreen - darkestGreen) * (greenValue - darkestGreen));
            int newBlueValue = (int) (255.0 / (lightestBlue - darkestBlue) * (blueValue - darkestBlue));

            convertedImage.setRGB(i % width, i / width, new Color(newRedValue, newGreenValue, newBlueValue).getRGB());
        }

        return convertedImage;
    }
}
