package net.jadenxgamer.netherexp.registry.item.client;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NonEntityKeyframeAnimations {

    public NonEntityKeyframeAnimations() {
    }

    // Literally just vanilla's KeyframeAnimations but modified to not use the entity HierarchicalModel

    public static void animate(NonEntityHierarchicalModel pModel, AnimationDefinition pAnimationDefinition, long pAccumulatedTime, float pScale, Vector3f pAnimationVecCache) {
        float $$5 = getElapsedSeconds(pAnimationDefinition, pAccumulatedTime);

        for (Map.Entry<String, List<AnimationChannel>> stringListEntry : pAnimationDefinition.boneAnimations().entrySet()) {
            Map.Entry<String, List<AnimationChannel>> $$6 = (Map.Entry) stringListEntry;
            Optional<ModelPart> $$7 = pModel.getAnyDescendantWithName((String) $$6.getKey());
            List<AnimationChannel> $$8 = (List) $$6.getValue();
            $$7.ifPresent((p_232330_) -> {
                $$8.forEach((p_288241_) -> {
                    Keyframe[] $$5x = p_288241_.keyframes();
                    int $$62 = Math.max(0, Mth.binarySearch(0, $$5x.length, (p_232315_) -> $$5 <= $$5x[p_232315_].timestamp()) - 1);
                    int $$72 = Math.min($$5x.length - 1, $$62 + 1);
                    Keyframe $$82 = $$5x[$$62];
                    Keyframe $$9 = $$5x[$$72];
                    float $$10 = $$5 - $$82.timestamp();
                    float $$12;
                    if ($$72 != $$62) {
                        $$12 = Mth.clamp($$10 / ($$9.timestamp() - $$82.timestamp()), 0.0F, 1.0F);
                    } else {
                        $$12 = 0.0F;
                    }

                    $$9.interpolation().apply(pAnimationVecCache, $$12, $$5x, $$62, $$72, pScale);
                    p_288241_.target().apply(p_232330_, pAnimationVecCache);
                });
            });
        }

    }

    private static float getElapsedSeconds(AnimationDefinition pAnimationDefinition, long pAccumulatedTime) {
        float $$2 = (float)pAccumulatedTime / 1000.0F;
        return pAnimationDefinition.looping() ? $$2 % pAnimationDefinition.lengthInSeconds() : $$2;
    }

    public static Vector3f posVec(float pX, float pY, float pZ) {
        return new Vector3f(pX, -pY, pZ);
    }

    public static Vector3f degreeVec(float pXDegrees, float pYDegrees, float pZDegrees) {
        return new Vector3f(pXDegrees * 0.017453292F, pYDegrees * 0.017453292F, pZDegrees * 0.017453292F);
    }

    public static Vector3f scaleVec(double pXScale, double pYScale, double pZScale) {
        return new Vector3f((float)(pXScale - 1.0), (float)(pYScale - 1.0), (float)(pZScale - 1.0));
    }
}
