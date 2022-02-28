package youyihj.herodotusutils.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import youyihj.herodotusutils.HerodotusUtils;
import youyihj.herodotusutils.network.container.*;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public enum GuiHandler implements IGuiHandler {
    INSTANCE;

    private final ResourceLocation ITEM_INPUT_GUI_BACKGROUND = HerodotusUtils.rl("textures/gui/item_input_interface.png");
    private final ResourceLocation ITEM_OUTPUT_GUI_BACKGROUND = HerodotusUtils.rl("textures/gui/item_output_interface.png");

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        switch (ID) {
            case 0:
                return new CreatureDataAnalyzerContainer(player, world, pos);
            case 1:
                return new ORELauncherContainer(player, world, pos);
            case 2:
                return new ItemInputInterfaceContainer(player, world, pos);
            case 3:
                return new ItemOutputInterfaceContainer(player, world, pos);
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        switch (ID) {
            case 0:
                return new CreatureDataAnalyzerGui(new CreatureDataAnalyzerContainer(player, world, pos));
            case 1:
                return new ORELauncherGui(new ORELauncherContainer(player, world, pos));
            case 2:
                return new ItemGuiContainer(new ItemInputInterfaceContainer(player, world, pos), ITEM_INPUT_GUI_BACKGROUND);
            case 3:
                return new ItemGuiContainer(new ItemOutputInterfaceContainer(player, world, pos), ITEM_OUTPUT_GUI_BACKGROUND);
            default:
                return null;
        }
    }
}
