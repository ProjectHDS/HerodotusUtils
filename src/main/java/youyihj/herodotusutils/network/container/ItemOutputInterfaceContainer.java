package youyihj.herodotusutils.network.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.awt.*;

/**
 * @author youyihj
 */
public class ItemOutputInterfaceContainer extends ItemContainer {
    public ItemOutputInterfaceContainer(EntityPlayer player, World world, BlockPos pos) {
        super(player, world, pos, 16);
    }

    @Override
    protected Point getSlotPosition(int index) {
        int row = index / 4;
        int column = index % 4;
        return new Point(53 + column * 18, 13 + row * 18);
    }
}
