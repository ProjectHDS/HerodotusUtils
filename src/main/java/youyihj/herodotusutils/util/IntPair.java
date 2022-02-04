package youyihj.herodotusutils.util;

import java.util.Objects;

/**
 * @author youyihj
 */
public class IntPair {
    private final int a;
    private final int b;

    public IntPair(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntPair intPair = (IntPair) o;
        return a == intPair.a && b == intPair.b;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
