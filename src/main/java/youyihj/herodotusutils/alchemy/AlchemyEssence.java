package youyihj.herodotusutils.alchemy;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author youyihj
 */
public class AlchemyEssence implements Comparable<AlchemyEssence> {
    private final int index;
    private final char symbol;
    private boolean used;
    private static final int TOTAL_COUNT = 89;
    private static final AlchemyEssence[] ESSENCES = new AlchemyEssence[TOTAL_COUNT];

    private AlchemyEssence(int index, char symbol) {
        this.index = index;
        this.symbol = symbol;
    }

    public static AlchemyEssence indexOf(int index) {
        return ESSENCES[index];
    }

    public static List<AlchemyEssence> getUsedEssences() {
        return Arrays.stream(ESSENCES).filter(AlchemyEssence::isUsed).collect(Collectors.toList());
    }

    static {
        for (int i = 0; i < TOTAL_COUNT; i++) {
            ESSENCES[i] = new AlchemyEssence(i, (char) (0x16a0 + i));
        }
    }

    public int getIndex() {
        return index;
    }

    public char getSymbol() {
        return symbol;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlchemyEssence that = (AlchemyEssence) o;
        return index == that.index && symbol == that.symbol;
    }

    @Override
    public String toString() {
        return "AlchemyEssence{" +
                "index=" + index +
                ", symbol=" + symbol +
                ", used=" + used +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, symbol);
    }

    @Override
    public int compareTo(AlchemyEssence o) {
        return Integer.compare(index, o.index);
    }
}
