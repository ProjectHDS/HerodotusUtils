package projecthds.herodotusutils.meta.tileentities.multi;

import com.cleanroommc.modularui.api.drawable.IKey;
import gregtech.api.capability.impl.ItemHandlerList;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockWithDisplayBase;
import gregtech.api.metatileentity.multiblock.ui.MultiblockUIBuilder;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.PatternMatchContext;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.util.BlockInfo;
import gregtech.api.util.GTUtility;
import gregtech.api.util.KeyUtil;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MultiMachineCarver extends MultiblockWithDisplayBase {

    private static final Supplier<TraceabilityPredicate> CHEST = () -> new TraceabilityPredicate(blockWorldState -> {
        if (blockWorldState.getBlockState().getBlock() == Blocks.CHEST &&
                blockWorldState.getTileEntity() instanceof TileEntityChest chest) {
            blockWorldState.getMatchContext().getOrCreate("ImportChest", () -> {
                var chests = new LinkedList<TileEntityChest>();
                chests.add(chest);
                return chests;
            });
            return true;
        }
        else return false;
    }, () -> new BlockInfo[]{new BlockInfo(Blocks.CHEST)});

    private static final Supplier<TraceabilityPredicate> DROPPER = () -> new TraceabilityPredicate(blockWorldState -> {
        if (blockWorldState.getBlockState().getBlock() == Blocks.DROPPER &&
        blockWorldState.getTileEntity() instanceof TileEntityDropper dropper) {
            blockWorldState.getMatchContext().getOrCreate("ExportDropper", () -> {
                var droppers = new LinkedList<TileEntityDropper>();
                droppers.add(dropper);
                return droppers;
            });
            return true;
        }
        else return false;
    }, () -> new BlockInfo[]{new BlockInfo(Blocks.DROPPER)});

    protected IItemHandlerModifiable chest, dropper;
    private int fuel = 0;

    public MultiMachineCarver(@NotNull ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
    }

    @Override
    protected void updateFormedValid() {}

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);

        List<TileEntityChest> chests = context.getOrDefault("ImportChest", Collections.emptyList());
        this.chest = new ItemHandlerList(chests.stream().map(it -> it.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).collect(Collectors.toList()));

        List<TileEntityDropper> droppers = context.getOrDefault("ExportDropper", Collections.emptyList());
        this.dropper = new ItemHandlerList(droppers.stream().map(it -> it.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).collect(Collectors.toList()));
    }

    @Override
    public void update() {
        super.update();
        if (!getWorld().isRemote && isStructureFormed()) {
            List<ItemStack> inputs = GTUtility.itemHandlerToList(chest);
            if (!inputs.isEmpty()) {
                for (ItemStack stack : inputs) {
                    if (TileEntityFurnace.isItemFuel(stack)) {
                        this.fuel += TileEntityFurnace.getItemBurnTime(stack) * stack.getCount();
                        stack.shrink(stack.getCount());
                    }
                }
            }
        }
    }

    private boolean isWorkingEnabled() {
        return this.fuel >= 100;
    }

    @Override
    protected void configureDisplayText(MultiblockUIBuilder builder) {
        builder.setWorkingStatus(isWorkingEnabled(), isActive())
                .addWorkingStatusLine()
                .addCustom((manager, syncer) -> {
                    if (isStructureFormed()) {
                        int fuelSync = syncer.syncInt(this.fuel);
                        IKey fuelKey = KeyUtil.number(TextFormatting.AQUA,
                                fuelSync, " tick");
                        manager.add(KeyUtil.lang(TextFormatting.GRAY,
                                "hdsutils.carver.fuel", fuelKey));
                    }
                });
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setInteger("fuel", fuel);
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        fuel = data.getInteger("fuel");
    }

    @Override
    protected @NotNull BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("ABC")
                .where('A', blocks(Blocks.DIAMOND_BLOCK))
                .where('B', selfPredicate())
                .where('C', blocks(Blocks.DROPPER))
                .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return Textures.WOOD_WALL;
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
