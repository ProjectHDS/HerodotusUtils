package projecthds.herodotusutils.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import projecthds.herodotusutils.block.dimcrystal.BlockPlainDimCrystal;

import java.util.Set;

public class OrePillarGenerator {
    private final IBlockState dimCrystal;
    private final IBlockState redstoneAmalgam;
    private final IBlockState copperOre;
    private final IBlockState ironOre;
    private final IBlockState tinOre;
    private final IBlockState leadOre;
    private final IBlockState lithiumQuartzOre;

    public OrePillarGenerator(IBlockState dimCrystal, IBlockState redstoneAmalgam,
                              IBlockState copperOre, IBlockState ironOre,
                              IBlockState tinOre, IBlockState leadOre,
                              IBlockState lithiumQuartzOre) {
        this.dimCrystal = dimCrystal;
        this.redstoneAmalgam = redstoneAmalgam;
        this.copperOre = copperOre;
        this.ironOre = ironOre;
        this.tinOre = tinOre;
        this.leadOre = leadOre;
        this.lithiumQuartzOre = lithiumQuartzOre;
    }

    public void generatePillar(World world, int chunkX, int chunkZ) {
        if (world.provider.getDimension() != 0) {
            return;
        }

        if (world.isRemote) {
            return;
        }

        BlockPos centerPos = new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8);
        Biome biome = world.getBiome(centerPos);

        Set<BiomeDictionary.Type> biomeTypes = BiomeDictionary.getTypes(biome);

        boolean isTargetBiome = false;

        if (biomeTypes.contains(BiomeDictionary.Type.SANDY) ||
                biomeTypes.contains(BiomeDictionary.Type.DRY) ||
                biomeTypes.contains(BiomeDictionary.Type.HOT)) {
            isTargetBiome = true;
        }

        if (!isTargetBiome && (biomeTypes.contains(BiomeDictionary.Type.PLAINS) ||
                biomeTypes.contains(BiomeDictionary.Type.SAVANNA))) {
            isTargetBiome = true;
        }

        if (!isTargetBiome && (biomeTypes.contains(BiomeDictionary.Type.SWAMP) ||
                biomeTypes.contains(BiomeDictionary.Type.WET) ||
                biomeTypes.contains(BiomeDictionary.Type.RIVER))) {
            isTargetBiome = true;
        }

        if (!isTargetBiome && (biomeTypes.contains(BiomeDictionary.Type.FOREST) ||
                biomeTypes.contains(BiomeDictionary.Type.CONIFEROUS) ||
                biomeTypes.contains(BiomeDictionary.Type.JUNGLE))) {
            isTargetBiome = true;
        }

        if (!isTargetBiome) {
            return;
        }

        OrePillarSavedData savedData = OrePillarSavedData.get(world);

        String biomeIdentifier = biome.getRegistryName().toString();

        if (savedData.hasGeneratedBiome(biomeIdentifier)) {
            return;
        }

        savedData.markBiomeGenerated(biomeIdentifier);

        IBlockState metalOre = getMetalOreForBiome(biome);

        WorldGenOrePillar orePillarGenerator = new WorldGenOrePillar(
                dimCrystal, redstoneAmalgam, metalOre, lithiumQuartzOre
        );

        BlockPos pillarPos = centerPos;
        orePillarGenerator.generate(world, world.rand, pillarPos);
    }

    private IBlockState getMetalOreForBiome(Biome biome) {
        Set<BiomeDictionary.Type> biomeTypes = BiomeDictionary.getTypes(biome);

        if (biomeTypes.contains(BiomeDictionary.Type.SANDY) ||
                biomeTypes.contains(BiomeDictionary.Type.DRY) ||
                biomeTypes.contains(BiomeDictionary.Type.HOT)) {
            return copperOre;
        }

        if (biomeTypes.contains(BiomeDictionary.Type.PLAINS) ||
                biomeTypes.contains(BiomeDictionary.Type.SAVANNA)) {
            return ironOre;
        }

        if (biomeTypes.contains(BiomeDictionary.Type.SWAMP) ||
                biomeTypes.contains(BiomeDictionary.Type.WET) ||
                biomeTypes.contains(BiomeDictionary.Type.RIVER)) {
            return tinOre;
        }

        if (biomeTypes.contains(BiomeDictionary.Type.FOREST) ||
                biomeTypes.contains(BiomeDictionary.Type.CONIFEROUS) ||
                biomeTypes.contains(BiomeDictionary.Type.JUNGLE)) {
            return leadOre;
        }

        return BlockPlainDimCrystal.INSTANCE.getDefaultState();
    }
}