package org.gapen.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ImageUtils {
    public static BufferedImage combineImages(List<BufferedImage> images) {
        int numRows = (int) Math.ceil((double) images.size() / 3);
        int numCols = Math.min(images.size(), 3);
        int imgWidth = images.get(0).getWidth();
        int imgHeight = images.get(0).getHeight();
        int resultWidth = numCols * imgWidth;
        int resultHeight = numRows * imgHeight;

        BufferedImage result = new BufferedImage(resultWidth, resultHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = result.createGraphics();

        int x = 0;
        int y = 0;
        for (int i = 0; i < images.size(); i++) {
            BufferedImage img = images.get(i);
            g2d.drawImage(img, x, y, null);
            x += imgWidth;

            if ((i + 1) % 3 == 0) {
                x = 0;
                y += imgHeight;
            }
        }

        g2d.dispose();
        return result;
    }

    public static double[][] anisotropicFilter(double[][] heightMap) {
        int width = heightMap.length;
        int height = heightMap[0].length;
        double[][] result = new double[width][height];

        double[][] gradientX = new double[width][height];
        double[][] gradientY = new double[width][height];

        // Calculate gradients for each point in the height map
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                gradientX[x][y] = heightMap[x + 1][y] - heightMap[x - 1][y];
                gradientY[x][y] = heightMap[x][y + 1] - heightMap[x][y - 1];
            }
        }

        // Apply anisotropic filtering to smooth the height map
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                double gx = gradientX[x][y];
                double gy = gradientY[x][y];

                // Calculate weights for surrounding points based on gradient magnitude
                double w1 = Math.exp(-((gx * gx) + (gy * gy)) / 16.0);
                double w2 = Math.exp(-((gx * gx) + (gy * gy)) / 4.0);
                double w3 = Math.exp(-((gx * gx) + (gy * gy)) / 1.0);

                // Apply weighted average of surrounding points to current point
                double sum = (w1 * heightMap[x - 1][y - 1]) + (w2 * heightMap[x][y - 1]) + (w1 * heightMap[x + 1][y - 1])
                        + (w2 * heightMap[x - 1][y]) + (w3 * heightMap[x][y]) + (w2 * heightMap[x + 1][y])
                        + (w1 * heightMap[x - 1][y + 1]) + (w2 * heightMap[x][y + 1]) + (w1 * heightMap[x + 1][y + 1]);

                result[x][y] = sum / (w1 + (2.0 * w2) + w3);
            }
        }

        return result;
    }

    public static void showImage(BufferedImage img, String title) {
        JFrame frame = new JFrame(title);
        JLabel label = new JLabel(new ImageIcon(img));
        frame.getContentPane().add(label, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String fileName = "maps/" + System.currentTimeMillis() + ".png";
                    File output = new File(fileName);
                    ImageIO.write(img, "png", output);
                    System.out.println("Saved image to " + fileName);
                } catch (IOException ex) {
                    System.err.println("Error saving image: " + ex.getMessage());
                }
            }
        });
        frame.getContentPane().add(saveButton, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static BufferedImage toImage(double[][] heightMap) {
        int width = heightMap.length;
        int height = heightMap[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                min = Math.min(min, heightMap[i][j]);
                max = Math.max(max, heightMap[i][j]);
            }
        }
        double diff = max - min;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int gray = (int) (255 * (heightMap[i][j] - min) / diff);
                Color color = new Color(gray, gray, gray);
                image.setRGB(i, j, color.getRGB());
            }
        }
        return image;
    }

    public static BufferedImage addTextToImage(BufferedImage image, String text) {
        // Create a new BufferedImage with the same dimensions as the original
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // Get the graphics object for the new image
        Graphics2D g2d = newImage.createGraphics();

        // Draw the original image onto the new image
        g2d.drawImage(image, 0, 0, null);

        // Set the font and color for the text
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.setColor(Color.RED);

        // Draw the text at the top left corner of the image
        g2d.drawString(text, 10, 30);

        // Dispose of the graphics object
        g2d.dispose();

        // Return the modified image
        return newImage;
    }

}

