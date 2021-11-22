public class Load implements Comparable<Load>{

    private Float loadValue = 0.0f;

    public Load(final Float loadValue) {
        this.loadValue = loadValue;
    }

    @Override
    public int compareTo(final Load load) {
        return this.loadValue.compareTo(load.loadValue);
    }

    public Float getLoadValue() {
        return loadValue;
    }


}
