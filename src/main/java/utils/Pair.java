package utils;

public record Pair<T, S>(T a, S b) {

    public T getA() {
        return a;
    }

    public S getB() {
        return b;
    }
}
