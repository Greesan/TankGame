package tankrotationexample;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Wall extends Stationary{
    protected Rectangle hitBox;
    public int state = 2;
    public Wall(int x, int y,  boolean vis, BufferedImage image)
    {
        super(x,y,vis,image);
        hitBox = new Rectangle(x,y,image.getWidth(),image.getHeight());
        this.hitBox.setLocation(x, y);
    }

    public Rectangle getHitBox()
    {
        return hitBox.getBounds();
    }

    public int getState() {
        return state;
    }
    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if(visible) {
            g2d.drawImage(image, null, this.x, this.y);
            g2d.setColor(Color.CYAN);
            //g2d.drawRect(x,y,this.image.getWidth(),this.image.getHeight());
        }
    }
}
