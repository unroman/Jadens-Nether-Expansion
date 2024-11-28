package net.jadenxgamer.netherexp.registry.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

public class ShotgunFistModel extends Model {
	private ModelPart shotgun;

	public ShotgunFistModel(ModelPart root) {
		this(RenderType::entityCutoutNoCull);
		this.shotgun = root.getChild("shotgun");
	}

	protected ShotgunFistModel(Function<ResourceLocation, RenderType> pRenderType) {
		super(pRenderType);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition shotgun = partdefinition.addOrReplaceChild("shotgun", CubeListBuilder.create().texOffs(0, 27).addBox(-11.0F, -10.0F, 3.0F, 6.0F, 6.0F, 10.0F, new CubeDeformation(-0.05F))
		.texOffs(0, 49).addBox(-12.0F, -11.0F, 13.0F, 8.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 24.0F, -8.0F));

		PartDefinition skull = shotgun.addOrReplaceChild("skull", CubeListBuilder.create().texOffs(0, 12).addBox(-3.0F, 0.5F, -6.5F, 6.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -4.5F, -6.5F, 8.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, -10.5F, 8.5F));

		PartDefinition jaw = shotgun.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(26, 16).addBox(-3.0F, -2.0F, -6.5F, 6.0F, 4.0F, 7.0F, new CubeDeformation(-0.02F)), PartPose.offset(-8.0F, -4.0F, 8.5F));

		PartDefinition trigger = shotgun.addOrReplaceChild("trigger", CubeListBuilder.create().texOffs(0, 43).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, -4.0F, 12.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		shotgun.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(Entity entity, float ageInTicks) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
	}

	public ModelPart root() {
		return shotgun;
	}
}