package net.jadenxgamer.netherexp.registry.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.registry.entity.client.JNEModelLayers;
import net.jadenxgamer.netherexp.registry.item.client.custom.PumpChargeShotgunModel;
import net.jadenxgamer.netherexp.registry.item.client.custom.ShotgunFistModel;
import net.jadenxgamer.netherexp.registry.item.custom.PumpChargeShotgunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class JNEItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static final ShotgunFistModel SHOTGUN_FIST_MODEL = new ShotgunFistModel(Minecraft.getInstance().getEntityModels().bakeLayer(JNEModelLayers.SHOTGUN_FIST_LAYER));
    private static final PumpChargeShotgunModel PUMP_CHARGE_SHOTGUN_MODEL = new PumpChargeShotgunModel(Minecraft.getInstance().getEntityModels().bakeLayer(JNEModelLayers.PUMP_CHARGE_SHOTGUN_LAYER));

    public JNEItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int overlay) {
        float partialTick = Minecraft.getInstance().getPartialTick();
        Player player = Minecraft.getInstance().player;
        Entity entity = null;
        if (player != null) {
            if (player.getMainHandItem() == stack) {
                entity = player;
            } else if (player.getOffhandItem() == stack) {
                entity = player;
            }
        }
        if (stack.is(JNEItems.PUMP_CHARGE_SHOTGUN.get())) {
            int tickCount = player == null ? 0 : player.tickCount;
            float ageInTicks = player == null ? 0f : tickCount + partialTick;
            poseStack.pushPose();
            poseStack.translate(0.5f, 1.5f, 0.5f);
            poseStack.mulPose(Axis.XP.rotationDegrees(-180));
            poseStack.mulPose(Axis.YP.rotationDegrees(180));
            poseStack.scale(1.0F, 1.0F, 1.0F);
            ResourceLocation texture = new ResourceLocation(NetherExp.MOD_ID, "textures/entity/pump_charge_shotgun/pumps_" + PumpChargeShotgunItem.getCharge(stack) + ".png");
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));
            PUMP_CHARGE_SHOTGUN_MODEL.setupAnim(entity, stack, ageInTicks);
            PUMP_CHARGE_SHOTGUN_MODEL.renderToBuffer(poseStack, vertexConsumer, packedLight, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            poseStack.popPose();
        }
        if (stack.is(JNEItems.SHOTGUN_FIST.get())) {
            int tickCount = player == null ? 0 : player.tickCount;
            float ageInTicks = player == null ? 0f : tickCount + partialTick;
            poseStack.pushPose();
            poseStack.translate(0.5f, 1.5f, 0.5f);
            poseStack.mulPose(Axis.XP.rotationDegrees(-180));
            poseStack.mulPose(Axis.YP.rotationDegrees(180));
            poseStack.scale(1.0F, 1.0F, 1.0F);
            ResourceLocation texture = new ResourceLocation(NetherExp.MOD_ID, "textures/entity/shotgun_fist/normal.png");
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));
            SHOTGUN_FIST_MODEL.setupAnim(entity, stack, ageInTicks);
            SHOTGUN_FIST_MODEL.renderToBuffer(poseStack, vertexConsumer, packedLight, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            poseStack.popPose();
        }
    }
}
