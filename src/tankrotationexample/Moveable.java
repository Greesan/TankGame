package tankrotationexample;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Moveable extends GameObject {
    protected Rectangle hitBox;
    public Moveable(int x, int y,boolean visible, BufferedImage projectileImage) {
        super(x,y,visible,projectileImage);
    }
    public Rectangle getHitBox()
    {
        return hitBox.getBounds();
    }
    public abstract void collideCheck(Object obj);
    public abstract void update();
    public abstract void drawImage(Graphics g);
}
