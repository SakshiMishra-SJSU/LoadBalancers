import java.util.concurrent.atomic.AtomicReference;

public class Server {
    private final String id;

    private  Load load = new Load(0.0f);
    Capacity capacity =(new Capacity(0));

    public Server(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Load getLoad() {
        return load;
    }

    public void setLoad(Load load) {
        this.load =load;
    }

    public Capacity getCapacity() {
        return capacity;
    }

    public void setCapacity( Capacity capacity) {
       this. capacity =capacity;
    }

}
