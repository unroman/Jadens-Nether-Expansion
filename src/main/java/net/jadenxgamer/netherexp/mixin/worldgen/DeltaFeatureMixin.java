package net.jadenxgamer.netherexp.mixin.worldgen;

import net.jadenxgamer.netherexp.registry.misc_registry.JNETags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.DeltaFeature;
import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DeltaFeature.class)
public class DeltaFeatureMixin {

    @Inject(
            method = "isClear",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private static void netherexp$isClear(LevelAccessor level, BlockPos pos, DeltaFeatureConfiguration config, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState = level.getBlockState(pos);
        if (blockState.is(JNETags.Blocks.SANCTUM_BLOCKS)) {
            cir.setReturnValue(false);
        }
    }
}

