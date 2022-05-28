package root;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame {
    Game game;

    public Window(int windowWidth, int windowHeight, Game game){
        super("Flappy Bird");
        this.setSize(windowWidth, windowHeight);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(Window.EXIT_ON_CLOSE);

        this.game = game;

        this.add(this.game);
        this.addKeyListener(this.game);

        this.game.startGame();
        this.setVisible(true);
    }
}
