package projecthds.herodotusutils.block.dimcrystal;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import projecthds.herodotusutils.block.PlainBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BlockOreDimCrystal extends PlainBlock {
    public static final String NAME = "dimcrystal";
    public static final List<BlockOreDimCrystal> BLOCKS = new ArrayList<>(3);
    public static final List<BlockOreDimCrystal.Item> ITEM_BLOCKS = new ArrayList<>(3);
    private final String content;

    static {
        for (String type: new String[]{"Copper", "Iron", "Tin", "Lead"}) {
            BlockOreDimCrystal block = new BlockOreDimCrystal(type);
            BLOCKS.add(block);
            BlockOreDimCrystal.Item itemBlock = new BlockOreDimCrystal.Item(block);
            itemBlock.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
            ITEM_BLOCKS.add(itemBlock);
        }
    }

    BlockOreDimCrystal(String ore) {
        super(Material.GLASS, NAME + "_" + ore.toLowerCase(Locale.ENGLISH));
        content = ore.toLowerCase(Locale.ENGLISH);
    }

    public static class Item extends ItemBlock {
        public Item(Block block) {
            super(block);
        }
    }
}
