package tankrotationexample;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

public class SoftWall extends Wall {
    int state = 2;
    public SoftWall(int x, int y, boolean vis, BufferedImage image)
    {
        super(x,y,vis,image);
    }

    @Override
    public void update() {
    }

}
