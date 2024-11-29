package net.jadenxgamer.netherexp.registry.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.function.Function;

public abstract class NonEntityHierarchicalModel extends Model {
    private final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();

    protected NonEntityHierarchicalModel() {
        this(RenderType::entityCutoutNoCull);
    }

    protected NonEntityHierarchicalModel(Function<ResourceLocation, RenderType> pRenderType) {
        super(pRenderType);
    }

    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        this.root().render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }

    public abstract ModelPart root();

    public Optional<ModelPart> getAnyDescendantWithName(String pName) {
        return pName.equals("root") ? Optional.of(this.root()) : this.root().getAllParts().filter((p_233400_) -> p_233400_.hasChild(pName)).findFirst().map((p_233397_) -> p_233397_.getChild(pName));
    }

    protected void animate(ItemStack stack, ItemAnimationState pAnimationState, AnimationDefinition pAnimationDefinition, float pAgeInTicks) {
        this.animate(stack, pAnimationState, pAnimationDefinition, pAgeInTicks, 1.0F);
    }

    protected void animate(ItemStack stack, ItemAnimationState animationState, AnimationDefinition pAnimationDefinition, float pAgeInTicks, float pSpeed) {
        animationState.updateTime(pAgeInTicks, pSpeed);
        if (animationState.matchesStack(stack)) {
            animationState.ifStarted((p_233392_) -> NonEntityKeyframeAnimations.animate(this, pAnimationDefinition, p_233392_.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE));
        }
    }
}
