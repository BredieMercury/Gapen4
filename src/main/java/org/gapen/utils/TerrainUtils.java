package org.gapen.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TerrainUtils {
    private static double DEEP = 0.2;
    private static double SHALLOW = 0.3;
    private static double SAND = 0.4;
    private static double GRASS = 0.6;
    private static double ROCK = 0.85;

    private static Color deepColor = new Color(0, 0, 98);
    private static Color shallowColor = new Color(0, 0, 152);
    private static Color sandColor = new Color(255, 255, 178);
    private static Color grassColor = new Color(34, 139, 34);
    private static Color rockColor = new Color(128, 128, 128);
    private static Color snowColor = new Color(255, 255, 255);

    public static BufferedImage getColoredMap(double[][] heightMap) {
        // assume heightMap is a 2D array of double values representing the height map
        BufferedImage image = new BufferedImage(heightMap.length, heightMap[0].length, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < heightMap.length; x++) {
            for (int y = 0; y < heightMap[0].length; y++) {
                double height = heightMap[x][y];
                Color color = getColorForHeight(height);
                image.setRGB(x, y, color.getRGB());
            }
        }
        return image;
    }

    public static BufferedImage getColoredMapWithShadows(double[][] heightMap) {
        int width = heightMap.length;
        int height = heightMap[0].length;

        BufferedImage colorImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = colorImage.createGraphics();

        // Loop through each pixel in the height map
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double heightValue = heightMap[x][y];

                // Determine the color for the current pixel based on its height value
                Color color = getColorForHeight(heightValue);

                // Set color in color image
                colorImage.setRGB(x, y, color.getRGB());

                if (heightValue <= 0.4) {
                    continue;
                }

                // Determine the brightness of the current pixel based on its height value
                double brightness = 1.0 - heightValue;

                // Set the shadow color and paint a translucent rectangle over the current pixel
                Color shadowColor = new Color(0, 0, 0, (int) ((255 * brightness * brightness) % 256));
                g2d.setColor(shadowColor);
                g2d.fillRect(x, y, 1, 1);
            }
        }

        // Dispose the graphics context
        g2d.dispose();

        return colorImage;
    }

    public static Color getColorForHeight(double height) {
        if (height < DEEP) {
            return deepColor; // dark blue for deep water
        } else if (height < SHALLOW) {
            return shallowColor; // bright blue for shallow water
        } else if (height < SAND) {
            return sandColor; // light yellow for beach
        } else if (height < GRASS) {
            return grassColor; // green for land
        } else if (height < ROCK) {
            return rockColor; // grey for mountains
        } else {
            return snowColor; // white for snow
        }
    }
}
