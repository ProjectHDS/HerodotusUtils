package youyihj.herodotusutils.network.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.awt.*;

/**
 * @author youyihj
 */
public class ItemInputInterfaceContainer extends ItemContainer {
    public ItemInputInterfaceContainer(EntityPlayer player, World world, BlockPos pos) {
        super(player, world, pos, 12);
    }

    @Override
    protected Point getSlotPosition(int index) {
        int row = index % 3;
        int column = index / 3;
        int y = 26 + row * 18;
        int x;
        switch (column) {
            case 0:
                x = 39;
                break;
            case 1:
                x = 71;
                break;
            case 2:
                x = 103;
                break;
            case 3:
                x = 121;
                break;
            default:
                x = 0;
        }
        return new Point(x, y);
    }
}
