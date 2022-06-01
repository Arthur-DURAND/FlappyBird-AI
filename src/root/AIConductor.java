package root;

import java.awt.*;
import java.util.HashMap;

public class AIConductor {

    private AI[] ais;
    private Gene[] inputGenes;
    private Gene outputGene;
    private int windowHeight;

    public AIConductor(Gene[] inputGenes, Gene outputGene, int aiNumber, int windowHeight){
        this.inputGenes = inputGenes;
        this.outputGene = outputGene;
        this.windowHeight = windowHeight;

        this.ais = new AI[aiNumber];
        for(int i=0 ; i<aiNumber ; i++){
            ais[i] = new AI(inputGenes, outputGene, windowHeight);
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

        for(int i=0 ; i<this.ais.length/8 ; i++){
            ais[i] = new AI(bestAi, 0.01, this.inputGenes, this.outputGene, this.windowHeight);
        }
        for(int i=this.ais.length/8 ; i<this.ais.length/4 ; i++){
            ais[i] = new AI(bestAi, 0.1, this.inputGenes, this.outputGene, this.windowHeight);
        }
        for(int i=this.ais.length/4 ; i<this.ais.length*3/8. ; i++){
            ais[i] = new AI(secondBestAi, 0.01, this.inputGenes, this.outputGene, this.windowHeight);
        }
        for(int i=(int)(this.ais.length*3./8.) ; i<this.ais.length/2 ; i++){
            ais[i] = new AI(secondBestAi, 0.1, this.inputGenes, this.outputGene, this.windowHeight);
        }
        for(int i=this.ais.length/2 ; i<this.ais.length-2 ; i++){
            ais[i] = new AI(this.inputGenes, this.outputGene, this.windowHeight);
        }
        ais[this.ais.length-2] = bestAi;
        ais[this.ais.length-1] = secondBestAi;
    }

    public void repaint(Graphics g){
        for(AI ai : this.ais){
            ai.getBird().repaint(g);
        }
    }
}
