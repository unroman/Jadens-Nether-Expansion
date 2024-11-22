package net.jadenxgamer.netherexp.compat;

import dev.architectury.platform.Platform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class CompatUtil {

    public static String CINDERSCAPES = "cinderscapes";
    public static String GARDENS_OF_THE_DEAD = "gardens_of_the_dead";
    public static String INFERNAL_EXPANSION = "infernalexp";
    public static final String GREED_AND_BLEED = "greedandbleed";
    public static final String BIOMES_O_PLENTY = "biomesoplenty";
    public static final String QUARK = "quark";
    public static final String DICEY_VENTURE = "dicey_ventures";
    public static final String FARMERS_DELIGHT = "farmersdelight";
    public static final String MY_NETHERS_DELIGHT = "mynethersdelight";
    public static final String OREGANIZED = "oreganized";
    public static final String ALEXS_CAVES = "alexscaves";
    public static final String ALEXS_MOBS = "alexsmobs";
    public static final String ARTS_AND_CRAFTS = "arts_and_crafts";
    public static final String CAVERNS_AND_CHASMS = "caverns_and_chasms";
    public static final String SULLYS_MOD = "sullysmod";
    public static final String SUPPLEMENTARIES = "supplementaries";
    public static final String RUBINATED_NETHER = "rubinated_nether";

    public static boolean compatCinderscapes() {
        return Platform.isModLoaded(CINDERSCAPES);
    }

    public static boolean compatGardensOfTheDead() {
        return Platform.isModLoaded(GARDENS_OF_THE_DEAD);
    }

    public static boolean compatInfernalExpansion() {
        return Platform.isModLoaded(INFERNAL_EXPANSION);
    }

    public static boolean compatGreedAndBleed() {
        return Platform.isModLoaded(GREED_AND_BLEED);
    }

    public static boolean compatBiomesOPlenty() {
        return Platform.isModLoaded(BIOMES_O_PLENTY);
    }

    public static boolean compatQuark() {
        return Platform.isModLoaded(QUARK);
    }

    public static boolean compatDiceyVentures() {
        return Platform.isModLoaded(DICEY_VENTURE);
    }

    public static boolean compatFarmersDelight() {
        return Platform.isModLoaded(FARMERS_DELIGHT);
    }

    public static boolean compatNethersDelight() {
        return Platform.isModLoaded(MY_NETHERS_DELIGHT);
    }

    public static boolean compatOreganized() {
        return Platform.isModLoaded(OREGANIZED);
    }

    public static boolean compatAlexsCaves() {
        return Platform.isModLoaded(ALEXS_CAVES);
    }

    public static boolean compatAlexsMobs() {
        return Platform.isModLoaded(ALEXS_MOBS);
    }

    public static boolean compatArtsAndCrafts() {
        return Platform.isModLoaded(ARTS_AND_CRAFTS);
    }

    public static boolean compatCavernsAndChasms() {
        return Platform.isModLoaded(CAVERNS_AND_CHASMS);
    }

    public static boolean compatSullysMod() {
        return Platform.isModLoaded(SULLYS_MOD);
    }

    public static boolean compatSupplementaries() {
        return Platform.isModLoaded(SUPPLEMENTARIES);
    }

    public static boolean compatRubinatedNether() {
        return Platform.isModLoaded(RUBINATED_NETHER);
    }

    public static class BiomeKeys {
        public static final ResourceKey<Biome> SOULBLIGHT_FOREST = register(CompatUtil.GARDENS_OF_THE_DEAD, "soulblight_forest");

        private static ResourceKey<Biome> register(String namespace, String id) {
            return ResourceKey.create(Registries.BIOME, new ResourceLocation(namespace, id));
        }
    }

    public static class Registry {

        public static Block getBlock(ResourceLocation id) {
            return BuiltInRegistries.BLOCK.get(id);
        }

        public static Item getItem(ResourceLocation id) {
            return BuiltInRegistries.ITEM.get(id);
        }
    }
}
