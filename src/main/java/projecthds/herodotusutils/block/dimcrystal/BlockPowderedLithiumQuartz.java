package projecthds.herodotusutils.block.dimcrystal;

import net.minecraft.block.material.Material;
import projecthds.herodotusutils.block.PlainBlock;

public class BlockPowderedLithiumQuartz extends PlainBlock {

    public static final BlockPowderedLithiumQuartz INSTANCE = new BlockPowderedLithiumQuartz();

    private BlockPowderedLithiumQuartz() {
        super(Material.ROCK, "powdered_lithium_quartz_block");
    }


}
