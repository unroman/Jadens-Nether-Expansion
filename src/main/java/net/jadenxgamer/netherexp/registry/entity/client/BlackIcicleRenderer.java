package net.jadenxgamer.netherexp.registry.entity.client;

import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.registry.entity.custom.BlackIcicle;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BlackIcicleRenderer extends ArrowRenderer<BlackIcicle> {
    public BlackIcicleRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(BlackIcicle entity) {
        return new ResourceLocation(NetherExp.MOD_ID, "textures/entity/projectiles/black_icicle.png");
    }
}
