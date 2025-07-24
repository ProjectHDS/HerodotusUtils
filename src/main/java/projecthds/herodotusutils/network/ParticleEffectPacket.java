package projecthds.herodotusutils.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ParticleEffectPacket implements IMessage {
    private double x, y, z;

    public ParticleEffectPacket() {}

    public ParticleEffectPacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }
    public static class Handler implements IMessageHandler<ParticleEffectPacket, IMessage> {
        @Override
        public IMessage onMessage(ParticleEffectPacket message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                World world = Minecraft.getMinecraft().world;
                if (world != null) {
                    spawnParticles(world, message.x, message.y, message.z);
                }
            });
            return null;
        }

        private void spawnParticles(World world, double x, double y, double z) {
            new Thread(() -> {
                for (int t = 0; t < 40; t++) {
                    try {
                        Thread.sleep(50);
                        Minecraft.getMinecraft().addScheduledTask(() -> {
                            for (int i = 0; i < 5; i++) {
                                double px = x + world.rand.nextDouble() * 0.5;
                                double py = y + (world.rand.nextDouble() - 0.5) * 0.5;
                                double pz = z + world.rand.nextDouble() * 0.5;

                                world.spawnParticle(EnumParticleTypes.CLOUD,
                                        px, py, pz, 0, 0.1, 0);
                            }
                        });
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }).start();
        }
    }
}
