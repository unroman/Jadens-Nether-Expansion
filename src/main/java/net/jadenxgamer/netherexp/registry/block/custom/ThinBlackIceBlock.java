package net.jadenxgamer.netherexp.registry.block.custom;

import net.jadenxgamer.netherexp.registry.misc_registry.JNESoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ThinBlackIceBlock extends BlackIceBlock {
    public ThinBlackIceBlock(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
        return Shapes.empty();
    }

    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return true;
    }

    @Override
    public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pSide) {
        return pAdjacentBlockState.is(this) ? true : super.skipRendering(pState, pAdjacentBlockState, pSide);
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (!pLevel.isClientSide() && pEntity instanceof LivingEntity livingEntity && pLevel.random.nextFloat() < 0.07f) {
            int breakingOdds = livingEntity.isSprinting() ? 5 : 15;
            if (!livingEntity.isShiftKeyDown() && !EnchantmentHelper.hasSoulSpeed(livingEntity)) {
                if (pLevel.random.nextInt(breakingOdds) == 0) {
                    pLevel.destroyBlock(pPos, false);
                } else {
                    pLevel.playSound(null, pPos, JNESoundEvents.BLOCK_THIN_BLACK_ICE_CRACKING.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                }
            }
        }
    }

    @Override
    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {
        if (pFallDistance >= 1.0f) {
            pLevel.destroyBlock(pPos, false);
            pLevel.playSound(null, pPos, JNESoundEvents.BLOCK_THIN_BLACK_ICE_CRACKING.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
        }
    }
}
