package tankrotationexample;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author anthony-pc
 */
public class Tank extends Moveable{
    private int vx;
    private int vy;
    private int angle;
    private int R = 3;
    private final int ROTATION_SPEED = 2;
    protected BufferedImage tankImage;
    private BufferedImage projectileImage;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed = false;
    //private boolean alive = true;
    private int projspeed = 5;
    protected ArrayList<Projectile> projectiles = new ArrayList<>();
    protected LivesHealthBar health;
    Date lasttime;
    Date projtime;
    Tank(int x, int y, boolean vis, BufferedImage tankImage, int vx, int vy, int angle, BufferedImage projectileImage, BufferedImage heartImage, BufferedImage healthbarImage) {
        super(x,y,vis,tankImage);
        health = new LivesHealthBar(x,y,true,heartImage,healthbarImage);
        lasttime = new Date();
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.tankImage = tankImage;
        this.hitBox = new Rectangle(x,y,this.tankImage.getWidth(),this.tankImage.getHeight());
        this.angle = angle;
        this.projectileImage = projectileImage;
    }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void toggleShootPressed() {this.ShootPressed = true;}

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void unToggleShootPressed() {this.ShootPressed = false; }


    public void drawImage() {

    }

    public void update() {
        if(health.getVisible())
        {
            if (this.UpPressed) {
                this.moveForwards();
            }
            if (this.DownPressed) {
                this.moveBackwards();
            }

            if (this.LeftPressed) {
                this.rotateLeft();
            }
            if (this.RightPressed) {
                this.rotateRight();
            }
            if (this.ShootPressed) {
                this.shoot();
            }
            if (!projectiles.isEmpty()) {
                for (int i = 0; i < projectiles.size(); i++) {
                    projectiles.get(i).update();
                    //tankCollision(projectiles);
                    projectiles.get(i).checkBorder();
                    if (!projectiles.get(i).getVisible())
                        projectiles.remove(i);
                    /*
                    if(projectiles.get(i).getX() < 0 || projectiles.get(i).getX() > TankRotationExample.SCREEN_WIDTH && projectiles.get(i).getY() < 0 && projectiles.get(i).getY() > TankRotationExample.SCREEN_HEIGHT)
                    {
                        projectiles.get(i).setVisible(false);
                        projectiles.remove(i);
                    }
                     */
                }
            }
            health.update(this);
        }
    }
    public void collideCheck(Object obj)
    {
        if(health.getVisible()) {
            if (obj instanceof ArrayList<?> && (!((ArrayList<?>) obj).isEmpty()) && ((ArrayList<?>) obj).get(0) instanceof Wall) {
                for(Object wall : (ArrayList<?>)obj) {
                    if (((Wall)wall).getVisible() && ((Wall) wall).getHitBox().intersects(hitBox)) {
                        setX(getX() - (isUpPressed() ? 1 : 0) * getVx() + (isDownPressed() ? 1 : 0) * getVx());
                        setY(getY() - (isUpPressed() ? 1 : 0) * getVy() + (isDownPressed() ? 1 : 0) * getVy());
                    }
                }
            }
            else if (obj instanceof Projectile) {
                if (((Projectile) obj).getHitBox().intersects(hitBox)) {
                    ((Projectile) obj).visible = false;
                    health.decHealth();
                }
            }
            else if (obj instanceof ArrayList<?> && (!((ArrayList<?>) obj).isEmpty()) && ((ArrayList<?>) obj).get(0) instanceof PowerUp) {
                //System.out.println("PING");
                for (Object powerup : (ArrayList<?>) obj) {
                    if (((PowerUp)powerup).getVisible() && ((PowerUp) powerup).getHitBox().intersects(hitBox)) {
                        //System.out.println(visible + " " + ((Tank) obj).getVisible());
                        //System.out.println("PONG");
                        R++;
                        projspeed +=2;
                        ((PowerUp) powerup).visible = false;
                    }
                }
            }
            else if (obj instanceof Tank) {
                if (((Tank) obj).getHitBox().intersects(hitBox) && (isDownPressed() || isUpPressed()) && ((Tank) obj).getVisible()) {
                    //System.out.println(visible + " " + ((Tank) obj).getVisible());
                    setX(getX() - (isUpPressed() ? 1 : 0) * getVx() + (isDownPressed() ? 1 : 0) * getVx());
                    setY(getY() - (isUpPressed() ? 1 : 0) * getVy() + (isDownPressed() ? 1 : 0) * getVy());
                }
            }
        }
    }
    private void rotateLeft() {
        this.angle -= this.ROTATION_SPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATION_SPEED;
    }

    private void moveBackwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
        hitBox.setLocation(x,y);
    }

    public int getVx() {
        return vx;
    }

    public int getVy() {
        return vy;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isUpPressed() {
        return UpPressed;
    }

    public boolean isDownPressed() {
        return DownPressed;
    }

    private void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        //checkBorder();
        hitBox.setLocation(x,y);
    }

    private void shoot() {
        projtime = new Date();
        if(projtime == null || projtime.getTime()-1000>lasttime.getTime()) {
            lasttime = projtime;
            projectiles.add(new Projectile(x, y, 5,5,visible, projectileImage, projspeed, angle, 25, 25));
        }
    }

    private void checkBorder() {
        if (x <= 32) {
            x = 32;
        }
        if (x >= TankRotationExample.WORLD_WIDTH - 32) {
            x = TankRotationExample.WORLD_WIDTH - 32;
        }
        if (y < 32) {
            y = 32;
        }
        if (y >= TankRotationExample.WORLD_HEIGHT - 32) {
            y = TankRotationExample.WORLD_HEIGHT - 32;
        }
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

    public boolean getShooting()
    {
        return ShootPressed;
    }
    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.tankImage.getWidth() / 2.0, this.tankImage.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        if(health.getVisible()) {
            g2d.drawImage(this.tankImage, rotation, null);
            g2d.setColor(Color.CYAN);
            //g2d.drawRect(x,y,this.tankImage.getWidth(),this.tankImage.getHeight());
            this.health.drawImage(g2d);

        }
        if(!projectiles.isEmpty()) {
            this.projectiles.forEach(bullet ->bullet.drawImage(g2d));
        }
    }
    /*
    public void tankCollision(ArrayList<Projectile> projs)
    {
        for(Projectile i:projs)
        {
            if(i.getX() > x && i.getX() < x + 50 && i.getY() > y && i.getY() < y + 50)
                vis = false;
        }
    }
    */
}
