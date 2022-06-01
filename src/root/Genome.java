package root;

import java.util.HashMap;
import java.util.LinkedList;


public class Genome {

    LinkedList<Connection> connections;
    Gene outputGene;

    public Genome(Gene[] inputGenes, Gene outputGene){

        this.outputGene = outputGene;

        this.connections = new LinkedList<Connection>();

        // Create hidden layer
        int numberHiddenGene = (int)(Math.random()*4);
        LinkedList<Gene> hiddenLayer = new LinkedList<>();
        for(int i=0 ; i<numberHiddenGene ; i++){
            hiddenLayer.add(new Gene(GeneType.HIDDEN, null));
        }

        // Create connections (full)
        for(Gene gene : inputGenes){
            if(numberHiddenGene > 0) {
                for(Gene hiddenGene : hiddenLayer){
                    this.connections.add(new Connection(gene, outputGene, Math.random()*2-1));
                }
            } else {
                this.connections.add(new Connection(gene, outputGene, Math.random()*2-1));
            }
        }
        for(Gene hiddenGene : hiddenLayer){
            this.connections.add(new Connection(hiddenGene, outputGene, Math.random()*2-1));
        }

    }

    public Genome(Genome parent, double dispersion, Gene[] inputGenes, Gene outputGene){

        this.outputGene = outputGene;

        this.connections = new LinkedList<Connection>();

        for(Connection connection : parent.connections){
            if(Math.random()<0.02){
                Gene hiddenGene = new Gene(GeneType.HIDDEN,null);
                this.connections.add(new Connection(connection.getIn(),hiddenGene,Math.random()*2-1));
                this.connections.add(new Connection(hiddenGene,connection.getOut(),Math.random()*2-1));
            } else if (Math.random()<0.05){
                this.connections.add(new Connection(connection.getIn(),outputGene,Math.random()*2-1));
            } else {
                this.connections.add(new Connection(connection.getIn(),connection.getOut(),Math.max(-1,Math.min(1,connection.getValue()+(Math.random()-0.5)*dispersion))));
            }
        }
    }

    public double getOutputValue(HashMap<GeneDataType, Double> values){
        // Clear values
        for(Connection connection : this.connections) {
            connection.getIn().resetStoredValue();
            connection.getOut().resetStoredValue();
        }
        // If created well, it will be in the right order
        for(Connection connection : this.connections){
            if(connection.getIn().getType() == GeneType.INPUT){
                double geneValue = values.get(connection.getIn().getDataType());
                connection.getOut().addStoredValue(geneValue * connection.getValue());
            } else { //GeneType.HIDDEN
                double geneValue = connection.getIn().getStoredValue();
                connection.getOut().addStoredValue(geneValue * connection.getValue());
            }
        }
        return this.outputGene.getStoredValue();
    }

    @Override
    public String toString() {
        return "Genome{" +
                "connections=" + connections +
                ", outputGene=" + outputGene +
                '}';
    }
}
