package net.jadenxgamer.netherexp.fabric.worldgen;

import net.jadenxgamer.netherexp.compat.CompatUtil;
import net.jadenxgamer.netherexp.fabric.worldgen.biomes.*;

public class FeatureGeneration {

    public static void generateFeatures() {
        NetherWastesFeatures.init();
        SoulSandValleyFeatures.init();
        CrimsonForestFeatures.init();
        WarpedForestFeatures.init();
        BasaltDeltasFeatures.init();
        if (CompatUtil.compatGardensOfTheDead()) {
            SoulblightForestFeatures.init();
        }
    }
}
