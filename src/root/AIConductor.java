package root;

import java.awt.*;

public class AIConductor {

    private AI[] ais;
    private Gene[] genes;
    private int windowHeight;

    public AIConductor(Gene[] genes, int aiNumber, int windowHeight){
        this.genes = genes;
        this.windowHeight = windowHeight;

        this.ais = new AI[aiNumber];
        for(int i=0 ; i<aiNumber ; i++){
            ais[i] = new AI(genes, windowHeight);
        }
    }

    public void tick(Game game){
        for(AI ai : this.ais){
            if(ai.isAlive()) {
                // Check if need to jump
                ai.play(game);
            }
            ai.getBird().tick(ai.isAlive(), game.getVx());
        }
    }

    public AI[] getAis() {
        return ais;
    }

    public void newGeneration(){
        AI bestAi = null;
        int bestValue = 0;
        AI secondBestAi = null;
        int secondBestValue = 0;

        for(AI ai : ais){
            if(ai.getReward() > bestValue){
                secondBestValue = bestValue;
                secondBestAi = bestAi;
                bestValue = ai.getReward();
                bestAi = ai;
            } else if(ai.getReward() > secondBestValue){
                secondBestValue = ai.getReward();
                secondBestAi = ai;
            }
        }

        System.out.println(bestAi);

        for(int i=0 ; i<this.ais.length/4 ; i++){
            ais[i] = new AI(bestAi, bestAi, this.genes, this.windowHeight);
        }
        for(int i=this.ais.length/4 ; i<this.ais.length/2 ; i++){
            ais[i] = new AI(secondBestAi, secondBestAi, this.genes, this.windowHeight);
        }
        for(int i=this.ais.length/2 ; i<this.ais.length ; i++){
            ais[i] = new AI(bestAi, secondBestAi, this.genes, this.windowHeight);
        }
    }

    public void repaint(Graphics g){
        for(AI ai : this.ais){
            ai.getBird().repaint(g);
        }
    }
}
