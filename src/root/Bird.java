package root;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bird {

    int y;
    int vy;
    final int x = 100;
    final int gravity = 2;
    final int jumpHeight = 20;
    final int maxSpeed = 20;

    final int birdWidth = (int)(34*1.75);
    final int birdHeight = (int)(24*1.75);
    BufferedImage birdImage;

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

    public void tick(){
        this.y += this.vy;
        this.vy = Math.min(this.vy + this.gravity, this.maxSpeed);
    }

    public void jump(){
        this.vy = -jumpHeight;
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
}
