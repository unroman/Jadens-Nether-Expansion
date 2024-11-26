package net.jadenxgamer.netherexp.registry.entity.client.layer;

import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.registry.entity.client.BansheeModel;
import net.jadenxgamer.netherexp.registry.entity.client.CarcassModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class CarcassGlowlayer<T extends LivingEntity> extends EyesLayer<T, CarcassModel<T>> {
    public CarcassGlowlayer(RenderLayerParent<T, CarcassModel<T>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public @NotNull RenderType renderType() {
        return RenderType.eyes(new ResourceLocation(NetherExp.MOD_ID, "textures/entity/carcass/carcass_glow.png"));
    }
}
