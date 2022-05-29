package root;

public class Main {

    public static void main(String[] args) {

        int windowHeight = 800;
        int windowWidth = (int)(windowHeight*2./3.);

        // Setup ai
        Gene[] genes = {
                //new Gene(GeneType.INPUT, GeneDataType.X_DISTANCE_PIPE),
                //new Gene(GeneType.INPUT, GeneDataType.NO_PIPE),
                new Gene(GeneType.INPUT, GeneDataType.Y_DISTANCE_UP_PIPE),
                new Gene(GeneType.INPUT, GeneDataType.Y_DISTANCE_BOTTOM_PIPE),
                new Gene(GeneType.INPUT, GeneDataType.Y_DISTANCE_FLOOR),
                new Gene(GeneType.INPUT, GeneDataType.VY),
                new Gene(GeneType.OUTPUT, GeneDataType.JUMP)
        };
        AIConductor aiConductor = new AIConductor(genes, 5000, windowHeight);

        boolean humanPlaying = false;

        // Setup game
        Game game = new Game(windowHeight, humanPlaying, aiConductor);
        Window window = new Window(windowWidth, windowHeight, game);
    }
}
