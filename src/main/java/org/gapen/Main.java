package org.gapen;

import org.gapen.utils.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static org.gapen.utils.ImageUtils.*;
import static org.gapen.utils.PerlinNoise.generateHeightMap;
import static org.gapen.utils.TerrainUtils.*;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        int width = 512;
        int height = 512;
        double frequency = 0.01;
        double amplitude = 1;
        int octaves = 8;
        double lacunarity = 2;
        double persistence = 0.5;

        double[][] heightMap = generateHeightMap(width, height, frequency, amplitude, octaves, lacunarity, persistence);

        BufferedImage imagePerlin = toImage(heightMap);
        BufferedImage imageColored = getColoredMap(heightMap);
        BufferedImage imageColoredShadow = getColoredMapWithShadows(heightMap);

        BufferedImage imagePerlinAni = toImage(anisotropicFilter(heightMap));
        BufferedImage imageColoredAni = getColoredMap(anisotropicFilter(heightMap));
        BufferedImage imageColoredShadowAni = getColoredMapWithShadows(anisotropicFilter(heightMap));


        List<BufferedImage> images = new ArrayList<>();
        // normal
        images.add(addTextToImage(imagePerlin, "PERLIN NOISE"));
        images.add(addTextToImage(imageColored, "COLORED"));
        images.add(addTextToImage(imageColoredShadow, "COLORED + SHADOWS"));
        // anisotropic filter
        images.add(addTextToImage(imagePerlinAni, "PERLIN NOISE + 1xAF"));
        images.add(addTextToImage(imageColoredAni, "COLORED + 1xAF"));
        images.add(addTextToImage(imageColoredShadowAni, "COLORED + SHADOWS + 1xAF"));
        // Create a new BufferedImage to hold the combined images
        BufferedImage combinedImage = combineImages(images);

        // Display images
        ImageUtils.showImage(combinedImage, "Gapen4");
    }
}