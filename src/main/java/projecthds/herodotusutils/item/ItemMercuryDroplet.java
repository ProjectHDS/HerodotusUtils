package projecthds.herodotusutils.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import projecthds.herodotusutils.HerodotusUtils;
import projecthds.herodotusutils.fluid.FluidMercury;

import java.util.Random;

public class ItemMercuryDroplet extends Item {
    public static final ItemMercuryDroplet INSTANCE = new ItemMercuryDroplet();

    private ItemMercuryDroplet() {
        setRegistryName("mercury_droplet");
        this.setTranslationKey(HerodotusUtils.MOD_ID + ".mercury_droplet");
        setMaxStackSize(16);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

        if (!worldIn.isRemote && entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityIn;

            if (shouldEvaporate(worldIn.rand, stack)) {
                evaporate(player, stack, itemSlot);
            }
        }
    }

    private boolean shouldEvaporate(Random rand, ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) {
            nbt = new NBTTagCompound();
            nbt.setLong("creationTime", System.currentTimeMillis());
            stack.setTagCompound(nbt);
            return false;
        }

        long creationTime = nbt.getLong("creationTime");
        long currentTime = System.currentTimeMillis();
        long ageInSeconds = (currentTime - creationTime) / 1000;

        double baseChance = 0.005 / 60.0;
        double timeMultiplier = 1.0 + (ageInSeconds / 3600.0);
        double evaporationChance = baseChance * timeMultiplier;

        return rand.nextDouble() < evaporationChance;
    }

    private void evaporate(EntityPlayer player, ItemStack stack, int slot) {
        stack.shrink(1);

        int poisonDuration = 6000 + player.world.rand.nextInt(6000);
        player.addPotionEffect(new PotionEffect(MobEffects.POISON, poisonDuration, 1));

        if (player.world.isRemote) {
            for (int i = 0; i < 5; i++) {
                double px = player.posX + (player.world.rand.nextDouble() - 0.5) * 0.5;
                double py = player.posY + 1.0 + player.world.rand.nextDouble() * 0.5;
                double pz = player.posZ + (player.world.rand.nextDouble() - 0.5) * 0.5;
                player.world.spawnParticle(net.minecraft.util.EnumParticleTypes.CLOUD, px, py, pz, 0, 0.1, 0);
            }
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);

        IFluidHandler fluidHandler = worldIn.getTileEntity(pos)
                .getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);

        if (fluidHandler != null) {
            FluidStack mercuryStack = new FluidStack(FluidMercury.INSTANCE, 50);

            int filled = fluidHandler.fill(mercuryStack, true);
            if (filled > 0) {
                if (!player.capabilities.isCreativeMode) {
                    stack.shrink(1);
                }
                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.PASS;
    }
}