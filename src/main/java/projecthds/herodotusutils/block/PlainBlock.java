package projecthds.herodotusutils.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import projecthds.herodotusutils.HerodotusUtils;

/**
 * @author youyihj
 */
public class PlainBlock extends Block {
    public PlainBlock(Material materialIn, String name) {
        super(materialIn);
        this.setRegistryName(name);
        this.setCreativeTab(CreativeTabs.MISC);
        this.blockHardness = 5.0f;
        this.blockResistance = 50.0f;
        this.setUnlocalizedName(HerodotusUtils.MOD_ID + "." + name);
    }
}
