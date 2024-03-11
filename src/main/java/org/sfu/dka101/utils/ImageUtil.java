package org.sfu.dka101.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageUtil {
    public static boolean isImageGreyscale(BufferedImage image) {
        for (int row=0; row < image.getWidth(); row++) {
            for (int col = 0; col < image.getHeight(); col++) {
                Color rgbValue = new Color(image.getRGB(row, col));
                if ( rgbValue.getRed() != rgbValue.getGreen() || rgbValue.getGreen() != rgbValue.getGreen())
                    return false;
            }
        }
        return true;
    }

    //ArrayList<ArrayList<Integer>>
    public static void getRGBValues(BufferedImage image) {
        for (int row=0; row < image.getWidth(); row++) {
            for (int col = 0; col < image.getHeight(); col++) {

            }
        }
    }

    public static BufferedImage readImage(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {

            byte[] imageHeader = new byte[14];
            fileInputStream.read(imageHeader);
            int imageDataOffset = read4BytesFromOffsetAsInt(imageHeader, 10);


            byte[] imageInfoHeader = new byte[imageDataOffset - 14];
            fileInputStream.read(imageInfoHeader);
            int width = read4BytesFromOffsetAsInt(imageInfoHeader, 4);
            int height = read4BytesFromOffsetAsInt(imageInfoHeader, 8);
            int bitsPerPixel = read2BytesFromOffsetAsInt(imageInfoHeader, 14);

            int colorTableSize = imageDataOffset - 54;
            if (colorTableSize > 0) {
                fileInputStream.skip(colorTableSize);
            }

            int bytesPerRow = (int) Math.ceil((bitsPerPixel * width) / 32.0) * 4;
            byte[] imageData = new byte[bytesPerRow * height];
            fileInputStream.read(imageData);

            BufferedImage bufferedImage = getBufferedImage(width, height, imageData);
            return bufferedImage;
        }
    }

    private static BufferedImage getBufferedImage(int width, int height, byte[] imageData) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int index = 0;
        for (int col = height - 1; col >= 0; col--) {
            for (int row = 0; row < width; row++) {
                int b = imageData[index++] & 0xFF;
                int g = imageData[index++] & 0xFF;
                int r = imageData[index++] & 0xFF;
                int rgb = (r << 16) | (g << 8) | b;
                bufferedImage.setRGB(row, col, rgb);
            }
        }
        return bufferedImage;
    }


    private static int read4BytesFromOffsetAsInt(byte[] bytes, int offset) {
        return (bytes[offset] & 0xFF) |
                ((bytes[offset + 1] & 0xFF) << 8) |
                ((bytes[offset + 2] & 0xFF) << 16) |
                ((bytes[offset + 3] & 0xFF) << 24);
    }

    // Helper method to convert little-endian byte array to short
    private static int read2BytesFromOffsetAsInt(byte[] bytes, int offset) {
        return ((bytes[offset] & 0xFF) | ((bytes[offset + 1] & 0xFF) << 8));
    }
}
