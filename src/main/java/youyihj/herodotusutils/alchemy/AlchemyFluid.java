package youyihj.herodotusutils.alchemy;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author youyihj
 */
public class AlchemyFluid implements INBTSerializable<NBTTagList> {
    private final Collection<AlchemyEssenceStack> essenceStacks = new TreeSet<>();

    public AlchemyFluid(AlchemyEssenceStack... stacks) {
        Map<AlchemyEssence, Integer> map = new HashMap<>();
        for (AlchemyEssenceStack stack : stacks) {
            map.merge(stack.getEssence(), stack.getCount(), Integer::sum);
        }
        map.forEach((essence, count) -> essenceStacks.add(new AlchemyEssenceStack(essence, count)));
    }

    public Collection<AlchemyEssenceStack> getEssenceStacks() {
        return essenceStacks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlchemyFluid that = (AlchemyFluid) o;
        return Objects.equals(essenceStacks, that.essenceStacks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(essenceStacks);
    }

    public String getDisplayName() {
        StringBuilder sb = new StringBuilder();
        for (AlchemyEssenceStack essenceStack : essenceStacks) {
            sb.append(essenceStack.getDisplayName());
        }
        return sb.toString();
    }

    public AlchemyFluid add(AlchemyFluid other) {
        AlchemyEssenceStack[] newContents = Stream.concat(this.essenceStacks.stream(), other.essenceStacks.stream())
                .toArray(AlchemyEssenceStack[]::new);
        return new AlchemyFluid(newContents);
    }

    public AlchemyFluid[] separate(int targetAmount) {
        AlchemyFluid[] result = new AlchemyFluid[targetAmount];
        AlchemyEssenceStack[] stacks = essenceStacks.toArray(new AlchemyEssenceStack[0]);
        if (stacks.length == 1) {
            AlchemyEssenceStack stack = stacks[0];
            int count = stack.getCount();
            int eachCount = count / targetAmount;
            int modulo = count % targetAmount;
            for (int i = 0; i < targetAmount; i++) {
                int thisCount = eachCount;
                if (modulo > i) {
                    thisCount++;
                }
                result[i] = new AlchemyFluid(new AlchemyEssenceStack(stack.getEssence(), thisCount));
            }
        } else if (stacks.length > targetAmount) {
            for (int i = 0; i < targetAmount - 1; i++) {
                result[i] = new AlchemyFluid(stacks[i]);
            }
            result[targetAmount - 1] = new AlchemyFluid(Arrays.copyOfRange(stacks, targetAmount - 1, stacks.length));
        } else {
            for (int i = 0; i < stacks.length; i++) {
                result[i] = new AlchemyFluid(stacks[i]);
            }
        }
        return result;
    }

    @Override
    public NBTTagList serializeNBT() {
        NBTTagList list = new NBTTagList();
        for (AlchemyEssenceStack essenceStack : essenceStacks) {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            nbtTagCompound.setInteger("essence", essenceStack.getEssence().getIndex());
            nbtTagCompound.setInteger("count", essenceStack.getCount());
            list.appendTag(nbtTagCompound);
        }
        return list;
    }

    @Override
    public void deserializeNBT(NBTTagList nbt) {
        for (NBTBase nbtBase : nbt) {
            NBTTagCompound compound = (NBTTagCompound) nbtBase;
            essenceStacks.add(new AlchemyEssenceStack(AlchemyEssence.indexOf(compound.getInteger("essence")), compound.getInteger("count")));
        }
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
