package net.jadenxgamer.netherexp.mixin.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Stray.class)
public abstract class StrayMixin extends AbstractSkeleton {

    protected StrayMixin(EntityType<? extends AbstractSkeleton> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(
            method = "checkStraySpawnRules",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private static void netherexp$checkStraySpawnRules(EntityType<Stray> pStray, ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom, CallbackInfoReturnable<Boolean> cir) {
        // lets strays spawn normally in dimensions with ceilings
        if (pLevel.dimensionType().hasCeiling()) {
            cir.setReturnValue(checkMonsterSpawnRules(pStray, pLevel, pSpawnType, pPos, pRandom));
        }
    }
}
