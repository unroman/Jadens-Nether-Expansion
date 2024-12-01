package net.jadenxgamer.netherexp.registry.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.jadenxgamer.netherexp.registry.item.custom.JackhammerFistItem;
import net.jadenxgamer.netherexp.registry.misc_registry.JNEAnimationDefinition;
import net.jadenxgamer.netherexp.util.NonEntityHierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class JackhammerFistModel extends NonEntityHierarchicalModel {
	private final ModelPart main;
    private final ModelPart dial;
	private float previousSpeed = 0.0F;

	public JackhammerFistModel(ModelPart root) {
		this.main = root.getChild("main");
        ModelPart shotgun = main.getChild("shotgun");
        ModelPart speedometer = shotgun.getChild("speedometer");
		this.dial = speedometer.getChild("dial");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition shotgun = main.addOrReplaceChild("shotgun", CubeListBuilder.create().texOffs(30, 6).addBox(-4.0F, -10.0F, 5.0F, 8.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 15).addBox(-3.0F, -9.0F, -5.0F, 6.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition trigger = shotgun.addOrReplaceChild("trigger", CubeListBuilder.create().texOffs(0, 15).addBox(-1.0F, -1.5F, -1.5F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, 0.5F));

		PartDefinition skull = shotgun.addOrReplaceChild("skull", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.0F, -10.25F, 12.0F, 12.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 31).addBox(-1.0F, -1.0F, -7.25F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, -3.75F));

		PartDefinition speedometer = shotgun.addOrReplaceChild("speedometer", CubeListBuilder.create().texOffs(30, 0).addBox(0.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -6.0F, -2.0F, 0.0F, 0.6545F, 0.0F));

		PartDefinition dial = speedometer.addOrReplaceChild("dial", CubeListBuilder.create().texOffs(40, 0).addBox(0.0F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 1.1F, 0.0F, 0.0F, 0.6981F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, ItemStack stack, float ageInTicks) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		JackhammerFistItem item = (JackhammerFistItem) stack.getItem();
		this.animate(entity, (item).punchAnimationState, JNEAnimationDefinition.JACKHAMMER_FIST_PUNCH, ageInTicks);
		this.animate(entity, (item).pullAnimationState, JNEAnimationDefinition.JACKHAMMER_FIST_PULL, ageInTicks);
		this.animate(entity, (item).pullLoopAnimationState, JNEAnimationDefinition.JACKHAMMER_FIST_PULL_LOOP, ageInTicks);

		if (entity != null) {
			double horizontalSpeed = Math.sqrt(entity.getDeltaMovement().x * entity.getDeltaMovement().x + entity.getDeltaMovement().z * entity.getDeltaMovement().z);
			double maxSpeed = 10.0;
			float normalizedSpeed = (float) Math.min(horizontalSpeed / maxSpeed, 1.0);

			float zRotation = Math.max(-260.0F, Math.min(Mth.lerp(normalizedSpeed, 0.0F, -260.0F), 0.0F));
			this.dial.setRotation(0.0F, 0.0F, zRotation);
		}
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		super.renderToBuffer(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
	}

	@Override
	public ModelPart root() {
		return main;
	}
}