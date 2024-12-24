package net.jadenxgamer.netherexp.mixin.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.registry.entity.variants.StrayType;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.StrayClothingLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Stray;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StrayClothingLayer.class)
public abstract class StrayClothingLayerMixin<T extends Mob & RangedAttackMob, M extends EntityModel<T>> extends RenderLayer<T, M> {

    @Unique
    private static final ResourceLocation BLACK_STRAY_CLOTHES_LOCATION = new ResourceLocation(NetherExp.MOD_ID ,"textures/entity/skeleton/black_stray_overlay.png");

    @Shadow @Final private SkeletonModel<T> layerModel;

    public StrayClothingLayerMixin(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/Mob;FFFFFF)V",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void netherexp$strayVariantClothing(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, CallbackInfo ci) {
        if (pLivingEntity instanceof Stray stray && stray instanceof VariantHolder<?> variant && variant.getVariant() == StrayType.BLACK) {
            coloredCutoutModelCopyLayerRender(this.getParentModel(), layerModel, BLACK_STRAY_CLOTHES_LOCATION, pMatrixStack, pBuffer, pPackedLight, pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch, pPartialTicks, 1.0F, 1.0F, 1.0F);
            ci.cancel();
        }
    }
}
