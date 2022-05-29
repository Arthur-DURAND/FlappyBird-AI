package root;

import java.util.*;

public class AI {
    private HashMap<Gene, HashMap<Gene, Double>> geneConnections;
    private Gene[] genes;
    private Bird bird;
    private boolean alive;
    private int reward;

    public AI(Gene[] genes, int windowHeight){
        // Set gene array
        this.genes = genes;
        // Set default gene connections
        // First key is connection's output, 2nd key is connection's input
        // 2 layers of genes (no hidden layer)
        this.geneConnections = new HashMap<Gene, HashMap<Gene,Double>>();
        for(Gene gene : this.genes){
            // Set input
            if(gene.type == GeneType.OUTPUT){
                this.geneConnections.put(gene, new HashMap<Gene,Double>());
                for(Gene gene2 : this.genes){
                    // Link to each output
                    if(gene2.type == GeneType.INPUT){
                        this.geneConnections.get(gene).put(gene2, (Math.random()- 0.5)*2);
                    }
                }
            }
        }
        // Set bird
        this.bird = new Bird(windowHeight);
        this.alive = true;
        this.reward = 0;
    }

    public AI(AI parent1, AI parent2, Gene[] genes, int windowHeight){
        // Set gene array and instantiate this.geneConnections
        this(genes, windowHeight);
        // Set gene connections. Average of both parents
        for(Gene gene : this.genes){
            for(Gene gene2 : this.genes){
                if(this.geneConnections.get(gene) != null && this.geneConnections.get(gene).get(gene2) != null){
                    double average = (parent1.geneConnections.get(gene).get(gene2) + parent2.geneConnections.get(gene).get(gene2))/2.;
                    if(Math.random() < 0.3){
                        this.geneConnections.get(gene).put(gene2, Math.min(1, Math.max(-1, average + 0.5 * (Math.random() - 0.5))));
                    } else if (Math.random() < 0.5){
                        this.geneConnections.get(gene).put(gene2, Math.min(1, Math.max(-1, average + 0.1 * (Math.random() - 0.5))));
                    } else {
                        this.geneConnections.get(gene).put(gene2, Math.min(1, Math.max(-1, average + 0.005 * (Math.random() - 0.5))));
                    }
                }
            }
        }
    }

    public void play(Game game){
        for(Gene gene : this.genes) {
            if(gene.type == GeneType.OUTPUT){
                double sumValues = (double)0;
                for (Gene gene2 : this.genes) {
                    if(gene2.type == GeneType.INPUT){
                        Double value = (double) 0;
                        switch (gene2.dataType){
                            case VY:
                                value = ((double) this.bird.getVy() / (double) this.bird.getMaxSpeed());
                                break;
                            case X_DISTANCE_PIPE:
                                LinkedList<Pipe> pipes = game.getPipes();
                                int xDistancePipe = Integer.MAX_VALUE;
                                for(Pipe pipe : pipes){
                                    if(pipe.getX() - this.bird.getX() > 0){
                                        xDistancePipe = Math.min(xDistancePipe, pipe.getX() - this.bird.getX());
                                    }
                                }
                                value = Math.min(1,((double) xDistancePipe / (double) (game.getVx() * game.getPipeTimer())));
                                break;
                            case Y_DISTANCE_UP_PIPE:
                                pipes = game.getPipes();
                                xDistancePipe = Integer.MAX_VALUE;
                                int yDistancePipe = 0;
                                for(Pipe pipe : pipes){
                                    if(pipe.getX() + pipe.getPipeTopWidth() - this.bird.getX() > 0){
                                        if(pipe.getX() + pipe.getPipeTopWidth() - this.bird.getX() < xDistancePipe) {
                                            xDistancePipe = pipe.getX() + pipe.getPipeTopWidth() - this.bird.getX();
                                            yDistancePipe = pipe.getY() - pipe.getGap() / 2 - this.bird.getY();
                                        }
                                    }
                                }
                                value = ((double) yDistancePipe / (double) (game.getWindowHeight()));
                                break;
                            case Y_DISTANCE_BOTTOM_PIPE:
                                pipes = game.getPipes();
                                xDistancePipe = Integer.MAX_VALUE;
                                yDistancePipe = 0;
                                for(Pipe pipe : pipes){
                                    if(pipe.getX() + pipe.getPipeTopWidth() - this.bird.getX() > 0){
                                        if(pipe.getX() + pipe.getPipeTopWidth() - this.bird.getX() < xDistancePipe) {
                                            xDistancePipe = pipe.getX() + pipe.getPipeTopWidth() - this.bird.getX();
                                            yDistancePipe = pipe.getY() + pipe.getGap() / 2 - this.bird.getY();
                                        }
                                    }
                                }
                                value = ((double) yDistancePipe / (double) (game.getWindowHeight()));
                                break;
                            case Y_DISTANCE_FLOOR:
                                value = ((double) (-this.bird.getY()+game.getFloorY()*5./6.) / (double) (game.getFloorY()));
                                if(value < 0){
                                    value = 0.;
                                }
                                break;
                            case NO_PIPE:
                                value = game.getPipes().size()==0?(double)1:(double)0;
                        }
                        double geneValue = this.geneConnections.get(gene).get(gene2) * value;
                        sumValues += geneValue;
                    }
                }
                double finalValue = Math.tanh(sumValues);
                if(finalValue > 0){
                    switch(gene.dataType){
                        case JUMP:
                            this.bird.jump();
                    }
                }
            }
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
        String result = "";
        for (Map.Entry<Gene, HashMap<Gene, Double>> entry : geneConnections.entrySet()) {
            for (Map.Entry<Gene, Double> entry2 : entry.getValue().entrySet()) {
                result += entry2.getKey().dataType + " : " + entry2.getValue() + " \n ";
            }
        }
        return "AI : \n " + result;
    }
}
