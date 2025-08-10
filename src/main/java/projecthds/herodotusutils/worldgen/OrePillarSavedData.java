package projecthds.herodotusutils.worldgen;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.HashSet;
import java.util.Set;

public class OrePillarSavedData extends WorldSavedData {
    private static final String DATA_NAME = "OrePillarGeneratedBiomes";
    private final Set<String> generatedBiomes = new HashSet<>();

    public OrePillarSavedData() {
        super(DATA_NAME);
    }

    public static OrePillarSavedData get(World world) {
        OrePillarSavedData data = (OrePillarSavedData) world.getPerWorldStorage().getOrLoadData(OrePillarSavedData.class, DATA_NAME);

        if (data == null) {
            data = new OrePillarSavedData();
            world.getPerWorldStorage().setData(DATA_NAME, data);
        }

        return data;
    }

    public boolean hasGeneratedBiome(String biomeName) {
        return generatedBiomes.contains(biomeName);
    }

    public void markBiomeGenerated(String biomeName) {
        if (!generatedBiomes.contains(biomeName)) {
            generatedBiomes.add(biomeName);
            markDirty();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        generatedBiomes.clear();
        NBTTagList biomeList = nbt.getTagList("GeneratedBiomes", Constants.NBT.TAG_STRING);

        for (int i = 0; i < biomeList.tagCount(); i++) {
            generatedBiomes.add(biomeList.getStringTagAt(i));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList biomeList = new NBTTagList();

        for (String biomeName : generatedBiomes) {
            biomeList.appendTag(new NBTTagString(biomeName));
        }

        compound.setTag("GeneratedBiomes", biomeList);
        return compound;
    }
}