package projecthds.herodotusutils.modsupport.thaumcraft;

import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.text.WordUtils;
import projecthds.herodotusutils.HerodotusUtils;
import thaumcraft.api.aspects.Aspect;

import java.util.Arrays;
import java.util.List;

public class AspectHandler {
    public static Aspect NETHER;
    public static Aspect WRATH;
    public static Aspect ENVY;
    public static Aspect GLUTTONY;
    public static Aspect PRIDE;
    public static Aspect LUST;
    public static Aspect SLOTH;
    public static Aspect INSPIRATION;

    public static void initAspects() {
        LUST = new Aspect("luxuria", 0xffc1ce, new Aspect[]{Aspect.BEAST, Aspect.DESIRE}, HerodotusUtils.rl("textures/aspects/luxuria.png"), 1);
        NETHER = new Aspect("infernus", 0xff0000, new Aspect[]{Aspect.FIRE, Aspect.MAGIC}, HerodotusUtils.rl("textures/aspects/infernus.png"), 771);
        PRIDE = new Aspect("superbia", 0x9639ff, new Aspect[]{Aspect.FLIGHT, Aspect.VOID}, HerodotusUtils.rl("textures/aspects/superbia.png"), 1);
        GLUTTONY = new Aspect("gula", 0xd59c46, new Aspect[]{Aspect.DESIRE, Aspect.VOID}, HerodotusUtils.rl("textures/aspects/gula.png"), 1);
        ENVY = new Aspect("invidia", 0x00ba00, new Aspect[]{Aspect.SENSES, Aspect.DESIRE}, HerodotusUtils.rl("textures/aspects/invidia.png"), 1);
        SLOTH = new Aspect("desidia", 0x6e6e6e, new Aspect[]{Aspect.TRAP, Aspect.SOUL}, HerodotusUtils.rl("textures/aspects/desidia.png"), 771);
        WRATH = new Aspect("ira", 0x870404, new Aspect[]{Aspect.AVERSION, Aspect.FIRE}, HerodotusUtils.rl("textures/aspects/ira.png"), 771);
        INSPIRATION = new Aspect("revelatio", 0xff8c00, new Aspect[]{Aspect.MIND, Aspect.ELDRITCH}, HerodotusUtils.rl("textures/aspects/revelatio.png"), 771);
        Aspect.FLUX.setComponents(null);
        Aspect.ELDRITCH.setComponents(null);
        Aspect.mixList.remove((Aspect.ENTROPY.getTag() + Aspect.MAGIC.getTag()).hashCode());
        Aspect.mixList.remove((Aspect.VOID.getTag() + Aspect.DARKNESS.getTag()).hashCode());

        int red = 0xfc0d20;
        int yellow = 0xffd701;
        int blue = 0x00a2dd;
        List<String> shapes = Arrays.asList("rhombus", "sphaericus", "quadrata");
        new Aspect("rubrum", red, null, HerodotusUtils.rl("textures/aspects/red.png"), 1);
        new Aspect("flavum", yellow, null, HerodotusUtils.rl("textures/aspects/yellow.png"), 1);
        new Aspect("caeruleum", blue, null, HerodotusUtils.rl("textures/aspects/blue.png"), 1);
        for (String shape : shapes) {
            ResourceLocation texture = HerodotusUtils.rl("textures/aspects/" + shape + ".png");
            new Aspect(shape, 0xffffff, null, texture, 1);
            new CustomAspect("rubrum_" + shape, red, null, texture, 1);
            new CustomAspect("flavum_" + shape, yellow, null, texture, 1);
            new CustomAspect("caeruleum_" + shape, blue, null, texture, 1);
        }
    }

    public static class CustomAspect extends Aspect {
        public CustomAspect(String tag, int color, Aspect[] components, ResourceLocation image, int blend) {
            super(tag, color, components, image, blend);
        }

        @Override
        public String getName() {
            return WordUtils.capitalizeFully(getTag(), '_', ' ').replace('_', ' ');
        }
    }

}

