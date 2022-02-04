package youyihj.herodotusutils.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import youyihj.herodotusutils.HerodotusUtils;
import youyihj.herodotusutils.computing.ComputingUnitCapabilityProvider;
import youyihj.herodotusutils.computing.IComputingUnit;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author youyihj
 */
@Mod.EventBusSubscriber
public class Capabilities {
    private static final ResourceLocation COMPUTING_UNIT_RL = HerodotusUtils.rl("computing_unit");
    private static final ResourceLocation TAINT_RL = HerodotusUtils.rl("taint");

    @CapabilityInject(IComputingUnit.class)
    public static Capability<IComputingUnit> COMPUTING_UNIT_CAPABILITY = null;

    @CapabilityInject(ITaint.class)
    public static Capability<ITaint> TAINT_CAPABILITY = null;

    @SubscribeEvent
    public static void attachToChunk(AttachCapabilitiesEvent<Chunk> event) {
        Objects.requireNonNull(COMPUTING_UNIT_CAPABILITY);
        event.addCapability(COMPUTING_UNIT_RL, new ComputingUnitCapabilityProvider());
    }

    @SubscribeEvent
    public static void attachToPlayer(AttachCapabilitiesEvent<Entity> event) {
        Entity object = event.getObject();
        if (object instanceof EntityPlayer) {
            Objects.requireNonNull(TAINT_CAPABILITY);
            event.addCapability(TAINT_RL, new TaintCapabilityProvider(((EntityPlayer) object)));
        }
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(IComputingUnit.class, getInvalidStorage(), IComputingUnit.Impl::new);
        CapabilityManager.INSTANCE.register(ITaint.class, getInvalidStorage(), ITaint.Impl::new);
    }

    @SuppressWarnings("unchecked")
    public static <T> Capability.IStorage<T> getInvalidStorage() {
        return ((Capability.IStorage<T>) new InvalidStorage());
    }

    private static class InvalidStorage implements Capability.IStorage<Object> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<Object> capability, Object instance, EnumFacing side) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void readNBT(Capability<Object> capability, Object instance, EnumFacing side, NBTBase nbt) {
            throw new UnsupportedOperationException();
        }
    }
}
