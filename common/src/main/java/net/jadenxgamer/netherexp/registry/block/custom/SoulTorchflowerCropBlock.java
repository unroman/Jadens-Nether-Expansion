package net.jadenxgamer.netherexp.registry.block.custom;

import net.jadenxgamer.netherexp.registry.block.JNEBlocks;
import net.jadenxgamer.netherexp.registry.misc_registry.JNETags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.TorchflowerCropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SoulTorchflowerCropBlock extends TorchflowerCropBlock {
    public SoulTorchflowerCropBlock(Properties settings) {
        super(settings);
    }

    @Override
    public @NotNull BlockState getStateForAge(int age) {
        return age == 2 ? JNEBlocks.SOUL_TORCHFLOWER.get().defaultBlockState() : super.getStateForAge(age);
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.is(JNETags.Blocks.SOUL_CROP_MUTATION_BLOCKS);
    }
}
