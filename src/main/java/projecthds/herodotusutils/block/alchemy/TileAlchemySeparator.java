package projecthds.herodotusutils.block.alchemy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import projecthds.herodotusutils.alchemy.AlchemyFluid;
import projecthds.herodotusutils.alchemy.AlchemyModuleCallback;
import projecthds.herodotusutils.alchemy.IAlchemyModule;
import projecthds.herodotusutils.alchemy.IHasAlchemyFluid;
import projecthds.herodotusutils.util.Util;

/**
 * @author youyihj
 */
public class TileAlchemySeparator extends AbstractHasAlchemyFluidTileEntity implements IAlchemyModule {
    @Override
    public void work() {
        if (content == null) return;
        EnumFacing[] tankFacings = new EnumFacing[4];
        for (int i = 0; i < 4; i++) {
            for (EnumFacing enumFacing : EnumFacing.Plane.HORIZONTAL) {
                switch (checkNearbyTank(i, enumFacing)) {
                    case SUCCESS:
                        tankFacings[i] = enumFacing;
                        break;
                    case FILLED_TANK:
                        return;
                }
            }
        }
        int validTanks = 0;
        for (EnumFacing tankFacing : tankFacings) {
            if (tankFacing != null) {
                validTanks++;
            } else break;
        }
        if (validTanks == 0) return;
        AlchemyFluid[] outputs = content.separate(validTanks);
        AlchemyModuleCallback callback = new AlchemyModuleCallback(this);
        int flags = 0;
        for (int i = 0; i < outputs.length; i++) {
            EnumFacing facing = tankFacings[i];
            TileAlchemySeparatorTank tank = Util.getTileEntity(world, pos.offset(facing), TileAlchemySeparatorTank.class).orElse(null);
            if (tank != null) {
                if (tank.getContainedFluid() != null) {
                    flags |= (1 << i);
                    tank.setEmptyCallback(callback);
                }
            }
        }
        if (flags == 0) {
            for (int i = 0; i < outputs.length; i++) {
                EnumFacing facing = tankFacings[i];
                TileAlchemySeparatorTank tank = Util.getTileEntity(world, pos.offset(facing), TileAlchemySeparatorTank.class).orElse(null);
                if (tank != null) {
                    tank.handleInput(outputs[i], null);
                }
            }
            emptyFluid();
        }
    }


    @Override
    public EnumFacing inputSide() {
        return EnumFacing.UP;
    }

    @Override
    public EnumFacing outputSide() {
        return null;
    }

    private TankCheckResult checkNearbyTank(int ordinal, EnumFacing facing) {
        BlockPos offset = pos.offset(facing);
        IBlockState blockState = world.getBlockState(offset);
        if (blockState.getBlock() != BlockAlchemySeparatorTank.INSTANCE) {
            return TankCheckResult.NOT_A_TANK;
        } else if (blockState.getValue(BlockAlchemySeparatorTank.NUMBER) != ordinal + 1) {
            return TankCheckResult.NOT_MATCHING_ORDINAL;
        } else {
            IHasAlchemyFluid hasAlchemyFluid = Util.getTileEntity(world, offset, IHasAlchemyFluid.class).orElseThrow(RuntimeException::new);
            if (hasAlchemyFluid.getContainedFluid() != null) {
                return TankCheckResult.FILLED_TANK;
            }
        }
        return TankCheckResult.SUCCESS;
    }

    private enum TankCheckResult {
        SUCCESS,
        NOT_A_TANK,
        FILLED_TANK,
        NOT_MATCHING_ORDINAL
    }
}
