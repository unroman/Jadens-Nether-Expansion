package net.jadenxgamer.netherexp.registry.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.registry.entity.client.JNEModelLayers;
import net.jadenxgamer.netherexp.registry.item.JNEItems;
import net.jadenxgamer.netherexp.registry.item.client.PumpChargeShotgunModel;
import net.jadenxgamer.netherexp.registry.item.client.ShotgunFistModel;
import net.jadenxgamer.netherexp.registry.item.custom.PumpChargeShotgunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class JNEItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final ShotgunFistModel SHOTGUN_FIST_MODEL = new ShotgunFistModel(Minecraft.getInstance().getEntityModels().bakeLayer(JNEModelLayers.SHOTGUN_FIST_LAYER));
    private static final PumpChargeShotgunModel PUMP_CHARGE_SHOTGUN_MODEL = new PumpChargeShotgunModel(Minecraft.getInstance().getEntityModels().bakeLayer(JNEModelLayers.PUMP_CHARGE_SHOTGUN_LAYER));
    private final ResourceLocation SHOTGUN_FIST_TEXTURE = new ResourceLocation(NetherExp.MOD_ID, "textures/entity/shotgun_fist/normal");
    public JNEItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int overlay) {
        float partialTick = Minecraft.getInstance().getPartialTick();
        if (stack.is(JNEItems.PUMP_CHARGE_SHOTGUN.get())) {
            int tickCount = Minecraft.getInstance().player == null ? 0 : Minecraft.getInstance().player.tickCount;
            float ageInTicks = Minecraft.getInstance().player == null ? 0f : tickCount + partialTick;
            poseStack.pushPose();
            poseStack.translate(0.5f, 1.5f, 0.5f);
            poseStack.mulPose(Axis.XP.rotationDegrees(-180));
            poseStack.mulPose(Axis.YP.rotationDegrees(180));
            poseStack.scale(1.0F, 1.0F, 1.0F);
            ResourceLocation texture = new ResourceLocation(NetherExp.MOD_ID, "textures/entity/pump_charge_shotgun/pumps_" + PumpChargeShotgunItem.getCharge(stack) + ".png");
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));
            PUMP_CHARGE_SHOTGUN_MODEL.setupAnim(stack, ageInTicks);
            PUMP_CHARGE_SHOTGUN_MODEL.renderToBuffer(poseStack, vertexConsumer, packedLight, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            poseStack.popPose();
        }
//        if (stack.is(JNEItems.SHOTGUN_FIST.get())) {
//            poseStack.pushPose();
//            poseStack.translate(0.5f, 1.5f, 0.5f);
//            poseStack.mulPose(Axis.XP.rotationDegrees(-180));
//            poseStack.mulPose(Axis.YP.rotationDegrees(180));
//            poseStack.scale(1.0F, 1.0F, 1.0F);
//            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(SHOTGUN_FIST_TEXTURE));
//            SHOTGUN_FIST_MODEL.renderToBuffer(poseStack, vertexConsumer, packedLight, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
//            poseStack.popPose();
//        }
    }
}
