package youyihj.herodotusutils.block.organism;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import thaumcraft.api.aspects.Aspect;
import thecodex6824.thaumicaugmentation.ThaumicAugmentation;
import thecodex6824.thaumicaugmentation.api.impetus.node.CapabilityImpetusNode;
import thecodex6824.thaumicaugmentation.api.impetus.node.ConsumeResult;
import thecodex6824.thaumicaugmentation.api.impetus.node.IImpetusConsumer;
import thecodex6824.thaumicaugmentation.api.impetus.node.NodeHelper;
import thecodex6824.thaumicaugmentation.api.impetus.node.prefab.ImpetusNode;
import thecodex6824.thaumicaugmentation.api.impetus.node.prefab.SimpleImpetusConsumer;
import thecodex6824.thaumicaugmentation.api.util.DimensionalBlockPos;
import youyihj.herodotusutils.organism.IInputInterface;
import youyihj.herodotusutils.organism.IngredientType;
import youyihj.herodotusutils.util.Capabilities;

import javax.annotation.Nullable;

/**
 * @author youyihj
 */
public class TileImpetusInputInterface extends TileEntity implements ITickable {
    public static final int CAPACITY = 500;
    private EnumFacing facing;
    private int impetus;

    private final ImpetusNode node = new SimpleImpetusConsumer(1, 0) {
        @Override
        public Vec3d getBeamEndpoint() {
            Vec3d beamEndpoint = super.getBeamEndpoint();
            return beamEndpoint.add(new Vec3d(facing.getDirectionVec()).scale(0.5));
        }
    };

    private final IInputInterface<Integer> inputInterface = new IInputInterface<Integer>() {
        @Override
        public IngredientType<Integer> getIngredientType() {
            return IngredientType.IMPETUS;
        }

        @Override
        public boolean checkCount(Integer ingredient) {
            return impetus >= ingredient;
        }

        @Override
        public void extractIngredient(Integer ingredient) {
            impetus -= ingredient;
            markDirty();
        }
    };

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.impetus = compound.getInteger("impetus");
        node.deserializeNBT(compound.getCompoundTag("node"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("impetus", impetus);
        compound.setTag("node", node.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityImpetusNode.IMPETUS_NODE ||
                capability == Capabilities.INPUT_INTERFACE_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityImpetusNode.IMPETUS_NODE) {
            return CapabilityImpetusNode.IMPETUS_NODE.cast(this.node);
        }
        if (capability == Capabilities.INPUT_INTERFACE_CAPABILITY) {
            return Capabilities.INPUT_INTERFACE_CAPABILITY.cast(inputInterface);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidate() {
        if (!this.world.isRemote) {
            NodeHelper.syncDestroyedImpetusNode(this.node);
        }

        this.node.destroy();
        ThaumicAugmentation.proxy.deregisterRenderableImpetusNode(this.node);
        super.invalidate();
    }

    @Override
    public void setWorld(World worldIn) {
        super.setWorld(worldIn);
        node.setLocation(new DimensionalBlockPos(pos.toImmutable(), worldIn.provider.getDimension()));
    }

    @Override
    public void setPos(BlockPos posIn) {
        super.setPos(posIn);
        if (world != null) {
            node.setLocation(new DimensionalBlockPos(posIn.toImmutable(), world.provider.getDimension()));
        }
    }

    @Override
    public void onLoad() {
        node.init(world);
        facing = world.getBlockState(pos).getValue(BlockHorizontal.FACING);
        ThaumicAugmentation.proxy.registerRenderableImpetusNode(node);
    }

    @Override
    public void onChunkUnload() {
        node.unload();
        ThaumicAugmentation.proxy.deregisterRenderableImpetusNode(node);
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        ThaumicAugmentation.proxy.getRenderHelper().renderSpark(world, pos.getX() + world.rand.nextFloat(),
                pos.getY() + world.rand.nextFloat(), pos.getZ() + world.rand.nextFloat(), 1.5F, Aspect.ELDRITCH.getColor(), false);

        return true;
    }

    @Override
    public void update() {
        if (world.isRemote || impetus >= CAPACITY)
            return;
        ConsumeResult result = ((IImpetusConsumer) node).consume(CAPACITY - impetus, false);
        if (result.energyConsumed > 0) {
            impetus += result.energyConsumed;
            this.markDirty();
        }
    }
}
