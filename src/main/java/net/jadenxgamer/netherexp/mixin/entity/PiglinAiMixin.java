package net.jadenxgamer.netherexp.mixin.entity;

import net.jadenxgamer.netherexp.registry.misc_registry.JNETags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinAi.class)
public class PiglinAiMixin {
    @Inject(
            method = "isZombified",
            at = @At(value = "TAIL"),
            cancellable = true
    )
    private static void netherexp$isAfraidMob(EntityType<?> entityType, CallbackInfoReturnable<Boolean> cir) {
        // for some reason mojang removed the fucking piglins_afraid_of tag
        if (entityType.is(JNETags.EntityTypes.PIGLINS_AFRAID_OF)) {
            cir.setReturnValue(true);
        }
    }
}
