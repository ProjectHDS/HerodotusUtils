package projecthds.herodotusutils.modsupport.theoneprobe;

import mcjty.theoneprobe.api.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.items.CapabilityItemHandler;
import projecthds.herodotusutils.HerodotusUtils;
import projecthds.herodotusutils.block.computing.TileComputingModule;
import projecthds.herodotusutils.util.Capabilities;
import projecthds.herodotusutils.alchemy.AlchemyFluid;
import projecthds.herodotusutils.alchemy.IHasAlchemyFluid;
import projecthds.herodotusutils.computing.IComputingUnit;
import projecthds.herodotusutils.computing.IComputingUnitConsumer;
import projecthds.herodotusutils.computing.IComputingUnitGenerator;
import projecthds.herodotusutils.computing.IComputingUnitInteract;
import projecthds.herodotusutils.computing.event.ComputingUnitChangeEvent;

/**
 * @author youyihj
 */
public enum TOPInfoProvider implements IProbeInfoProvider {
    INSTANCE;

    @Override
    public String getID() {
        return HerodotusUtils.MOD_ID;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tileEntity = world.getTileEntity(data.getPos());
        if (tileEntity instanceof IComputingUnitInteract) {
            Chunk chunk = world.getChunk(data.getPos());
            IComputingUnit computingUnit = chunk.getCapability(Capabilities.COMPUTING_UNIT_CAPABILITY, null);
            new ComputingUnitChangeEvent(computingUnit, chunk).post();
            probeInfo.element(new ElementTextComponent(TextStyleClass.INFO, new TextComponentTranslation("hdsutils.computing_unit.bar", computingUnit.totalConsumePower(), computingUnit.totalGeneratePower())));
        }
        if (tileEntity instanceof IComputingUnitGenerator) {
            probeInfo.element(new ElementTextComponent(TextStyleClass.INFO, new TextComponentTranslation("hdsutils.computing_unit.generate", ((IComputingUnitGenerator) tileEntity).generateAmount())));
        }
        if (tileEntity instanceof IComputingUnitConsumer) {
            probeInfo.element(new ElementTextComponent(TextStyleClass.INFO, new TextComponentTranslation("hdsutils.computing_unit.consume", ((IComputingUnitConsumer) tileEntity).consumeAmount())));
        }
        if (tileEntity instanceof TileComputingModule) {
            ItemStack itemStack = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH).getStackInSlot(0).copy();
            if (!itemStack.isEmpty()) {
                probeInfo.element(new ElementItemWithName(itemStack));
            }
        }
        if (tileEntity instanceof IHasAlchemyFluid) {
            AlchemyFluid containedFluid = ((IHasAlchemyFluid) tileEntity).getContainedFluid();
            if (containedFluid != null) {
                probeInfo.element(new ElementTextComponent(TextStyleClass.NAME, new TextComponentTranslation("hdsutils.alchemy.fluid_info").appendSibling(new TextComponentString(containedFluid.getDisplayName()))));
            }
        }
    }
}
