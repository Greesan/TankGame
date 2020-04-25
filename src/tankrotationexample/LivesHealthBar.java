package tankrotationexample;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class LivesHealthBar extends Moveable{
    protected BufferedImage healthbarImage;
    protected int lives;
    protected int health;
    public LivesHealthBar(int x, int y, boolean visible, BufferedImage heartImage, BufferedImage healthbarImage) {
        super(x, y, visible, heartImage);
        this.healthbarImage = healthbarImage;
        lives = 3;
        health = 3;
    }

    protected int getLives() {
        return lives;
    }

    protected int getHealth() {
        return health;
    }

    public void decHealth() {
        if(lives > 1 && health == 1) {
            this.lives--;
            this.health = 3;
        }
        else if(lives == 1 && health == 1)
            visible = false;
        else health--;
    }

    @Override
    public void collideCheck(Object obj) {
        //no need to check for collisions
    }

    @Override
    public void update() {
        //no need for default update, must update based on tank values
    }

    public void update(Tank tank){
        this.x = tank.getX();
        this.y = tank.getY()+tank.tankImage.getHeight();
    }
    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if(visible) {
            for (int i = 0;i < lives;i++)
                g2d.drawImage(image, null, this.x + i*healthbarImage.getWidth()/2, this.y);
            for(int i = 0; i < health;i++)
                g2d.drawImage(healthbarImage, null, this.x + (i-1)*healthbarImage.getWidth()+15, this.y + this.image.getHeight()-3);
            //g2d.setColor(Color.CYAN);
            //g2d.drawRect(x,y,this.image.getWidth(),this.image.getHeight());
        }
    }
}
