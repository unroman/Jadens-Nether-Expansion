package net.jadenxgamer.netherexp.registry.block.custom;

import net.jadenxgamer.netherexp.registry.block.JNEBlocks;
import net.jadenxgamer.netherexp.registry.misc_registry.JNESoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class SoulPermafrostBlock extends Block {
    public SoulPermafrostBlock(Properties pProperties) {
        super(pProperties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean bl) {
        if (!level.isClientSide()) {
            Fluid fluid = level.getFluidState(fromPos).getType();
            if (fluid == Fluids.LAVA) {
                level.setBlock(pos, Blocks.SOUL_SOIL.defaultBlockState(), 2);
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            else if (fluid == Fluids.WATER) {
                level.setBlock(fromPos, JNEBlocks.THIN_BLACK_ICE.get().defaultBlockState(), 2);
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), JNESoundEvents.SOUL_SLATE_SOLIDIFYING.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean bl) {
        super.onPlace(state, level, pos, oldState, bl);
        if (!level.isClientSide()) {
            for (Direction direction : Direction.values()) {
                BlockPos adjacentPos = pos.relative(direction);
                Fluid fluid = level.getFluidState(adjacentPos).getType();

                if (fluid == Fluids.LAVA) {
                    level.setBlock(pos, Blocks.SOUL_SOIL.defaultBlockState(), 2);
                    level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                else if (fluid == Fluids.WATER) {
                    level.setBlock(adjacentPos, JNEBlocks.THIN_BLACK_ICE.get().defaultBlockState(), 2);
                    level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), JNESoundEvents.SOUL_SLATE_SOLIDIFYING.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                }
            }
        }
    }
}
