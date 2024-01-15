package Elements;

import java.util.Objects;

public class Pair<F, S> {

    private  F first;
    private  S second;

    public Pair() {

    }
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Pair<F, S> other = (Pair<F, S>) obj;
        return Objects.equals(first, other.getFirst()) && Objects.equals(second, other.getSecond());
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

}
