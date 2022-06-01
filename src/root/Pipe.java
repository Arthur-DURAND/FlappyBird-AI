package root;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Pipe {
    private int x;
    private int vx;
    private int y;
    private final int gap = 150; //old 170
    private BufferedImage pipeTopImage;
    private BufferedImage pipeLayerImage;
    private final int pipeLayerWidth = 85;
    private final int pipeLayerHeight = (int)(this.pipeLayerWidth*61./124.);
    private final int pipeTopWidth = (int)(this.pipeLayerWidth*1.2);
    private final int pipeTopHeight = (int)(this.pipeTopWidth*62./134.);
    private boolean scored;

    private int windowHeight;

    public Pipe(int windowWidth, int windowHeight, int vx, BufferedImage pipeTopImage, BufferedImage pipeLayerImage){
        this.x = windowWidth;
        this.y = (int)(windowHeight*2./5.) + (int)((Math.random()-0.5) * (windowHeight*2./5.));
        this.vx = vx;
        this.windowHeight = windowHeight;

        this.pipeTopImage = pipeTopImage;
        this.pipeLayerImage = pipeLayerImage;

        this.scored = false;
    }

    public void tick(){
        this.x -= this.vx;
    }

    public boolean toDestroy(){
        return this.x + this.pipeTopWidth < 0;
    }

    public void repaint(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        for(int i = 0; i < this.y - this.gap/2 - this.pipeLayerHeight - 1 ; i += this.pipeLayerHeight-1){
            g2d.drawImage(pipeLayerImage, this.x, i, this.pipeLayerWidth, this.pipeLayerHeight, null);
        }
        for(int i = this.y + this.gap/2 ; i <= windowHeight ; i += this.pipeLayerHeight-1){
            g2d.drawImage(pipeLayerImage, this.x, i, this.pipeLayerWidth, this.pipeLayerHeight, null);
        }
        g2d.drawImage(pipeTopImage, this.x - (this.pipeTopWidth - this.pipeLayerWidth)/2, this.y - this.gap/2 - this.pipeTopHeight, this.pipeTopWidth, this.pipeTopHeight, null);
        g2d.drawImage(pipeTopImage, this.x - (this.pipeTopWidth - this.pipeLayerWidth)/2, this.y + this.gap/2, this.pipeTopWidth, this.pipeTopHeight, null);

    }

    public int getX() {
        return x;
    }

    public int getVx() {
        return vx;
    }

    public int getY() {
        return y;
    }

    public int getGap() {
        return gap;
    }

    public int getPipeTopWidth() {
        return pipeTopWidth;
    }

    public boolean isScored() {
        return scored;
    }

    public void setScored(boolean scored) {
        this.scored = scored;
    }
}
