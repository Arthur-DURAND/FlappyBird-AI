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


public class Game extends JPanel implements ActionListener, KeyListener {
    private int score;
    private Bird bird;
    private LinkedList<Pipe> pipes;

    // Floor animation
    private final int vx = 10;
    private int x = 0;
    private int globalX = 0;
    private final int loopX = 36;

    // Images
    BufferedImage backgroundImage;
    BufferedImage floorImage;
    BufferedImage pipeTopImage;
    BufferedImage pipeLayerImage;

    // To know when to summon pipe
    private int pipeTimer;
    private final int pipeTimerSpawn = 40;

    // If still playing or not
    private GameState state;

    private Timer timer;

    private int windowHeight;
    private int floorY;

    private boolean humanPlaying;
    private AIConductor aiConductor;

    public Game(int windowHeight, boolean humanPlaying, AIConductor aiConductor){

        // Game elements
        pipes = new LinkedList<Pipe>();
        this.windowHeight = windowHeight;
        this.floorY = (int)(this.windowHeight * 0.843);
        bird = new Bird(windowHeight);
        this.score = 0;

        // AI or not
        this.humanPlaying = humanPlaying;
        this.aiConductor = aiConductor;

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
        if(this.humanPlaying) {
            bird.tick(true, 0);
        } else {
            this.aiConductor.tick(this);
        }

        // Update x
        this.updateX();

        // Summon pipes
        this.pipeTimer++;
        if (this.pipeTimer > pipeTimerSpawn){
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
        if(this.humanPlaying) {
            boolean collided = this.collision(this.bird);
            if (collided) {
                this.state = GameState.ENDSCREEN;
                this.repaint();
            }
        } else {
            // Check if an ai is still playing
            boolean aiAlive = false;
            for(AI ai : this.aiConductor.getAis()){
                if(ai.isAlive()) {
                    aiAlive = true;
                    boolean collided = this.collision(ai.getBird());
                    if (collided) {
                        ai.feedReward(this.globalX);
                    }
                }
            }
            if(!aiAlive){
                this.generationLost();
            }
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

    public void generationLost(){
        this.aiConductor.newGeneration();
        startGame();
    }

    public void updateX(){
        this.x += this.vx;
        if (this.x > loopX){
            this.x -= loopX;
        }
        // For ai reward
        this.globalX += this.vx;
    }

    public boolean collision(Bird birdToCheck){
        // Pipes
        // TODO bird not square (more like a circle)
        for(Pipe pipe : this.pipes){
            if (pipe.getX() < birdToCheck.getX() + birdToCheck.getBirdWidth() &&
                    pipe.getX() + pipe.getPipeTopWidth() > birdToCheck.getX()){
                if (pipe.getY() - pipe.getGap()/2 > birdToCheck.getY() ||
                        pipe.getY() + pipe.getGap()/2 < birdToCheck.getY() + birdToCheck.getBirdHeight()){
                    return true;
                }
            }
        }

        // Floor
        if(birdToCheck.getY() + birdToCheck.getBirdHeight() > this.floorY){
            return true;
        }

        return false;
    }

    public void startGame(){
        this.pipes = new LinkedList<Pipe>();
        if(humanPlaying){
            this.pipeTimer = 0;
        } else {
            this.pipeTimer = pipeTimerSpawn;
        }
        this.globalX = 0;
        this.score = 0;
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // Background
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        // Floor
        g.drawImage(floorImage, -this.x, this.floorY, 800, 15,this);
        // Bird
        if(this.humanPlaying) {
            this.bird.repaint(g);
        } else {
            this.aiConductor.repaint(g);
        }
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
        if(this.humanPlaying) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (this.state == GameState.WAITING) {
                    this.startGame();
                    this.state = GameState.PLAYING;
                } else if (this.state == GameState.ENDSCREEN) {
                    this.pipes.clear();
                    this.bird = new Bird(windowHeight);
                    this.score = 0;
                    this.state = GameState.WAITING;
                }
                this.bird.jump();
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (this.state == GameState.WAITING) {
                    this.startGame();
                    this.state = GameState.PLAYING;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public LinkedList<Pipe> getPipes() {
        return pipes;
    }

    public int getVx() {
        return vx;
    }

    public int getPipeTimer() {
        return pipeTimer;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public int getFloorY() {
        return floorY;
    }
}
