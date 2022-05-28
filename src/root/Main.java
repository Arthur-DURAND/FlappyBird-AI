package root;

public class Main {

    public static void main(String[] args) {

        int windowHeight = 800;
        int windowWidth = (int)(windowHeight*2./3.);
        Game game = new Game(windowHeight);
        Window window = new Window(windowWidth, windowHeight, game);
    }
}
