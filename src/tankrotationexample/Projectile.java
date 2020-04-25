package tankrotationexample;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Projectile extends Moveable {

    private int speed,angle;
    private BufferedImage projectileImage;
    private boolean vis = true;
    private boolean reflect = false;
    private Rectangle intersectRect;

    public Projectile(int x,int y, int height, int width, boolean vis, BufferedImage projectileImage, int speed, int angle, int midXtankproj, int midYtankproj)
    {
        super(x,y,vis,projectileImage);
        this.speed = speed;
        this.angle = angle;
        this.projectileImage = projectileImage;
        //projectile is rectangular, so I'm using height interchangeably with width for point up and pointing down
        if(Math.abs(angle % 180) >= 45 && Math.abs(angle) % 180 <= 135)
            hitBox = new Rectangle(x,y,this.projectileImage.getHeight(),this.projectileImage.getWidth());
        else
            hitBox = new Rectangle(x,y,this.projectileImage.getWidth(),this.projectileImage.getHeight());
    }


    public void checkBorder()
    {
        if (x < 32) {
            x = 32;
            vis = false;
        }
        if (x >= TankRotationExample.WORLD_WIDTH - 32) {
            x = TankRotationExample.WORLD_WIDTH - 32;
            vis = false;
        }
        if (y < 32) {
            y = 32;
            vis = false;
        }
        if (y >= TankRotationExample.WORLD_HEIGHT - 32) {
            y = TankRotationExample.WORLD_HEIGHT - 32;
            vis = false;
        }
    }

    @Override
    public void collideCheck(Object obj) {
        if(visible) {
            if (obj instanceof ArrayList<?> && ((ArrayList<?>) obj).get(1) instanceof Wall) {
                for(Object wall : (ArrayList<?>)obj) {
                    if (((Wall)wall).getVisible() && ((Wall) wall).getHitBox().intersects(hitBox)) {
                        if(reflect && (Wall)wall instanceof HardWall)
                        {
                            intersectRect = ((HardWall) wall).getHitBox().intersection(hitBox);
                            if(Math.abs(angle % 180) >= 45 && Math.abs(angle) % 180 <= 135)
                                hitBox = new Rectangle(x,y,this.projectileImage.getHeight(),this.projectileImage.getWidth());
                            else
                                hitBox = new Rectangle(x,y,this.projectileImage.getWidth(),this.projectileImage.getHeight());
                        }
                        else {
                            this.setVisible(false);
                            if ((Wall) wall instanceof SoftWall) ((Wall) wall).setVisible(false);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void update() {
        if(vis) {
            x += (int) Math.round(speed * Math.cos(Math.toRadians(angle)));
            y += (int) Math.round(speed * Math.sin(Math.toRadians(angle)));
            checkBorder();
            if(Math.abs(angle % 180) >= 45 && Math.abs(angle) % 180 <= 135)
                this.hitBox.setLocation(this.x+10, this.y-5);
            else
                this.hitBox.setLocation(this.x, this.y);
        }
    }

    @Override
    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.projectileImage.getWidth() / 2.0, this.projectileImage.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        if(vis) {
            g2d.drawImage(this.projectileImage, rotation, null);
            g2d.setColor(Color.CYAN);
            //g2d.drawRect((int) hitBox.getX(), (int) hitBox.getY(), (int) this.hitBox.getWidth(), (int) this.hitBox.getHeight());
        }
    }
}
