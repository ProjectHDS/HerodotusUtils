package youyihj.herodotusutils.alchemy;

import java.util.Objects;

/**
 * @author youyihj
 */
public class AlchemyEssenceStack implements Comparable<AlchemyEssenceStack> {
    private final AlchemyEssence essence;
    private final int count;

    public AlchemyEssenceStack(AlchemyEssence essence, int count) {
        essence.setUsed(true);
        this.essence = essence;
        this.count = count;
    }

    public AlchemyEssence getEssence() {
        return essence;
    }

    public int getCount() {
        return count;
    }

    public String getDisplayName() {
        if (count == 1) {
            return String.valueOf(essence.getSymbol());
        }
        return essence.getSymbol() + String.valueOf(count);
    }

    public AlchemyEssenceStack copy() {
        return new AlchemyEssenceStack(this.getEssence(), this.getCount());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlchemyEssenceStack that = (AlchemyEssenceStack) o;
        return count == that.count && Objects.equals(essence, that.essence);
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    @Override
    public int hashCode() {
        return Objects.hash(essence, count);
    }

    @Override
    public int compareTo(AlchemyEssenceStack o) {
        return essence.compareTo(o.essence);
    }
}
