package projecthds.herodotusutils.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;

public class WorldGenOrePillar extends WorldGenerator {
    private final IBlockState dimCrystal;
    private final IBlockState redstoneAmalgam;
    private final IBlockState metalOre;
    private final IBlockState lithiumQuartzOre;

    public WorldGenOrePillar(IBlockState dimCrystal, IBlockState redstoneAmalgam,
                             IBlockState metalOre, IBlockState lithiumQuartzOre) {
        this.dimCrystal = dimCrystal;
        this.redstoneAmalgam = redstoneAmalgam;
        this.metalOre = metalOre;
        this.lithiumQuartzOre = lithiumQuartzOre;
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        if (world.isRemote) {
            return false;
        }

        for (int y = 0; y <= 255; y++) {
            BlockPos currentPos = new BlockPos(position.getX(), y, position.getZ());

            if (y <= 4 && world.getBlockState(currentPos).getBlock() == Blocks.BEDROCK) {
                continue;
            }

            float randValue = rand.nextFloat();

            if (randValue < 0.1f) {
                world.setBlockState(currentPos, dimCrystal, 2);
            } else if (randValue < 0.4f) {
                world.setBlockState(currentPos, redstoneAmalgam, 2);
            } else if (randValue < 0.7f) {
                world.setBlockState(currentPos, metalOre, 2);
            } else {
                world.setBlockState(currentPos, lithiumQuartzOre, 2);
            }
        }
        return true;
    }
}