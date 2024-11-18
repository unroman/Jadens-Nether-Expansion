package net.jadenxgamer.netherexp;

import dev.architectury.platform.Platform;
import net.jadenxgamer.netherexp.compat.CompatUtil;
import net.jadenxgamer.netherexp.registry.advancements.JNECriteriaTriggers;
import net.jadenxgamer.netherexp.registry.block.JNEBlockEntityType;
import net.jadenxgamer.netherexp.registry.block.JNEBlocks;
import net.jadenxgamer.netherexp.registry.effect.JNEMobEffects;
import net.jadenxgamer.netherexp.registry.enchantment.JNEEnchantments;
import net.jadenxgamer.netherexp.registry.entity.JNEEntityType;
import net.jadenxgamer.netherexp.registry.fluid.JNEFluids;
import net.jadenxgamer.netherexp.registry.item.JNECreativeModeTabs;
import net.jadenxgamer.netherexp.registry.item.JNEItems;
import net.jadenxgamer.netherexp.registry.item.brewing.JNEPotions;
import net.jadenxgamer.netherexp.registry.misc_registry.JNEPaintings;
import net.jadenxgamer.netherexp.registry.misc_registry.JNESoundEvents;
import net.jadenxgamer.netherexp.registry.particle.JNEParticleTypes;
import net.jadenxgamer.netherexp.registry.worldgen.feature.JNEFeature;
import net.jadenxgamer.netherexp.registry.worldgen.structure.JNEStructureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetherExp {
    public static final String MOD_ID = "netherexp";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        JNECriteriaTriggers.init();
        JNECreativeModeTabs.init();
        JNESoundEvents.init();
        JNEParticleTypes.init();
        JNEEnchantments.init();
        JNEMobEffects.init();

        JNEEntityType.init();
        JNEStructureType.init();
        JNEFluids.init();
        JNEPaintings.init();
        JNEBlocks.init();
        JNEFeature.init();
        JNEBlockEntityType.init();
        JNEItems.init();
        JNEPotions.init();
    }

    // MOD COMPATIBILITY CHECKS

    public static boolean compatCinderscapes() {
        return Platform.isModLoaded(CompatUtil.CINDERSCAPES);
    }

    public static boolean compatGardensOfTheDead() {
        return Platform.isModLoaded(CompatUtil.GARDENS_OF_THE_DEAD);
    }

    public static boolean compatInfernalExpansion() {
        return Platform.isModLoaded(CompatUtil.INFERNAL_EXPANSION);
    }

    public static boolean compatGreedAndBleed() {
        return Platform.isModLoaded(CompatUtil.GREED_AND_BLEED);
    }

    public static boolean compatBiomesOPlenty() {
        return Platform.isModLoaded(CompatUtil.BIOMES_O_PLENTY);
    }

    public static boolean compatQuark() {
        return Platform.isModLoaded(CompatUtil.QUARK);
    }

    public static boolean compatDiceyVentures() {
        return Platform.isModLoaded(CompatUtil.DICEY_VENTURE);
    }

    public static boolean compatFarmersDelight() {
        return Platform.isModLoaded(CompatUtil.FARMERS_DELIGHT);
    }

    public static boolean compatNethersDelight() {
        return Platform.isModLoaded(CompatUtil.MY_NETHERS_DELIGHT);
    }

    public static boolean compatOreganized() {
        return Platform.isModLoaded(CompatUtil.OREGANIZED);
    }

    public static boolean compatAlexsCaves() {
        return Platform.isModLoaded(CompatUtil.ALEXS_CAVES);
    }

    public static boolean compatAlexsMobs() {
        return Platform.isModLoaded(CompatUtil.ALEXS_MOBS);
    }

    public static boolean compatArtsAndCrafts() {
        return Platform.isModLoaded(CompatUtil.ARTS_AND_CRAFTS);
    }

    public static boolean compatCavernsAndChasms() {
        return Platform.isModLoaded(CompatUtil.CAVERNS_AND_CHASMS);
    }

    public static boolean compatSullysMod() {
        return Platform.isModLoaded(CompatUtil.SULLYS_MOD);
    }

    public static boolean compatSupplementaries() {
        return Platform.isModLoaded(CompatUtil.SUPPLEMENTARIES);
    }
}
