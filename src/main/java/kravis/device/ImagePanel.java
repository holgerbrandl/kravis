package kravis.device;


import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Holger Brandl
 */
class ImagePanel extends JLabel {
    private Image image;


    ImagePanel() {
        super("text");
    }


    public void setIcon(Icon icon) {
        super.setIcon(icon);
        if (icon instanceof ImageIcon) {
            image = ((ImageIcon) icon).getImage();
        }
    }


    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);

    }


    public void setImage(@Nullable BufferedImage img) {
        image = img;
        invalidate();
        repaint();
    }
}