package net.jadenxgamer.netherexp.registry.entity.client;

import net.jadenxgamer.netherexp.NetherExp;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class JNEModelLayers {
    public static final ModelLayerLocation APPARITION_LAYER = new ModelLayerLocation(
            new ResourceLocation(NetherExp.MOD_ID, "apparition_layer"), "main");

    public static final ModelLayerLocation WISP_LAYER = new ModelLayerLocation(
            new ResourceLocation(NetherExp.MOD_ID, "wisp_layer"), "main");

    public static final ModelLayerLocation VESSEL_LAYER = new ModelLayerLocation(
            new ResourceLocation(NetherExp.MOD_ID, "vessel_layer"), "main");

    public static final ModelLayerLocation MIST_CHARGE_LAYER = new ModelLayerLocation(
            new ResourceLocation(NetherExp.MOD_ID, "mist_charge_layer"), "main");
}