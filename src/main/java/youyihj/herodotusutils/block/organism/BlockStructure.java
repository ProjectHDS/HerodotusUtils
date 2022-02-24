package youyihj.herodotusutils.block.organism;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import youyihj.herodotusutils.block.PlainBlock;

import java.util.Locale;

/**
 * @author youyihj
 */
public class BlockStructure extends PlainBlock {
    private final StructureTier tier;
    private final Item item;

    public BlockStructure(String name, StructureTier tier) {
        super(Material.IRON, name = tier.name().toLowerCase(Locale.ROOT) + "_" + name);
        this.tier = tier;
        this.item = new ItemBlock(this);
        this.item.setRegistryName(name);
    }

    public Item getItem() {
        return item;
    }

    public StructureTier getTier() {
        return tier;
    }
}
