package net.jadenxgamer.netherexp.registry.worldgen.feature.custom;

import com.mojang.serialization.Codec;
import net.jadenxgamer.netherexp.registry.block.JNEBlocks;
import net.jadenxgamer.netherexp.registry.misc_registry.JNETags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.levelgen.feature.DripstoneUtils;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.PointedDripstoneConfiguration;

import java.util.Optional;
import java.util.function.Consumer;

public class BlackIcicleFeature extends Feature<PointedDripstoneConfiguration> {
    public BlackIcicleFeature(Codec<PointedDripstoneConfiguration> pCodec) {
        super(pCodec);
    }

    public boolean place(FeaturePlaceContext<PointedDripstoneConfiguration> pContext) {
        LevelAccessor $$1 = pContext.level();
        BlockPos $$2 = pContext.origin();
        RandomSource $$3 = pContext.random();
        PointedDripstoneConfiguration $$4 = pContext.config();
        Optional<Direction> $$5 = getTipDirection($$1, $$2, $$3);
        if ($$5.isEmpty()) {
            return false;
        } else {
            BlockPos $$6 = $$2.relative($$5.get().getOpposite());
            createPatchOfBlackIce($$1, $$3, $$6, $$4);
            int $$7 = $$3.nextFloat() < $$4.chanceOfTallerDripstone && DripstoneUtils.isEmptyOrWater($$1.getBlockState($$2.relative((Direction)$$5.get()))) ? 2 : 1;
            growBlackIcicle($$1, $$2, $$5.get(), $$7, false);
            return true;
        }
    }

    private static Optional<Direction> getTipDirection(LevelAccessor pLevel, BlockPos pPos, RandomSource pRandom) {
        boolean $$3 = DripstoneUtils.isDripstoneBase(pLevel.getBlockState(pPos.above()));
        boolean $$4 = DripstoneUtils.isDripstoneBase(pLevel.getBlockState(pPos.below()));
        if ($$3 && $$4) {
            return Optional.of(pRandom.nextBoolean() ? Direction.DOWN : Direction.UP);
        } else if ($$3) {
            return Optional.of(Direction.DOWN);
        } else {
            return $$4 ? Optional.of(Direction.UP) : Optional.empty();
        }
    }

    private static void createPatchOfBlackIce(LevelAccessor pLevel, RandomSource pRandom, BlockPos pPos, PointedDripstoneConfiguration pConfig) {
        placeBlackIceIfPossible(pLevel, pPos);

        for (Direction $$4 : Direction.Plane.HORIZONTAL) {
            if (!(pRandom.nextFloat() > pConfig.chanceOfDirectionalSpread)) {
                BlockPos $$5 = pPos.relative($$4);
                placeBlackIceIfPossible(pLevel, $$5);
                if (!(pRandom.nextFloat() > pConfig.chanceOfSpreadRadius2)) {
                    BlockPos $$6 = $$5.relative(Direction.getRandom(pRandom));
                    placeBlackIceIfPossible(pLevel, $$6);
                    if (!(pRandom.nextFloat() > pConfig.chanceOfSpreadRadius3)) {
                        BlockPos $$7 = $$6.relative(Direction.getRandom(pRandom));
                        placeBlackIceIfPossible(pLevel, $$7);
                    }
                }
            }
        }

    }

    protected static void growBlackIcicle(LevelAccessor pLevel, BlockPos pPos, Direction pDirection, int pHeight, boolean pMergeTip) {
        if (isBlackIceBaseOrLava(pLevel.getBlockState(pPos.relative(pDirection.getOpposite())))) {
            BlockPos.MutableBlockPos $$5 = pPos.mutable();
            buildBaseToTipColumn(pDirection, pHeight, pMergeTip, (p_277326_) -> {
                if (p_277326_.is(JNEBlocks.BLACK_ICICLE.get())) {
                    p_277326_ = p_277326_.setValue(PointedDripstoneBlock.WATERLOGGED, pLevel.isWaterAt($$5));
                }

                pLevel.setBlock($$5, p_277326_, 2);
                $$5.move(pDirection);
            });
        }
    }

    protected static boolean placeBlackIceIfPossible(LevelAccessor pLevel, BlockPos pPos) {
        BlockState $$2 = pLevel.getBlockState(pPos);
        if ($$2.is(JNETags.Blocks.BLACK_ICE_REPLACEABLE)) {
            pLevel.setBlock(pPos, JNEBlocks.BLACK_ICE.get().defaultBlockState(), 2);
            return true;
        } else {
            return false;
        }
    }

    public static boolean isBlackIceBaseOrLava(BlockState pState) {
        return isBlackIceBase(pState) || pState.is(Blocks.LAVA);
    }

    public static boolean isBlackIceBase(BlockState pState) {
        return pState.is(JNEBlocks.BLACK_ICE.get()) || pState.is(JNETags.Blocks.BLACK_ICE_REPLACEABLE);
    }

    protected static void buildBaseToTipColumn(Direction pDirection, int pHeight, boolean pMergeTip, Consumer<BlockState> pBlockSetter) {
        if (pHeight >= 3) {
            pBlockSetter.accept(createBlackIcicle(pDirection, DripstoneThickness.BASE));

            for(int $$4 = 0; $$4 < pHeight - 3; ++$$4) {
                pBlockSetter.accept(createBlackIcicle(pDirection, DripstoneThickness.MIDDLE));
            }
        }

        if (pHeight >= 2) {
            pBlockSetter.accept(createBlackIcicle(pDirection, DripstoneThickness.FRUSTUM));
        }

        if (pHeight >= 1) {
            pBlockSetter.accept(createBlackIcicle(pDirection, pMergeTip ? DripstoneThickness.TIP_MERGE : DripstoneThickness.TIP));
        }

    }

    private static BlockState createBlackIcicle(Direction pDirection, DripstoneThickness pDripstoneThickness) {
        return JNEBlocks.BLACK_ICICLE.get().defaultBlockState().setValue(PointedDripstoneBlock.TIP_DIRECTION, pDirection).setValue(PointedDripstoneBlock.THICKNESS, pDripstoneThickness);
    }
}