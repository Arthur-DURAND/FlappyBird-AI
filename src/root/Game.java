package root;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Game extends JPanel implements ActionListener, KeyListener {
    private int score;
    private Bird bird;
    private LinkedList<Pipe> pipes;

    // Floor animation
    private final int vx = 10;
    private int x = 0;
    private final int loopX = 36;

    // Images
    BufferedImage backgroundImage;
    BufferedImage floorImage;
    BufferedImage pipeTopImage;
    BufferedImage pipeLayerImage;

    // To know when to summon pipe
    private int pipeTimer = 0;

    // If still playing or not
    private GameState state;

    private Timer timer;

    private int windowHeight;

    public Game(int windowHeight){

        // Game elements
        pipes = new LinkedList<Pipe>();
        this.windowHeight = windowHeight;
        bird = new Bird(windowHeight);
        this.score = 0;

        // Load images
        try {
            this.backgroundImage = ImageIO.read(new File("images/background.png"));
        } catch (IOException e){
            System.err.println("Cannot read background image !");
            System.exit(-1);
        }
        try {
            this.floorImage = ImageIO.read(new File("images/floor.png"));
        } catch (IOException e){
            System.err.println("Cannot read floor image !");
            System.exit(-1);
        }
        try {
            this.pipeTopImage = ImageIO.read(new File("images/pipe_top.png"));
        } catch (IOException e){
            System.err.println("Cannot read pipe_top image !");
            System.exit(-1);
        }
        try {
            this.pipeLayerImage = ImageIO.read(new File("images/pipe_layer.png"));
        } catch (IOException e){
            System.err.println("Cannot read pipe_layer image !");
            System.exit(-1);
        }

        // State
        this.state = GameState.WAITING;

        // Timer
        this.timer = new Timer(1000/60, this);
        this.timer.start();

    }

    // update stuff on timer
    public void tick() {
        bird.tick();

        // Update x
        this.updateX();

        // Summon pipes
        this.pipeTimer++;
        if (this.pipeTimer > 40){
            this.pipeTimer = 0;
            this.pipes.add(new Pipe(this.getWidth(), this.getHeight(), this.vx, this.pipeTopImage, this.pipeLayerImage));
        }
        for(int i=0 ; i<this.pipes.size() ; i++){
            Pipe pipe = this.pipes.get(i);
            pipe.tick();
            if(pipe.toDestroy()){
                this.pipes.remove(pipe);
                i--;
            }
        }

        // Check for collisions
        boolean collided = this.collision();
        if(collided){
            this.state = GameState.ENDSCREEN;
            this.repaint();
        }

        // Update score
        for(Pipe pipe : this.pipes){
            if(pipe.getX() + pipe.getPipeTopWidth() < this.bird.getX()){
                if(!pipe.isScored()){
                    score++;
                    pipe.setScored(true);
                }
            }
        }
    }

    public void updateX(){
        this.x += this.vx;
        if (this.x > loopX){
            this.x -= loopX;
        }
    }

    public boolean collision(){
        // Pipes
        // TODO bird not square (more like a circle)
        for(Pipe pipe : this.pipes){
            if (pipe.getX() < this.bird.getX() + this.bird.getBirdWidth() &&
                    pipe.getX() + pipe.getPipeTopWidth() > this.bird.getX()){
                if (pipe.getY() - pipe.getGap()/2 > this.bird.getY() ||
                        pipe.getY() + pipe.getGap()/2 < this.bird.getY() + this.bird.getBirdHeight()){
                    return true;
                }
            }
        }

        // Floor
        if(this.bird.getY() + this.bird.getBirdHeight() > (int)(this.getHeight() * 0.885)){
            return true;
        }

        return false;
    }

    public void startGame(){

    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // Background
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        // Floor
        g.drawImage(floorImage, -this.x, (int)(this.getHeight() * 0.885), 800, 15,this);
        // Bird
        this.bird.repaint(g);
        // Pipes
        for(Pipe pipe : this.pipes){
            pipe.repaint(g);
        }
        // Score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 48));
        int stringSize = 40 * (int)(Math.max(Math.log10(score),0)+1);
        g.drawString(Integer.toString(score),this.getWidth()/2 - stringSize/2, 50);
    }

    // Catch timer
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.timer) {
            if(this.state == GameState.PLAYING) {
                this.tick();
            }  else if (this.state == GameState.WAITING){
                this.updateX();
            }
            this.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    // Catch key up (arrow up)
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP){
            if(this.state == GameState.WAITING){
                this.state = GameState.PLAYING;
            } else if(this.state == GameState.ENDSCREEN){
                this.pipes.clear();
                this.bird = new Bird(windowHeight);
                this.score = 0;
                this.state = GameState.WAITING;
            }
            this.bird.jump();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
