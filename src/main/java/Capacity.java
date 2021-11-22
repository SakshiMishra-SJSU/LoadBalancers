public class Capacity implements Comparable<Capacity>{
    private Integer capacity = 0;

    public Capacity(final Integer capacity) {
        this.capacity = capacity;
    }

    @Override
    public int compareTo(final Capacity capacity) {
        return this.capacity.compareTo(capacity.capacity);
    }

    public Integer getCapacity() {
        return capacity;
    }


}
