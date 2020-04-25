package tankrotationexample;

import java.awt.image.BufferedImage;

public abstract class Stationary extends GameObject{
    public Stationary(int x, int y, boolean vis, BufferedImage image) {
        super(x, y, vis,image);
    }

    public abstract void update();
}
