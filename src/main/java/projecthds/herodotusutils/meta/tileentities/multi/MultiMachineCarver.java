package projecthds.herodotusutils.meta.tileentities.multi;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import gregtech.api.capability.impl.ItemHandlerList;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.PatternMatchContext;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.api.util.BlockInfo;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import io.sommers.packmode.api.PackModeAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import projecthds.herodotusutils.item.ItemCarverUpgrade;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MultiMachineCarver extends MultiblockControllerBase {

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

    private static final List<UnificationEntry> WOOD_PARTS = new ArrayList<>();

    protected IItemHandlerModifiable chest, dropper;
    private int fuel = 0;
    private int progress = 0;
    private boolean working = false;

    private static final int WORK_TIME = 100; // 20t*5s
    private static final int DETECT_TICK = 60; // per 3s work
    private static final int FUEL_PER_OPERATION = 100;

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
        if (getWorld().isRemote || !isStructureFormed()) return;

        // fuel check
        refuelFromChest();

        // work before
        if (!working && fuel >= FUEL_PER_OPERATION) {
            fuel -= FUEL_PER_OPERATION;
            progress = 0;
            working = true;
        }

        if (working) {
            progress++;

            if (hasSawdust(dropper)) {
                clearInventory(dropper);
                working = false;
                return;
            }

            // work step
            if (progress == DETECT_TICK) {
                // update check
                boolean updated = checkUpdate();
                if (updated) {
                    List<UnificationEntry> missing = getAutoCompleteEntries(dropper);
                    if (dropper.getStackInSlot(4).getItem() instanceof ItemBlock itemBlock
                            && itemBlock.getBlock() == Blocks.PLANKS && dropper.getStackInSlot(4).getCount() >= missing.size()) {
                        dropper.getStackInSlot(4).shrink(missing.size());
                        for (UnificationEntry req : missing) {
                            for (int i = 0; i < dropper.getSlots(); i++) {
                                if (i==4) continue;
                                if (dropper.getStackInSlot(i) != ItemStack.EMPTY) continue;
                                ItemStack part = OreDictUnifier.get(req);
                                dropper.setStackInSlot(i, part);
                                break;
                            }
                        }
                        progress = 0;
                    }
                }

                if (hasAllWoodParts(dropper)) {
                    for (UnificationEntry req : WOOD_PARTS) {
                        if (OreDictUnifier.hasOreDictionary(dropper.getStackInSlot(4), req.toString())) {
                            transform(dropper, isExpertMode());
                            progress = 0; // reset progress after transform
                            break;
                        }
                    }
                }
            }

            if (progress >= WORK_TIME) {
                working = false;
                progress = 0;
            }
        }
    }

    @Override
    public boolean onRightClick(EntityPlayer playerIn, EnumHand hand, EnumFacing facing, CuboidRayTraceResult hitResult) {
        if (super.onRightClick(playerIn, hand, facing, hitResult)) {
            return true;
        } else if (hand == EnumHand.MAIN_HAND && playerIn.isSneaking() && !getWorld().isRemote) {
            playerIn.sendMessage(new TextComponentTranslation("hdsutils.carver.fuel", fuel));
            return true;
        } else {
            return false;
        }
    }

    private void refuelFromChest() {
        List<ItemStack> inputs = GTUtility.itemHandlerToList(chest);
        if (inputs.isEmpty()) return;

        for (int i = 0; i < inputs.size(); i++) {
            ItemStack stack = inputs.get(i);
            if (!stack.isEmpty() && TileEntityFurnace.isItemFuel(stack)) {
                int burn = TileEntityFurnace.getItemBurnTime(stack);
                fuel += burn * stack.getCount();
                if (stack.getItem() == Items.LAVA_BUCKET) {
                    chest.setStackInSlot(i, new ItemStack(Items.BUCKET, stack.getCount()));
                } else {
                    stack.shrink(stack.getCount());
                }
            }
        }
    }

    private boolean checkUpdate() {
        List<ItemStack> inputs = GTUtility.itemHandlerToList(chest);
        if (inputs.isEmpty()) return false;
        for (ItemStack stack : inputs) if (!stack.isEmpty() && stack.getItem() == ItemCarverUpgrade.INSTANCE) return true;
        return false;
    }

    private List<UnificationEntry> getAutoCompleteEntries(IItemHandlerModifiable inv) {
        List<UnificationEntry> result = new ArrayList<>();
        for (UnificationEntry req : WOOD_PARTS) {
            boolean found = false;
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack slot = inv.getStackInSlot(i);
                if (slot.isEmpty()) continue;
                if (OreDictUnifier.hasOreDictionary(slot, req.toString())) {
                    found = true;
                    break;
                }
            }
            if (!found) result.add(req);
        }
        return result;
    }

    private boolean hasSawdust(IItemHandlerModifiable inv) {
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && OreDictUnifier.get(OrePrefix.dust, Materials.Wood).isItemEqual(stack)) {
                return true;
            }
        }
        return false;
    }

    private void clearInventory(IItemHandlerModifiable inv) {
        for (int i = 0; i < inv.getSlots(); i++) {
            inv.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    static {
        WOOD_PARTS.add(new UnificationEntry(OrePrefix.plate, Materials.Wood));
        WOOD_PARTS.add(new UnificationEntry(OrePrefix.gear, Materials.Wood));
        WOOD_PARTS.add(new UnificationEntry(OrePrefix.ingot, Materials.Wood));
        WOOD_PARTS.add(new UnificationEntry(OrePrefix.gearSmall, Materials.Wood));
        WOOD_PARTS.add(new UnificationEntry(OrePrefix.ring, Materials.Wood));
        WOOD_PARTS.add(new UnificationEntry(OrePrefix.stickLong, Materials.Wood));
        WOOD_PARTS.add(new UnificationEntry(OrePrefix.stick, Materials.Wood));
        WOOD_PARTS.add(new UnificationEntry(OrePrefix.screw, Materials.Wood));
    }

    private boolean hasAllWoodParts(IItemHandlerModifiable inv) {
        for (UnificationEntry req : WOOD_PARTS) {
            ItemStack unified = OreDictUnifier.get(req);
            if (unified.isEmpty()) {
                continue;
            }

            boolean found = false;

            for (int i = 0; i < inv.getSlots(); i++) {
                if (i == 4) continue; //skip center check
                ItemStack slot = inv.getStackInSlot(i);
                if (!slot.isEmpty()) {
                    if (OreDictUnifier.hasOreDictionary(slot, req.toString())) {
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                return false;
            }
        }
        return true;
    }

    private boolean isExpertMode() {
        return PackModeAPI.getInstance().getCurrentPackMode().equalsIgnoreCase("expert");
    }

    private void transform(IItemHandlerModifiable inv, boolean random) {
        int center = 4;
        ItemStack stack = inv.getStackInSlot(center);
        inv.setStackInSlot(center, random ? getRandomStage(stack) : getNextStage(stack));
    }

    private ItemStack getNextStage(ItemStack stack) {
        UnificationEntry ex = WOOD_PARTS.stream()
                .filter(req -> OreDictUnifier.hasOreDictionary(stack, req.toString()))
                .findFirst()
                .orElse(null);
        UnificationEntry entry = WOOD_PARTS.get((WOOD_PARTS.indexOf(ex) + 1) % WOOD_PARTS.size());
        return OreDictUnifier.get(entry.orePrefix, entry.material, stack.getCount());
    }

    private ItemStack getRandomStage(ItemStack stack) {
        UnificationEntry entry = WOOD_PARTS.get(getWorld().rand.nextInt(WOOD_PARTS.size()));
        return OreDictUnifier.get(entry.orePrefix, entry.material, stack.getCount());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setInteger("fuel", fuel);
        data.setInteger("progress", progress);
        data.setBoolean("working", working);
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        fuel = data.getInteger("fuel");
        progress = data.getInteger("progress");
        working = data.getBoolean("working");
    }

    @Override
    protected @NotNull BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXX")
                .aisle("XXX")
                .aisle("CSD")
                .where('S', selfPredicate())
                .where('X', any())
                .where('C', CHEST.get())
                .where('D', DROPPER.get())
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
}
