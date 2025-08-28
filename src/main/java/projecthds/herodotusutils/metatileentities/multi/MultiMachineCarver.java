package projecthds.herodotusutils.metatileentities.multi;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.*;
import gregtech.api.metatileentity.multiblock.ui.MultiblockUIBuilder;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.util.KeyUtil;
import gregtech.client.renderer.ICubeRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import projecthds.herodotusutils.util.TransferUtils;

public class MultiMachineCarver extends MultiblockWithDisplayBase {
    private int burnTime = 0;
    private int progress = 0;
    private static final int ONCE_TIME = 60;
    private static final int MAX_PROGRESS = 100;

    public MultiMachineCarver(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
    }

    @Override
    public void update() {
        super.update();
        if (!getWorld().isRemote && isActive()) {
            IItemHandler chestInventory = getWorld().getTileEntity(getPos().up()).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            TransferUtils.forEachItems(chestInventory, (index, stack) -> {
                if (TileEntityFurnace.getItemBurnTime(stack) > 0) {

                }
            });
        }
    }

    @Override
    @SideOnly(value = Side.CLIENT)
    protected void configureDisplayText(MultiblockUIBuilder builder) {
        builder.addCustom((manager, syncer) -> {
            manager.add(KeyUtil.lang("hdsutils.mte.carver.burn_time", burnTime));
        });
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setInteger("burnTime", burnTime);
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        burnTime = data.getInteger("burnTime");
    }

    @Override
    protected void updateFormedValid() {}

    @Override
    protected @NotNull BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("A")
                .aisle("B")
                .aisle("C")
                .where('A', blocks(Blocks.CHEST))
                .where('B', selfPredicate())
                .where('C', blocks(Blocks.DROPPER))
                .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return null;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MultiMachineCarver(metaTileEntityId);
    }

    @Override
    public boolean hasMaintenanceMechanics() {
        return false;
    }
}
