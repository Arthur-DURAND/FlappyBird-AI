package root;

public class Gene {
    private GeneType type;
    private GeneDataType dataType;
    private double storedValue;

    public Gene(GeneType type, GeneDataType dataType){
        this.type = type;
        this.dataType = dataType;
        this.storedValue = 0.;
    }

    public GeneType getType() {
        return type;
    }

    public GeneDataType getDataType() {
        return dataType;
    }

    public double getStoredValue() {
        return storedValue;
    }

    public void addStoredValue(double storedValue) {
        this.storedValue += storedValue;
    }

    public void resetStoredValue(){
        this.storedValue = 0.;
    }
}
