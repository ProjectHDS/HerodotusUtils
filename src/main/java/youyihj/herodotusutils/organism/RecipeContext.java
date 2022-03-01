package youyihj.herodotusutils.organism;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.tuple.Pair;
import youyihj.herodotusutils.recipe.OrganismRecipe;
import youyihj.herodotusutils.util.Capabilities;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * @author youyihj
 */
public class RecipeContext implements INBTSerializable<NBTTagCompound> {
    private final Multimap<IngredientType<?>, IInputInterface<?>> inputInterfaces = Multimaps.newMultimap(new IdentityHashMap<>(), ArrayList::new);
    private final Multimap<IngredientType<?>, IOutputInterface<?>> outputInterfaces = Multimaps.newMultimap(new IdentityHashMap<>(), ArrayList::new);
    private OrganismRecipe currentRecipe;
    private int ticks;
    private Status status = Status.NO_RECIPE_FOUND;

    public Status getStatus() {
        return status;
    }

    public int getTicks() {
        return ticks;
    }

    @Nullable
    public OrganismRecipe getCurrentRecipe() {
        return currentRecipe;
    }

    public void reset() {
        inputInterfaces.clear();
        outputInterfaces.clear();
        currentRecipe = null;
        ticks = 0;
        status = Status.NO_RECIPE_FOUND;
    }

    public void findRecipe() {
        for (OrganismRecipe recipe : OrganismRecipe.REGISTRY.values()) {
            if (hasEnoughInputs(recipe)) {
                currentRecipe = recipe;
                handleInput();
                status = Status.PROCESSING;
                return;
            }
        }
        status = Status.NO_RECIPE_FOUND;
    }

    public void checkModule(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null) {
            IInputInterface<?> inputInterface = tileEntity.getCapability(Capabilities.INPUT_INTERFACE_CAPABILITY, null);
            if (inputInterface != null) {
                IngredientType<?> ingredientType = inputInterface.getIngredientType();
                inputInterfaces.put(ingredientType, inputInterface);
            }
            IOutputInterface<?> outputInterface = tileEntity.getCapability(Capabilities.OUTPUT_INTERFACE_CAPABILITY, null);
            if (outputInterface != null) {
                IngredientType<?> ingredientType = outputInterface.getIngredientType();
                outputInterfaces.put(ingredientType, outputInterface);
            }
        }
    }

    public void tick() {
        if (status == Status.PROCESSING) {
            ticks++;
        }
        if (ticks == currentRecipe.getTime()) {
            complete();
        }
    }

    public void partialTick() {
        switch (status) {
            case NO_RECIPE_FOUND:
                findRecipe();
                break;
            case INSUFFICIENT_OUTPUT_SPACE:
                complete();
                break;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private boolean hasEnoughInputs(OrganismRecipe recipe) {
        for (IngredientType<?> inputType : recipe.getInputTypes()) {
            Collection<IInputInterface<?>> typeInputInterfaces = inputInterfaces.get(inputType);
            Collection<?> inputEntries = recipe.getInputEntries(inputType);
            for (Object inputEntry : inputEntries) {
                boolean foundInterfaceToIn = false;
                for (IInputInterface typeInputInterface : typeInputInterfaces) {
                    boolean result = typeInputInterface.checkCount(inputEntry);
                    if (result) {
                        foundInterfaceToIn = true;
                        break;
                    }
                }
                if (!foundInterfaceToIn) {
                    return false;
                }
            }
        }
        return true;
    }

    private void complete() {
        boolean result = handleOutput();
        if (result) {
            ticks = 0;
            if (!hasEnoughInputs(currentRecipe)) {
                currentRecipe = null;
                findRecipe();
            }
        } else {
            status = Status.INSUFFICIENT_OUTPUT_SPACE;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void handleInput() {
        for (IngredientType<?> inputType : currentRecipe.getInputTypes()) {
            Collection<IInputInterface<?>> typeInputInterfaces = inputInterfaces.get(inputType);
            Collection<?> inputEntries = currentRecipe.getInputEntries(inputType);
            for (Object inputEntry : inputEntries) {
                for (IInputInterface typeInputInterface : typeInputInterfaces) {
                    if (typeInputInterface.checkCount(inputEntry)) {
                        typeInputInterface.extractIngredient(inputEntry);
                        break;
                    }
                }
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private boolean handleOutput() {
        List<Pair<?, IOutputInterface<?>>> toOutputInterfaces = new ArrayList<>();
        for (IngredientType<?> outputType : currentRecipe.getOutputTypes()) {
            Collection<IOutputInterface<?>> typeOutputInterfaces = outputInterfaces.get(outputType);
            Collection<?> outputEntries = currentRecipe.getOutputEntries(outputType);
            for (Object outputEntry : outputEntries) {
                boolean foundInterfaceToOut = false;
                for (IOutputInterface typeOutputInterface : typeOutputInterfaces) {
                    boolean result = typeOutputInterface.checkCapacity(outputEntry);
                    if (result) {
                        foundInterfaceToOut = true;
                        toOutputInterfaces.add(Pair.of(outputEntry, typeOutputInterface));
                        break;
                    }
                }
                if (!foundInterfaceToOut) {
                    return false;
                }
            }
        }
        for (Pair<?, IOutputInterface<?>> pair : toOutputInterfaces) {
            Object left = pair.getLeft();
            IOutputInterface right = pair.getRight();
            right.insertIngredient(left);
        }
        return true;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("recipe", currentRecipe == null ? "" : currentRecipe.getName());
        tag.setInteger("ticks", ticks);
        tag.setInteger("status", status.ordinal());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        currentRecipe = OrganismRecipe.REGISTRY.get(nbt.getString("recipe"));
        ticks = nbt.getInteger("ticks");
        status = Status.values()[nbt.getInteger("status")];
    }

    public enum Status {
        NO_RECIPE_FOUND,
        PROCESSING,
        INSUFFICIENT_OUTPUT_SPACE
    }
}
