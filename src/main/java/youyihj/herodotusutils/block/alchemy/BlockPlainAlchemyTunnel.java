package youyihj.herodotusutils.block.alchemy;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import youyihj.herodotusutils.alchemy.IAdjustableBlock;
import youyihj.herodotusutils.util.HorizontalBlockBoundingBoxes;
import youyihj.herodotusutils.util.Util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * @author youyihj
 */
public abstract class BlockPlainAlchemyTunnel extends AbstractPipeBlock implements IAdjustableBlock {
    public static final BlockPlainAlchemyTunnel STRAIGHT = new BlockPlainAlchemyTunnel("straight_tunnel") {
        @Override
        protected TunnelType getTunnelType() {
            return TunnelType.STRAIGHT;
        }

        @SuppressWarnings("deprecation")
        @Override
        public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
            return STRAIGHT_AABB.getBoundingBox(state.getValue(property).getInputSide());
        }
    };
    public static final BlockPlainAlchemyTunnel HORIZONTAL_RIGHT_ANGLE = new BlockPlainAlchemyTunnel("right_angle_tunnel") {
        @Override
        protected TunnelType getTunnelType() {
            return TunnelType.HORIZONTAL_RIGHT_ANGLE;
        }

        @SuppressWarnings("deprecation")
        @Override
        public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
            return RIGHT_ANGLE_SELECTION_AABB;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
            super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
            TransferDirection direction = state.getValue(property);
            addCollisionBoxToList(pos, entityBox, collidingBoxes, PART_AABB.getBoundingBox(direction.getInputSide()));
            addCollisionBoxToList(pos, entityBox, collidingBoxes, PART_AABB.getBoundingBox(direction.getOutputSide()));
        }
    };
    public static final BlockPlainAlchemyTunnel VERTICAL_RIGHT_ANGLE = new BlockPlainAlchemyTunnel("vertical_right_angle") {
        @Override
        protected TunnelType getTunnelType() {
            return TunnelType.VERTICAL_RIGHT_ANGLE;
        }

        @SuppressWarnings("deprecation")
        @Override
        public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
            return VERTICAL_SELECTION_AABB;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
            super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
            addCollisionBoxToList(pos, entityBox, collidingBoxes, PART_AABB.getBoundingBox(state.getValue(property).getOutputSide()));
        }
    };
    public static final Item STRAIGHT_ITEM = new ItemBlock(STRAIGHT).setRegistryName("straight_tunnel");
    public static final Item RIGHT_ANGLE_ITEM = new ItemBlock(HORIZONTAL_RIGHT_ANGLE).setRegistryName("right_angle_tunnel");
    public static final Item VERTICAL_ITEM = new ItemBlock(VERTICAL_RIGHT_ANGLE).setRegistryName("vertical_right_angle");

    protected static final HorizontalBlockBoundingBoxes STRAIGHT_AABB = HorizontalBlockBoundingBoxes.ofModelPos(3, 3, 0, 13, 13, 16);
    protected static final AxisAlignedBB RIGHT_ANGLE_SELECTION_AABB = Util.createAABBFromModelPos(4, 4, 4, 12, 12, 12);
    protected static final AxisAlignedBB VERTICAL_SELECTION_AABB = Util.createAABBFromModelPos(4, 4, 4, 12, 16, 12);
    protected static final HorizontalBlockBoundingBoxes PART_AABB = HorizontalBlockBoundingBoxes.ofModelPos(0, 4, 0, 4, 4, 4);
    protected IProperty<TransferDirection> property;

    protected BlockPlainAlchemyTunnel(String name) {
        super(name);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return this.blockState.getValidStates().indexOf(state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.blockState.getValidStates().get(meta);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.blockState.getBaseState().withProperty(property, getTunnelType().directionSupplierFromPlacement.get(placer.getHorizontalFacing().getOpposite(), hitX, hitZ));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        property = createProperty();
        return new BlockStateContainer(this, property);
    }

    private IProperty<TransferDirection> createProperty() {
        return PropertyEnum.create("direction", TransferDirection.class, getTunnelType());
    }

    protected abstract TunnelType getTunnelType();

    @Override
    public IBlockState getAdjustedResult(IBlockState previous) {
        TransferDirection direction = getDirection(previous);
        return this.getDefaultState().withProperty(property, Util.getCycledNextElement(getTunnelType().getAcceptedDirections(), direction));
    }

    @Nonnull
    @Override
    public AbstractPipeTileEntity createTileEntity(World world, IBlockState state) {
        return new TileAlchemyTunnel();
    }

    public TransferDirection getDirection(IBlockState state) {
        return state.getValue(this.property);
    }

    @SuppressWarnings("unused")
    public enum TransferDirection implements IStringSerializable {
        // straight
        N2S(EnumFacing.NORTH, EnumFacing.SOUTH),
        S2N(EnumFacing.SOUTH, EnumFacing.NORTH),
        W2E(EnumFacing.WEST, EnumFacing.EAST),
        E2W(EnumFacing.EAST, EnumFacing.WEST),
        // horizontal right angle
        N2W(EnumFacing.NORTH, EnumFacing.WEST),
        N2E(EnumFacing.NORTH, EnumFacing.EAST),
        S2W(EnumFacing.SOUTH, EnumFacing.WEST),
        S2E(EnumFacing.SOUTH, EnumFacing.EAST),
        W2N(EnumFacing.WEST, EnumFacing.NORTH),
        W2S(EnumFacing.WEST, EnumFacing.SOUTH),
        E2N(EnumFacing.EAST, EnumFacing.NORTH),
        E2S(EnumFacing.EAST, EnumFacing.SOUTH),
        // vertical right angle
        U2N(EnumFacing.UP, EnumFacing.NORTH),
        U2S(EnumFacing.UP, EnumFacing.SOUTH),
        U2W(EnumFacing.UP, EnumFacing.WEST),
        U2E(EnumFacing.UP, EnumFacing.EAST);

        private final EnumFacing inputSide;
        private final EnumFacing outputSide;

        TransferDirection(EnumFacing inputSide, EnumFacing outputSide) {
            this.inputSide = inputSide;
            this.outputSide = outputSide;
        }

        public static TransferDirection find(EnumFacing inputSide, EnumFacing outputSide) {
            for (TransferDirection value : values()) {
                if (value.inputSide == inputSide && value.outputSide == outputSide) {
                    return value;
                }
            }
            throw new NoSuchElementException("No such transfer direction for input: " + inputSide.name() + " output: " + outputSide.name());
        }

        public EnumFacing getInputSide() {
            return inputSide;
        }

        public EnumFacing getOutputSide() {
            return outputSide;
        }

        public boolean isStraight() {
            return inputSide.getOpposite() == outputSide;
        }

        public boolean isVerticalRightAngle() {
            return inputSide == EnumFacing.UP;
        }

        public boolean isHorizontalRightAngle() {
            return !isStraight() && !isVerticalRightAngle();
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public enum TunnelType implements com.google.common.base.Predicate<TransferDirection> {
        STRAIGHT(TransferDirection::isStraight, (mainFacing, hitX, hitZ) -> {
            switch (mainFacing) {
                case NORTH:
                    return TransferDirection.N2S;
                case SOUTH:
                    return TransferDirection.S2N;
                case WEST:
                    return TransferDirection.W2E;
                case EAST:
                    return TransferDirection.E2W;
                default:
                    throw new IllegalArgumentException(mainFacing.name());
            }
        }),
        HORIZONTAL_RIGHT_ANGLE(TransferDirection::isHorizontalRightAngle, (mainFacing, hitX, hitZ) -> {
            EnumFacing outputSide;
            if (mainFacing.getAxis() == EnumFacing.Axis.X) {
                outputSide = hitZ > 0.5f ? EnumFacing.SOUTH : EnumFacing.NORTH;
            } else {
                outputSide = hitX > 0.5f ? EnumFacing.EAST : EnumFacing.WEST;
            }
            return TransferDirection.find(mainFacing, outputSide);
        }),
        VERTICAL_RIGHT_ANGLE(TransferDirection::isVerticalRightAngle, (mainFacing, hitX, hitZ) ->
                (hitX + hitZ < 1.0f) ?
                        (hitX > hitZ) ? TransferDirection.U2N : TransferDirection.U2W
                        :
                        (hitX > hitZ) ? TransferDirection.U2E : TransferDirection.U2S
        );

        private final Predicate<TransferDirection> directionFilter;
        private final IDirectionSupplierFromPlacement directionSupplierFromPlacement;
        private final TransferDirection[] acceptedDirections;

        TunnelType(Predicate<TransferDirection> directionFilter, IDirectionSupplierFromPlacement directionSupplierFromPlacement) {
            this.directionFilter = directionFilter;
            this.directionSupplierFromPlacement = directionSupplierFromPlacement;
            this.acceptedDirections = Arrays.stream(TransferDirection.values()).filter(directionFilter).toArray(TransferDirection[]::new);
        }

        public TransferDirection[] getAcceptedDirections() {
            return acceptedDirections;
        }

        @Override
        public boolean apply(@Nullable TransferDirection input) {
            return directionFilter.test(input);
        }
    }

    @FunctionalInterface
    private interface IDirectionSupplierFromPlacement {
        TransferDirection get(EnumFacing mainFacing, float hitX, float hitZ);
    }
}
