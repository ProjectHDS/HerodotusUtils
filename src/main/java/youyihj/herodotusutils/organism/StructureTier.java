package youyihj.herodotusutils.organism;

import youyihj.herodotusutils.block.organism.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author youyihj
 */
public enum StructureTier {
    BRASS(7, "brass") {
        @Override
        public void init() {
            putBlock(BlockORELauncher.BRASS);
            putBlock(BlockFrameworks.BRASS);
            putBlock(BlockItemInputInterface.BRASS);
            putBlock(BlockItemOutputInterface.BRASS);
            putBlock(BlockFluidInputInterface.BRASS);
            putBlock(BlockFluidOutputInterface.BRASS);
        }
    };

    StructureTier(int size, String name2) {
        this.size = size;
        this.name2 = name2;
    }

    private final int size;
    private final String name2;
    private final List<BlockStructure> blocks = new ArrayList<>();

    public int getSize() {
        return size;
    }

    public void putBlock(BlockStructure block) {
        blocks.add(block);
    }

    public void register(Consumer<BlockStructure> registerFunction) {
        blocks.forEach(registerFunction);
    }

    public String getName2() {
        return name2;
    }

    public abstract void init();
}
