package net.jadenxgamer.netherexp.mixin.renderer;

import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.registry.entity.variants.StrayType;
import net.minecraft.client.renderer.entity.StrayRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StrayRenderer.class)
public class StrayRendererMixin {

    @Inject(
            method = "getTextureLocation(Lnet/minecraft/world/entity/monster/AbstractSkeleton;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At(value = "TAIL"),
            cancellable = true
    )
    private void netherexp$strayVariants(AbstractSkeleton pEntity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (pEntity instanceof VariantHolder<?> variant && variant.getVariant() == StrayType.BLACK) {
            cir.setReturnValue(new ResourceLocation(NetherExp.MOD_ID, "textures/entity/skeleton/black_stray.png"));
        }
    }
}
