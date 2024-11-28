package net.jadenxgamer.netherexp.registry.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.function.Function;

public abstract class NonEntityHierarchicalModel extends Model {
    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();

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

    protected void animate(AnimationState pAnimationState, AnimationDefinition pAnimationDefinition, float pAgeInTicks) {
        this.animate(pAnimationState, pAnimationDefinition, pAgeInTicks, 1.0F);
    }

    protected void animateWalk(AnimationDefinition pAnimationDefinition, float pLimbSwing, float pLimbSwingAmount, float pMaxAnimationSpeed, float pAnimationScaleFactor) {
        long $$5 = (long)(pLimbSwing * 50.0F * pMaxAnimationSpeed);
        float $$6 = Math.min(pLimbSwingAmount * pAnimationScaleFactor, 1.0F);
        NonEntityKeyframeAnimations.animate(this, pAnimationDefinition, $$5, $$6, ANIMATION_VECTOR_CACHE);
    }

    protected void animate(AnimationState pAnimationState, AnimationDefinition pAnimationDefinition, float pAgeInTicks, float pSpeed) {
        pAnimationState.updateTime(pAgeInTicks, pSpeed);
        pAnimationState.ifStarted((p_233392_) -> {
            NonEntityKeyframeAnimations.animate(this, pAnimationDefinition, p_233392_.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE);
        });
    }
}
