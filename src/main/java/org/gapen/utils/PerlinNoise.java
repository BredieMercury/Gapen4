package org.gapen.utils;

import java.util.Random;

public class PerlinNoise {
    private final int[] p;

    public PerlinNoise(long seed) {
        p = new int[512];

        Random random = new Random(seed);

        for (int i = 0; i < 256; i++) {
            p[i] = i;
        }

        for (int i = 0; i < 256; i++) {
            int j = random.nextInt(256);
            int temp = p[i];
            p[i] = p[j];
            p[j] = temp;
        }

        for (int i = 0; i < 256; i++) {
            p[i + 256] = p[i];
        }
    }

    public static double[][] generateHeightMap(int width, int height, double frequency, double amplitude, int octaves, double lacunarity, double persistence) {
        Random rand = new Random();
        int seed = rand.nextInt(1337);
        PerlinNoise perlin = new PerlinNoise(seed);
        double[][] heightMap = new double[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = 0;
                double currentAmplitude = amplitude;
                double currentFrequency = frequency;
                for (int o = 0; o < octaves; o++) {
                    value += perlin.noise(x * currentFrequency, y * currentFrequency) * currentAmplitude;
                    currentAmplitude *= persistence;
                    currentFrequency *= lacunarity;
                }
                heightMap[x][y] = value;
            }
        }
        return heightMap;
    }

    public double noise(double x, double y) {
        int X = (int) Math.floor(x) & 255; // calculate X
        int Y = (int) Math.floor(y) & 255; // calculate Y

        // get corner values
        int A = p[X % 256] + Y;
        int B = p[(X + 1) % 256] + Y;
        int AA = p[A % 256];
        int AB = p[(A + 1) % 256];
        int BA = p[B % 256];
        int BB = p[(B + 1) % 256];

        // calculate fractional part of x and y
        double dx = x - Math.floor(x);
        double dy = y - Math.floor(y);

        // interpolate along x-axis
        double wx = smooth(dx);
        double xa = lerp(grad(AA, dx, dy), grad(BA, dx - 1, dy), wx);
        double xb = lerp(grad(AB, dx, dy - 1), grad(BB, dx - 1, dy - 1), wx);

        // interpolate along y-axis
        double wy = smooth(dy);
        return lerp(xa, xb, wy);
    }

    private double smooth(double t) {
        return (1 - Math.cos(t * Math.PI)) / 2;
    }

    private double lerp(double a, double b, double t) {
        return a * (1 - t) + b * t;
    }

    private double grad(int hash, double x, double y) {
        int h = hash & 7;
        double u = h < 4 ? x : y;
        double v = h < 4 ? y : x;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }
}








