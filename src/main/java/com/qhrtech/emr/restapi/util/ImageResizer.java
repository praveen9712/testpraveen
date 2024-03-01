
package com.qhrtech.emr.restapi.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImageResizer {

  /**
   * Accuro function to resize the image. check ProfilePicturePanel.java in accuro
   */
  public static BufferedImage resizeImage(int frameWidth, int frameHeight,
      BufferedImage originalImage, boolean preserveAlpha) {
    int xoriginal = originalImage.getWidth();
    int yoriginal = originalImage.getHeight();

    if (!isResizeNeeded(frameWidth, frameHeight, xoriginal, yoriginal)) {
      return originalImage;
    }

    // Maintain Aspect calcs
    Double x = ((Integer) frameWidth).doubleValue();
    Double y = ((Integer) frameHeight).doubleValue();

    if (xoriginal > yoriginal) {
      y = (x / xoriginal) * yoriginal;
    } else {
      x = (y / yoriginal) * xoriginal;
    }

    int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_3BYTE_BGR;
    BufferedImage scaledBI = new BufferedImage(x.intValue(), y.intValue(), imageType);
    Graphics2D g = scaledBI.createGraphics();

    g.setBackground(Color.WHITE);
    g.clearRect(0, 0, scaledBI.getWidth(), scaledBI.getHeight());

    if (preserveAlpha) {
      g.setComposite(AlphaComposite.Src);
    }
    g.drawImage(originalImage, 0, 0, x.intValue(), y.intValue(), null);
    g.dispose();

    return scaledBI;
  }

  private static boolean isResizeNeeded(int frameWidth, int frameHeight, int width, int height) {
    return (frameHeight != height && frameWidth != width);
  }

}
