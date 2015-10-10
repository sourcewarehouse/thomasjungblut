package de.jungblut.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;

import de.jungblut.math.DoubleVector;

public class ImageReaderTest {

  private static final String LENNA_PATH = "files/img/lenna.png";

  @Test
  public void testGreyScaleReader() throws Exception {

    DoubleVector vect = ImageReader.readImageAsGreyScale(ImageIO.read(new File(
        LENNA_PATH)));
    assertEquals(512 * 512, vect.getDimension());
    for (int i = 0; i < vect.getLength(); i++) {
      assertTrue(vect.get(i) >= 0);
      assertTrue(vect.get(i) <= 255);
    }

  }

  @Test
  public void testSlidingWindow() throws Exception {

    BufferedImage source = ImageIO.read(new File(LENNA_PATH));
    List<BufferedImage> subImages = ImageReader.getSlidingWindowPatches(source,
        32, 32, 32, 32);
    assertEquals(225, subImages.size());
    for (BufferedImage img : subImages) {
      assertEquals(32, img.getHeight());
      assertEquals(32, img.getWidth());
    }
  }

  @Test
  public void testLUVReader() throws Exception {

    DoubleVector[] luvVectors = ImageReader.readImageAsLUV(ImageIO
        .read(new File(LENNA_PATH)));
    assertEquals(512 * 512, luvVectors.length);
    for (int i = 0; i < luvVectors.length; i++) {
      assertEquals(3, luvVectors[i].getLength());
    }

  }

  @Test
  public void testRGBReader() throws Exception {

    DoubleVector[] rgbVectors = ImageReader.readImageAsRGB(ImageIO
        .read(new File(LENNA_PATH)));
    assertEquals(512 * 512, rgbVectors.length);
    for (int i = 0; i < rgbVectors.length; i++) {
      assertEquals(3, rgbVectors[i].getLength());
      assertTrue(rgbVectors[i].get(0) >= 0 && rgbVectors[i].get(0) <= 255);
      assertTrue(rgbVectors[i].get(1) >= 0 && rgbVectors[i].get(1) <= 255);
      assertTrue(rgbVectors[i].get(2) >= 0 && rgbVectors[i].get(2) <= 255);
    }

  }

}
