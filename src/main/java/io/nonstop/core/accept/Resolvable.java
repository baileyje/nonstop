package io.nonstop.core.accept;

public abstract class Resolvable implements Comparable<Resolvable> {

    private final float weight;

    private final int index;

    static Resolvable parse(final String string) {
        return null;
    }

    public Resolvable(float weight, int index) {
        this.weight = weight;
        this.index = index;
    }

    public float getWeight() {
        return weight;
    }

    public int getIndex() {
        return index;
    }

    public abstract boolean satisfies(String value);

    @Override
    public int compareTo(final Resolvable other) {
        int byWeight = Float.compare(other.weight, this.weight);
        if (byWeight == 0) {
            return Integer.compare(index, other.index);
        }
        return byWeight;
    }
}
