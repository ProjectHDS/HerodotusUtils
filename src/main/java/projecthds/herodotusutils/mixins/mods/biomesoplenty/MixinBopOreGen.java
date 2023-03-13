package projecthds.herodotusutils.mixins.mods.biomesoplenty;

import biomesoplenty.common.init.ModGenerators;
import biomesoplenty.common.world.GeneratorRegistry;
import biomesoplenty.common.world.generator.*;
import biomesoplenty.common.world.generator.tree.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ModGenerators.class)
public class MixinBopOreGen {
    /**
     * @author Gary Bryson Luis Jr.
     * @reason This is stupid but BOP is also stupid as hell.
     * */
    @Overwrite(remap = false)
    public static void init() {
        GeneratorRegistry.registerGenerator("weighted", GeneratorWeighted.class, new GeneratorWeighted.Builder());
        GeneratorRegistry.registerGenerator("basic_tree", GeneratorBasicTree.class, new GeneratorBasicTree.Builder());
        GeneratorRegistry.registerGenerator("big_tree", GeneratorBigTree.class, new GeneratorBigTree.Builder());
        GeneratorRegistry.registerGenerator("bush", GeneratorBush.class, new GeneratorBush.Builder());
        GeneratorRegistry.registerGenerator("twiglet_tree", GeneratorTwigletTree.class, new GeneratorTwigletTree.Builder());
        GeneratorRegistry.registerGenerator("pine_tree", GeneratorPineTree.class, new GeneratorPineTree.Builder());
        GeneratorRegistry.registerGenerator("bulb_tree", GeneratorBulbTree.class, new GeneratorBulbTree.Builder());
        GeneratorRegistry.registerGenerator("mega_jungle_tree", GeneratorMegaJungleTree.class, new GeneratorMegaJungleTree.Builder());
        GeneratorRegistry.registerGenerator("bayou_tree", GeneratorBayouTree.class, new GeneratorBayouTree.Builder());
        GeneratorRegistry.registerGenerator("mangrove_tree", GeneratorMangroveTree.class, new GeneratorMangroveTree.Builder());
        GeneratorRegistry.registerGenerator("taiga_tree", GeneratorTaigaTree.class, new GeneratorTaigaTree.Builder());
        GeneratorRegistry.registerGenerator("profile_tree", GeneratorProfileTree.class, new GeneratorProfileTree.Builder());
        GeneratorRegistry.registerGenerator("flora", GeneratorFlora.class, new GeneratorFlora.Builder());
        GeneratorRegistry.registerGenerator("double_flora", GeneratorDoubleFlora.class, new GeneratorDoubleFlora.Builder());
        GeneratorRegistry.registerGenerator("grass", GeneratorGrass.class, new GeneratorGrass.Builder());
        GeneratorRegistry.registerGenerator("logs", GeneratorLogs.class, new GeneratorLogs.Builder());
        GeneratorRegistry.registerGenerator("big_mushrooms", GeneratorBigMushroom.class, new GeneratorBigMushroom.Builder());
        GeneratorRegistry.registerGenerator("big_flowers", GeneratorBigFlower.class, new GeneratorBigFlower.Builder());
        GeneratorRegistry.registerGenerator("waterside", GeneratorWaterside.class, new GeneratorWaterside.Builder());
        GeneratorRegistry.registerGenerator("splatter", GeneratorSplatter.class, new GeneratorSplatter.Builder());
        GeneratorRegistry.registerGenerator("splotches", GeneratorSplotches.class, new GeneratorSplotches.Builder());
        GeneratorRegistry.registerGenerator("blobs", GeneratorBlobs.class, new GeneratorBlobs.Builder());
        GeneratorRegistry.registerGenerator("lakes", GeneratorLakes.class, new GeneratorLakes.Builder());
        GeneratorRegistry.registerGenerator("columns", GeneratorColumns.class, new GeneratorColumns.Builder());
        GeneratorRegistry.registerGenerator("bramble", GeneratorBramble.class, new GeneratorBramble.Builder());
        GeneratorRegistry.registerGenerator("vines", GeneratorVines.class, new GeneratorVines.Builder());
        GeneratorRegistry.registerGenerator("mixed_lily", GeneratorMixedLily.class, new GeneratorMixedLily.Builder());
        GeneratorRegistry.registerGenerator("crystals", GeneratorCrystals.class, new GeneratorCrystals.Builder());
        GeneratorRegistry.registerGenerator("spike", GeneratorSpike.class, new GeneratorSpike.Builder());
        GeneratorRegistry.registerGenerator("hive", GeneratorHive.class, new GeneratorHive.Builder());
        GeneratorRegistry.registerGenerator("redwood_tree", GeneratorRedwoodTree.class, new GeneratorRedwoodTree.Builder());
        GeneratorRegistry.registerGenerator("redwood_tree_thin", GeneratorRedwoodTreeThin.class, new GeneratorRedwoodTreeThin.Builder());
        GeneratorRegistry.registerGenerator("mahogany_tree", GeneratorMahoganyTree.class, new GeneratorMahoganyTree.Builder());
        GeneratorRegistry.registerGenerator("palm_tree", GeneratorPalmTree.class, new GeneratorPalmTree.Builder());

    }
}

