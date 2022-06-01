package root;

public class Connection {
    private final Gene in;
    private final Gene out;
    private final double value;
    public Connection(Gene in, Gene out, double value){
        this.in = in;
        this.out = out;
        this.value = value;
    }

    public Gene getIn() {
        return in;
    }

    public Gene getOut() {
        return out;
    }

    public double getValue() {
        return value;
    }
}
