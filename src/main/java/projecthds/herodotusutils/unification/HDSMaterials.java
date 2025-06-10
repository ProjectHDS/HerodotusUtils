package projecthds.herodotusutils.unification;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.properties.IngotProperty;
import gregtech.api.unification.material.properties.PropertyKey;

import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialIconSet.WOOD;
import static projecthds.herodotusutils.util.Util.hdsId;

public class HDSMaterials {

    public static Material CopperCoatedWood;
    public static Material IronCoatedWood;
    public static Material TinCoatedWood;
    public static Material LeadCoatedWood;

    public static void init() {
        CopperCoatedWood = new Material.Builder(1, hdsId("copper_coated_wood"))
                .ingot()
                .color(0xFF6400).iconSet(WOOD)
                .flags(NO_UNIFICATION, GENERATE_PLATE, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_ROD, GENERATE_LONG_ROD, GENERATE_RING, GENERATE_BOLT_SCREW)
                .build();

        IronCoatedWood = new Material.Builder(2, hdsId("iron_coated_wood"))
                .ingot()
                .color(0xC8C8C8).iconSet(WOOD)
                .flags(NO_UNIFICATION, GENERATE_PLATE, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_ROD, GENERATE_LONG_ROD, GENERATE_RING, GENERATE_BOLT_SCREW)
                .build();

        TinCoatedWood = new Material.Builder(3, hdsId("tin_coated_wood"))
                .ingot()
                .color(0xDCDCDC).iconSet(WOOD)
                .flags(NO_UNIFICATION, GENERATE_PLATE, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_ROD, GENERATE_LONG_ROD, GENERATE_RING, GENERATE_BOLT_SCREW)
                .build();

        LeadCoatedWood = new Material.Builder(4, hdsId("lead_coated_wood"))
                .ingot()
                .color(0x8C648C).iconSet(WOOD)
                .flags(NO_UNIFICATION, GENERATE_PLATE, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_ROD, GENERATE_LONG_ROD, GENERATE_RING, GENERATE_BOLT_SCREW)
                .build();
    }

    public static void addFlag() {
        Materials.Wood.addFlags(GENERATE_SMALL_GEAR, GENERATE_RING);
    }

    public static void addProperty() {
        Materials.Wood.setProperty(PropertyKey.INGOT, new IngotProperty());
    }

}
