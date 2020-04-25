package tankrotationexample;
import java.awt.image.BufferedImage;
import java.awt.*;

public abstract class GameObject{
    protected int x;
    protected int y;
    protected boolean visible = true;
    protected BufferedImage image;

    public GameObject(int x, int y, boolean visible, BufferedImage image)
    {
        this.x = x;
        this.y = y;
        this.visible = visible;
        this.image = image;
    }

    protected int getX() {
        return x;
    }
    protected int getY() {
        return y;
    }
    protected void setVisible(boolean vis) {
        visible = vis;
    }
    protected boolean getVisible() {
        return visible;
    }
    public abstract void drawImage(Graphics g);
    public abstract void update();
}
