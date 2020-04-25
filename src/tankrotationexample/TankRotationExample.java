/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankrotationexample;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import java.net.URL;
import static javax.imageio.ImageIO.read;

/**
 * Main driver class of Tank Example.
 * Class is responsible for loading resources and
 * initializing game objects. Once completed, control will
 * be given to infinite loop which will act as our game loop.
 * A very simple game loop.
 * @author anthony-pc
 */
public class TankRotationExample extends JPanel  {

    public static final int WORLD_WIDTH = 2144;
    public static final int WORLD_HEIGHT = 2144;
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 960;
    int xleft,yleft,xright,yright;
    private BufferedImage world;
    private Graphics2D buffer;
    private JFrame jFrame;
    private Tank tankOne;
    private Tank tankTwo;
    private ArrayList<Wall> thewall;
    private ArrayList<PowerUp> powers;
    private Background bg;
    private static Clip clip;
    AudioInputStream audioInputStream;


    public static void main(String[] args) {
        TankRotationExample tankExample = new TankRotationExample();
        tankExample.init();

        //clip.loop(1);
        try {

            while (true) {
                tankExample.tankOne.update();
                tankExample.tankTwo.update();
                tankExample.repaint();
                //System.out.print(tankExample.tankOne.getX());
                //System.out.print(tankExample.tankOne.getY());
                tankExample.tankOne.collideCheck(tankExample.tankTwo);
                tankExample.tankTwo.collideCheck(tankExample.tankOne);
                if(tankExample.thewall!=null)
                    tankExample.tankOne.collideCheck(tankExample.thewall);
                if(tankExample.thewall!=null)
                    tankExample.tankTwo.collideCheck(tankExample.thewall);
                if(tankExample.powers!=null)
                    tankExample.tankOne.collideCheck(tankExample.powers);
                if(tankExample.powers!=null)
                    tankExample.tankTwo.collideCheck(tankExample.powers);
                tankExample.tankOne.projectiles.forEach(bullet -> tankExample.tankTwo.collideCheck(bullet));
                tankExample.tankTwo.projectiles.forEach(bullet -> tankExample.tankOne.collideCheck(bullet));
                tankExample.tankOne.projectiles.forEach(bullet -> bullet.collideCheck(tankExample.thewall));
                tankExample.tankTwo.projectiles.forEach(bullet -> bullet.collideCheck(tankExample.thewall));
                //  System.out.println(tankExample.t1);
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }

    }


    private void init() {
        this.jFrame = new JFrame("Tank Rotation");
        this.world = new BufferedImage(TankRotationExample.WORLD_WIDTH, TankRotationExample.WORLD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        BufferedImage tank1Image = null;
        BufferedImage tank2Image = null;
        BufferedImage backgroundImage = null;
        BufferedImage projectileImage = null;
        BufferedImage softWallImage = null;
        BufferedImage hardWallImage = null;
        BufferedImage speedImage = null;
        BufferedImage heartImage = null;
        BufferedImage healthBarImage = null;
        thewall = new ArrayList<>();
        powers = new ArrayList<>();
        URL url = this.getClass().getClassLoader().getResource("Music.mid");
        try {/*
             * There is a subtle difference between using class
             * loaders and File objects to read in resources for your
             * tank games. First, File objects when given to input readers
             * will use your project's working directory as the base path for
             * finding the file. Class loaders will use the class path of the project
             * as the base path for finding files. For Intellij, this will be in the out
             * folder. if you expand the out folder, the expand the production folder, you
             * will find a folder that has the same name as your project. This is the folder
             * where your class path points to by default. Resources, will need to be stored
             * in here for class loaders to load resources correctly.
             *
             * Also one important thing to keep in mind, Java input readers given File objects
             * cannot be used to access file in jar files. So when building your jar, if you want
             * all files to be stored inside the jar, you'll need to class loaders and not File
             * objects.
             *
             */
            //Using class loaders to read in resources
            tank1Image = read(TankRotationExample.class.getClassLoader().getResource("tank1.png"));
            tank2Image = read(TankRotationExample.class.getClassLoader().getResource("tank2.png"));
            backgroundImage = read(TankRotationExample.class.getClassLoader().getResource("Background.jpg"));
            projectileImage = read(TankRotationExample.class.getClassLoader().getResource("missle.png"));
            softWallImage = read(TankRotationExample.class.getClassLoader().getResource("Wall2.gif"));
            hardWallImage = read(TankRotationExample.class.getClassLoader().getResource("Wall1.gif"));
            speedImage = read(TankRotationExample.class.getClassLoader().getResource("speed.png"));
            heartImage = read(TankRotationExample.class.getClassLoader().getResource("heart.png"));
            healthBarImage = read(TankRotationExample.class.getClassLoader().getResource("healthbar.png"));
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            //Using file objects to read in resources.
            //tankImage = read(new File("tank1.png"));
            InputStreamReader isr = new InputStreamReader(TankRotationExample.class.getClassLoader().getResourceAsStream("maps/map1"));
            BufferedReader mapReader = new BufferedReader(isr);
            String row = mapReader.readLine();
            if(row == null){
                throw new IOException("No data in file");
            }
            String[] mapInfo = row.split("\t");
            int numCols = Integer.parseInt(mapInfo[0]);
            int numRows = Integer.parseInt(mapInfo[1]);
            for(int i = 0;i < numRows; i++)
            {
                row = mapReader.readLine();
                mapInfo = row.split("\t");
                for(int j = 0;j < numCols; j++)
                {
                    //System.out.println(i+j);
                    switch(mapInfo[j]){
                        case "2":
                            thewall.add(new SoftWall(j*softWallImage.getHeight(),i*softWallImage.getWidth(),true,softWallImage));
                            //System.out.println(softWallImage.getHeight());
                            //System.out.println(softWallImage.getWidth());
                            break;
                        case "5":
                            powers.add(new PowerUp(j*speedImage.getHeight(),i*speedImage.getWidth(),true,speedImage));
                            break;
                        case "3":
                        case "9":
                            thewall.add(new HardWall(j*hardWallImage.getHeight(),i*hardWallImage.getWidth(),true,hardWallImage));
                            //System.out.println(softWallImage.getHeight());
                            //System.out.println(softWallImage.getWidth());
                            break;
                    }
                }
            }
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
            System.out.println(ex.getMessage());
        }
        tankOne = new Tank(100, 200, true, tank1Image,5,5,0, projectileImage, heartImage, healthBarImage);
        tankTwo = new Tank(WORLD_WIDTH-100, WORLD_HEIGHT-200, true,tank2Image,5,5, 180, projectileImage, heartImage, healthBarImage);
        bg = new Background(backgroundImage);
        TankControl tankOneControl = new TankControl(tankOne, KeyEvent.VK_UP,
                KeyEvent.VK_DOWN,
                KeyEvent.VK_LEFT,
                KeyEvent.VK_RIGHT,
                KeyEvent.VK_ENTER);

        TankControl tankTwoControl = new TankControl(tankTwo, KeyEvent.VK_W,
                KeyEvent.VK_S,
                KeyEvent.VK_A,
                KeyEvent.VK_D,
                KeyEvent.VK_SPACE);
        this.jFrame.setLayout(new BorderLayout());
        this.jFrame.add(this);
        this.jFrame.addKeyListener(tankOneControl);
        this.jFrame.addKeyListener(tankTwoControl);
        this.jFrame.setSize(TankRotationExample.SCREEN_WIDTH, TankRotationExample.SCREEN_HEIGHT + 30);
        this.jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jFrame.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        buffer = world.createGraphics();
        this.bg.drawImage(buffer);
        //tankOne.tankCollision(tankTwo.projectiles);
        //tankTwo.tankCollision(tankOne.projectiles);
        this.tankOne.drawImage(buffer);
        this.tankTwo.drawImage(buffer);
        this.thewall.forEach(wall -> wall.drawImage(buffer));
        this.powers.forEach(power -> power.drawImage(buffer));
        xleft = (tankOne.getX()<TankRotationExample.SCREEN_WIDTH/4?0:(tankOne.getX()>TankRotationExample.WORLD_WIDTH - TankRotationExample.SCREEN_WIDTH/4 ? TankRotationExample.WORLD_WIDTH-TankRotationExample.SCREEN_WIDTH/2:tankOne.getX()-TankRotationExample.SCREEN_WIDTH/4));
        yleft = (tankOne.getY()<TankRotationExample.SCREEN_HEIGHT/2?0:(tankOne.getY()>TankRotationExample.WORLD_HEIGHT - TankRotationExample.SCREEN_HEIGHT/2 ? TankRotationExample.WORLD_HEIGHT-TankRotationExample.SCREEN_HEIGHT:tankOne.getY()-TankRotationExample.SCREEN_HEIGHT/2));
        xright = (tankTwo.getX()<TankRotationExample.SCREEN_WIDTH/4?0:(tankTwo.getX()>TankRotationExample.WORLD_WIDTH - TankRotationExample.SCREEN_WIDTH/4 ? TankRotationExample.WORLD_WIDTH-TankRotationExample.SCREEN_WIDTH/2:tankTwo.getX()-TankRotationExample.SCREEN_WIDTH/4));
        yright = (tankTwo.getY()<TankRotationExample.SCREEN_HEIGHT/2?0:(tankTwo.getY()>TankRotationExample.WORLD_HEIGHT - TankRotationExample.SCREEN_HEIGHT/2 ? TankRotationExample.WORLD_HEIGHT-TankRotationExample.SCREEN_HEIGHT:tankTwo.getY()-TankRotationExample.SCREEN_HEIGHT/2));
        BufferedImage leftHalf = world.getSubimage(xleft,yleft,TankRotationExample.SCREEN_WIDTH/2-1,TankRotationExample.SCREEN_HEIGHT);
        BufferedImage rightHalf = world.getSubimage(xright,yright,TankRotationExample.SCREEN_WIDTH/2,TankRotationExample.SCREEN_HEIGHT);
        g2.drawImage(leftHalf,0,0,null);
        g2.drawImage(rightHalf, TankRotationExample.SCREEN_WIDTH/2, 0, null);
    }
}
