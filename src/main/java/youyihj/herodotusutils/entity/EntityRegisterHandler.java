package youyihj.herodotusutils.entity;

import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import youyihj.herodotusutils.HerodotusUtils;
import youyihj.herodotusutils.entity.golem.EntityExtraIronGolem;
import youyihj.herodotusutils.entity.golem.EntityExtraSnowman;

import java.awt.*;

@EventBusSubscriber
public class EntityRegisterHandler {
    public static final EntityEntry ENTITY_RED_SLIME = EntityEntryBuilder.create()
            .entity(EntityRedSlime.class)
            .id("red_slime", 0)
            .name(HerodotusUtils.MOD_ID + ".red_slime")
            .tracker(80, 3, true)
            .egg(Color.RED.getRGB(), Color.MAGENTA.getRGB())
            .build();

    public static final EntityEntry ENTITY_EXTRA_GOLEM = EntityEntryBuilder.create()
            .entity(EntityExtraIronGolem.class)
            .id("alchemy_golem", 1)
            .tracker(80, 3, true)
            .name(HerodotusUtils.MOD_ID + ".alchemy_golem")
            .build();

    public static final EntityEntry ENTITY_EXTRA_SNOW_MAN = EntityEntryBuilder.create()
            .entity(EntityExtraSnowman.class)
            .id("alchemy_snow_man", 2)
            .tracker(80, 3, true)
            .name(HerodotusUtils.MOD_ID + ".alchemy_snow_golem")
            .build();


    @SubscribeEvent
    public static void onRegistry(Register<EntityEntry> event) {
        event.getRegistry().registerAll(ENTITY_RED_SLIME, ENTITY_EXTRA_GOLEM, ENTITY_EXTRA_SNOW_MAN);
    }
}
