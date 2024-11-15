package net.jadenxgamer.netherexp.mixin.worldgen;

import net.jadenxgamer.netherexp.registry.misc_registry.JNETags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.BasaltColumnsFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BasaltColumnsFeature.class)
public class BasaltColumnsFeatureMixin {

    @Inject(
            method = "canPlaceAt",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private static void netherexp$canPlaceAt(LevelAccessor level, int i, BlockPos.MutableBlockPos mutablePos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState = level.getBlockState(mutablePos.move(Direction.DOWN));
        mutablePos.move(Direction.UP);
        if (blockState.is(JNETags.Blocks.SANCTUM_BLOCKS) || level.getBlockState(mutablePos).is(JNETags.Blocks.SANCTUM_BLOCKS)) {
            cir.setReturnValue(false);
        }
    }
}
