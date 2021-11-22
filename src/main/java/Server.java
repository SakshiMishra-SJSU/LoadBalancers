import java.util.concurrent.atomic.AtomicReference;

public class Server {
    private final String id;

    private final AtomicReference<Load> loadReference = new AtomicReference<>(new Load(0.0f));
    private final AtomicReference<Capacity> capacityAtomicReference = new AtomicReference<>(new Capacity(0));

    public Server(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Load getLoad() {
        return loadReference.get();
    }

    public void setLoad(final Load load) {
        loadReference.set(load);
    }

    public Capacity getCapacity() {
        return capacityAtomicReference.get();
    }

    public void setCapacity(final Capacity capacity) {
        capacityAtomicReference.set(capacity);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Server)) {
            return false;
        }
        Server other = (Server) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Node[id:").append(id);
        builder.append(", ").append(loadReference.get());
        builder.append(", ").append(capacityAtomicReference.get());
        builder.append("]");
        return builder.toString();
    }
}
