package tankrotationexample;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Background {
    BufferedImage bgImage;
    Background(BufferedImage bgImage)
    {
        this.bgImage = bgImage;
    }
    void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.bgImage,null,0,0);
    }
}
