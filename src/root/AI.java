package root;

import java.util.*;

public class AI {
    private Genome genome;
    private Gene[] inputGenes;
    private Gene outputGene;

    private Bird bird;
    private boolean alive;
    private int reward;

    public AI(Gene[] inputGenes, Gene outputGene, int windowHeight){
        // Set genome
        this.inputGenes = inputGenes;
        this.outputGene = outputGene;
        this.genome = new Genome(inputGenes, outputGene);

        // Set bird
        this.bird = new Bird(windowHeight);
        this.alive = true;
        this.reward = 0;
    }

    public AI(AI parent1, double dispersion, Gene[] inputGenes, Gene outputGene, int windowHeight){
        // Set genome
        this.inputGenes = inputGenes;
        this.outputGene = outputGene;

        this.genome = new Genome(parent1.getGenome(), dispersion, inputGenes, outputGene);

        // Set bird
        this.bird = new Bird(windowHeight);
        this.alive = true;
        this.reward = 0;
    }

    public void play(Game game){

        LinkedList<Pipe> pipes = game.getPipes();
        int xDistancePipe = Integer.MAX_VALUE;
        int yDistanceUpPipe = 0;
        int yDistanceBottomPipe = 0;
        for(Pipe pipe : pipes){
            if(pipe.getX() + pipe.getPipeTopWidth() - this.bird.getX() > 0){
                if(pipe.getX() + pipe.getPipeTopWidth() - this.bird.getX() < xDistancePipe) {
                    xDistancePipe = pipe.getX() + pipe.getPipeTopWidth() - this.bird.getX();
                    yDistanceUpPipe = pipe.getY() - pipe.getGap() / 2 - this.bird.getY();
                    yDistanceBottomPipe = pipe.getY() + pipe.getGap() / 2 - this.bird.getY();
                }
            }
        }

        double valueVY = ((double) this.bird.getVy() / (double) this.bird.getMaxSpeed());
        double valueX_DISTANCE_PIPE = ((double) xDistancePipe / (double) (game.getVx() * game.getPipeTimerSpawn()));
        double valueY_DISTANCE_UP_PIPE = ((double) yDistanceUpPipe / (double) (game.getWindowHeight()));
        double valueY_DISTANCE_BOTTOM_PIPE = ((double) yDistanceBottomPipe / (double) (game.getWindowHeight()));
        double valueY_DISTANCE_FLOOR = ((double) (this.bird.getY()+game.getFloorY()) / (double) (game.getFloorY()));

        HashMap<GeneDataType, Double> values = new HashMap<GeneDataType, Double>();
        values.put(GeneDataType.VY,valueVY);
        values.put(GeneDataType.X_DISTANCE_PIPE,valueX_DISTANCE_PIPE);
        values.put(GeneDataType.Y_DISTANCE_UP_PIPE,valueY_DISTANCE_UP_PIPE);
        values.put(GeneDataType.Y_DISTANCE_BOTTOM_PIPE,valueY_DISTANCE_BOTTOM_PIPE);
        values.put(GeneDataType.Y_DISTANCE_FLOOR,valueY_DISTANCE_FLOOR);

        // todo verify those values

        double outputValue = this.genome.getOutputValue(values);
        if(outputValue>0){
            this.bird.jump();
        }
    }

    public void feedReward(int x){
        this.reward = x;
        this.alive = false;
    }

    public Bird getBird() {
        return bird;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getReward() {
        return reward;
    }

    @Override
    public String toString() {
        return "AI : \n " + genome.toString();
    }

    public Genome getGenome() {
        return genome;
    }
}
