package youyihj.herodotusutils.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import youyihj.herodotusutils.network.container.CreatureDataAnalyzerContainer;
import youyihj.herodotusutils.network.container.CreatureDataAnalyzerGui;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public enum GuiHandler implements IGuiHandler {
    INSTANCE;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == 0) {
            return new CreatureDataAnalyzerContainer(player, world, new BlockPos(x, y, z));
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == 0) {
            return new CreatureDataAnalyzerGui(new CreatureDataAnalyzerContainer(player, world, new BlockPos(x, y, z)));
        }
        return null;
    }
}
