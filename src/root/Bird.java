package root;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bird {

    private int y;
    private int vy;
    private int x = 100;
    private final int gravity = 2;
    private final int jumpHeight = 20;
    private final int maxSpeed = 20;

    private final int birdWidth = (int)(34*1.75);
    private final int birdHeight = (int)(24*1.75);
    private BufferedImage birdImage;

    public Bird(int windowHeight){

        this.y = windowHeight/2;
        this.vy = 1;

        //Bird image
        try {
            this.birdImage = ImageIO.read(new File("images/Bird.png"));
        } catch (IOException e){
            System.err.println("Cannot read bird image !");
            System.exit(-1);
        }
    }

    public void tick(boolean isAlive, int vx){
        if(isAlive) {
            this.y += this.vy;
            this.vy = Math.min(this.vy + this.gravity, this.maxSpeed);
        } else {
            this.x -= vx;
        }
    }

    public void jump(){
        if(this.y > 0) {
            this.vy = -jumpHeight;
        }
    }

    public void repaint(Graphics g){
        g.drawImage(birdImage, this.x, this.y, birdWidth, birdHeight, null);
    }

    public int getY() {
        return y;
    }

    public int getBirdWidth() {
        return birdWidth;
    }

    public int getBirdHeight() {
        return birdHeight;
    }

    public int getX() {
        return x;
    }

    public int getVy() {
        return vy;
    }

    public int getMaxSpeed(){
        return maxSpeed;
    }


}
