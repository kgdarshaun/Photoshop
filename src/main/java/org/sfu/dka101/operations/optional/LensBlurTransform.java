package org.sfu.dka101.operations.optional;

import org.sfu.dka101.operations.Transformer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class LensBlurTransform implements Transformer {

    private static final int BLUR_RADIUS = 5;

    @Override
    public BufferedImage transform(BufferedImage image) {
        ColorModel colorModel = image.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster writableRaster = image.copyData(null);
        BufferedImage convertedImage = new BufferedImage(colorModel, writableRaster, isAlphaPremultiplied, null);

        double[][] kernel = generateGaussianKernel(BLUR_RADIUS);

        for (int row = 0; row < image.getWidth(); row++) {
            for (int col = 0; col < image.getHeight(); col++) {
                double rValue = 0;
                double gValue = 0;
                double bValue = 0;
                double weightSum = 0;

                for (int j = -BLUR_RADIUS; j <= BLUR_RADIUS; j++) {
                    for (int i = -BLUR_RADIUS; i <= BLUR_RADIUS; i++) {
                        int nx = row + i;
                        int ny = col + j;

                        if (nx >= 0 && nx < image.getWidth() && ny >= 0 && ny < image.getHeight()) {
                            int rgbValue = image.getRGB(nx, ny);
                            double weight = kernel[j + BLUR_RADIUS][i + BLUR_RADIUS];
                            rValue += new Color(rgbValue).getRed() * weight;
                            gValue += new Color(rgbValue).getGreen() * weight;
                            bValue += new Color(rgbValue).getBlue() * weight;
                            weightSum += weight;
                        }
                    }
                }

                if (weightSum != 0) {
                    rValue /= weightSum;
                    gValue /= weightSum;
                    bValue /= weightSum;
                }

                convertedImage.setRGB(row, col, new Color((int) rValue, (int) gValue, (int) bValue).getRGB());
            }
        }

        return convertedImage;
    }

    public static double[][] generateGaussianKernel(int radius) {
        int size = 2 * radius + 1;
        double[][] kernel = new double[size][size];
        double sigma = radius / 5.0;

        double sigmaSquared = sigma * sigma;
        double twoSigmaSquared = 2 * sigmaSquared;
        double sigmaRoot = Math.sqrt(twoSigmaSquared * Math.PI);

        double total = 0;

        for (int row = -radius; row <= radius; row++) {
            for (int col = -radius; col <= radius; col++) {
                double distance = row * row + col * col;
                int i = row + radius;
                int j = col + radius;
                kernel[j][i] = Math.exp(-distance / twoSigmaSquared) / sigmaRoot;
                total += kernel[j][i];
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                kernel[j][i] /= total;
            }
        }

        return kernel;
    }

}
