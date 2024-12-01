// Made with Blockbench 4.9.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class JackhammerFistModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "jackhammerfistmodel"), "main");
	private final ModelPart main;

	public JackhammerFistModel(ModelPart root) {
		this.main = root.getChild("main");
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
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}