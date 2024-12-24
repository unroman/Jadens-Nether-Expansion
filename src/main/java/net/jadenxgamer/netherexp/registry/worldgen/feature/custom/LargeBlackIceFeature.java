package net.jadenxgamer.netherexp.registry.worldgen.feature.custom;

import com.mojang.serialization.Codec;
import net.jadenxgamer.netherexp.registry.block.JNEBlocks;
import net.jadenxgamer.netherexp.registry.misc_registry.JNETags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.DripstoneUtils;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;

public class LargeBlackIceFeature extends Feature<LargeDripstoneConfiguration> {
    public LargeBlackIceFeature(Codec<LargeDripstoneConfiguration> pCodec) {
        super(pCodec);
    }

    // taken from vanilla's LargeDripstoneFeature but modified to place Black Ice

    public boolean place(FeaturePlaceContext<LargeDripstoneConfiguration> pContext) {
        WorldGenLevel $$1 = pContext.level();
        BlockPos $$2 = pContext.origin();
        LargeDripstoneConfiguration $$3 = pContext.config();
        RandomSource $$4 = pContext.random();
        if (!isEmptyOrWater($$1, $$2)) {
            return false;
        } else {
            Optional<Column> $$5 = Column.scan($$1, $$2, $$3.floorToCeilingSearchRange, DripstoneUtils::isEmptyOrWater, LargeBlackIceFeature::isBlackIceBaseOrLava);
            if ($$5.isPresent() && $$5.get() instanceof Column.Range $$6) {
                if ($$6.height() < 4) {
                    return false;
                } else {
                    int $$7 = (int)((float)$$6.height() * $$3.maxColumnRadiusToCaveHeightRatio);
                    int $$8 = Mth.clamp($$7, $$3.columnRadius.getMinValue(), $$3.columnRadius.getMaxValue());
                    int $$9 = Mth.randomBetweenInclusive($$4, $$3.columnRadius.getMinValue(), $$8);
                    LargeBlackIce $$10 = makeBlackIce($$2.atY($$6.ceiling() - 1), false, $$4, $$9, $$3.stalactiteBluntness, $$3.heightScale);
                    LargeBlackIce $$11 = makeBlackIce($$2.atY($$6.floor() + 1), true, $$4, $$9, $$3.stalagmiteBluntness, $$3.heightScale);
                    LargeBlackIceFeature.WindOffsetter $$13;
                    if ($$10.isSuitableForWind($$3) && $$11.isSuitableForWind($$3)) {
                        $$13 = new LargeBlackIceFeature.WindOffsetter($$2.getY(), $$4, $$3.windSpeed);
                    } else {
                        $$13 = LargeBlackIceFeature.WindOffsetter.noWind();
                    }

                    boolean $$14 = $$10.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary($$1, $$13);
                    boolean $$15 = $$11.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary($$1, $$13);
                    if ($$14) {
                        $$10.placeBlocks($$1, $$4, $$13);
                    }

                    if ($$15) {
                        $$11.placeBlocks($$1, $$4, $$13);
                    }

                    return true;
                }
            } else {
                return false;
            }
        }
    }

    private static LargeBlackIce makeBlackIce(BlockPos pRoot, boolean pPointingUp, RandomSource pRandom, int pRadius, FloatProvider pBluntnessBase, FloatProvider pScaleBase) {
        return new LargeBlackIce(pRoot, pPointingUp, pRadius, pBluntnessBase.sample(pRandom), pScaleBase.sample(pRandom));
    }

    static final class LargeBlackIce {
        private BlockPos root;
        private final boolean pointingUp;
        private int radius;
        private final double bluntness;
        private final double scale;

        LargeBlackIce(BlockPos pRoot, boolean pPointingUp, int pRadius, double pBluntness, double pScale) {
            this.root = pRoot;
            this.pointingUp = pPointingUp;
            this.radius = pRadius;
            this.bluntness = pBluntness;
            this.scale = pScale;
        }

        private int getHeight() {
            return this.getHeightAtRadius(0.0F);
        }

        boolean moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(WorldGenLevel pLevel, LargeBlackIceFeature.WindOffsetter pWindOffsetter) {
            while(this.radius > 1) {
                BlockPos.MutableBlockPos $$2 = this.root.mutable();
                int $$3 = Math.min(10, this.getHeight());

                for(int $$4 = 0; $$4 < $$3; ++$$4) {
                    if (pLevel.getBlockState($$2).is(Blocks.LAVA)) {
                        return false;
                    }

                    if (isCircleMostlyEmbeddedInStone(pLevel, pWindOffsetter.offset($$2), this.radius)) {
                        this.root = $$2;
                        return true;
                    }

                    $$2.move(this.pointingUp ? Direction.DOWN : Direction.UP);
                }

                this.radius /= 2;
            }

            return false;
        }

        private int getHeightAtRadius(float pRadius) {
            return (int)getDripstoneHeight((double)pRadius, (double)this.radius, this.scale, this.bluntness);
        }

        void placeBlocks(WorldGenLevel pLevel, RandomSource pRandom, LargeBlackIceFeature.WindOffsetter pWindOffsetter) {
            for(int $$3 = -this.radius; $$3 <= this.radius; ++$$3) {
                for(int $$4 = -this.radius; $$4 <= this.radius; ++$$4) {
                    float $$5 = Mth.sqrt((float)($$3 * $$3 + $$4 * $$4));
                    if (!($$5 > (float)this.radius)) {
                        int $$6 = this.getHeightAtRadius($$5);
                        if ($$6 > 0) {
                            if ((double)pRandom.nextFloat() < 0.2) {
                                $$6 = (int)((float)$$6 * Mth.randomBetween(pRandom, 0.8F, 1.0F));
                            }

                            BlockPos.MutableBlockPos $$7 = this.root.offset($$3, 0, $$4).mutable();
                            boolean $$8 = false;
                            int $$9 = this.pointingUp ? pLevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, $$7.getX(), $$7.getZ()) : Integer.MAX_VALUE;

                            for(int $$10 = 0; $$10 < $$6 && $$7.getY() < $$9; ++$$10) {
                                BlockPos $$11 = pWindOffsetter.offset($$7);
                                if (isEmptyOrWaterOrLava(pLevel, $$11)) {
                                    $$8 = true;
                                    Block $$12 = JNEBlocks.BLACK_ICE.get();
                                    pLevel.setBlock($$11, $$12.defaultBlockState(), 2);
                                } else if ($$8 && pLevel.getBlockState($$11).is(BlockTags.BASE_STONE_OVERWORLD)) {
                                    break;
                                }

                                $$7.move(this.pointingUp ? Direction.UP : Direction.DOWN);
                            }
                        }
                    }
                }
            }

        }

        boolean isSuitableForWind(LargeDripstoneConfiguration pConfig) {
            return this.radius >= pConfig.minRadiusForWind && this.bluntness >= (double)pConfig.minBluntnessForWind;
        }
    }

    private static final class WindOffsetter {
        private final int originY;
        @Nullable
        private final Vec3 windSpeed;

        WindOffsetter(int pOriginY, RandomSource pRandom, FloatProvider pMagnitude) {
            this.originY = pOriginY;
            float $$3 = pMagnitude.sample(pRandom);
            float $$4 = Mth.randomBetween(pRandom, 0.0F, 3.1415927F);
            this.windSpeed = new Vec3((double)(Mth.cos($$4) * $$3), 0.0, (double)(Mth.sin($$4) * $$3));
        }

        private WindOffsetter() {
            this.originY = 0;
            this.windSpeed = null;
        }

        static LargeBlackIceFeature.WindOffsetter noWind() {
            return new LargeBlackIceFeature.WindOffsetter();
        }

        BlockPos offset(BlockPos pPos) {
            if (this.windSpeed == null) {
                return pPos;
            } else {
                int $$1 = this.originY - pPos.getY();
                Vec3 $$2 = this.windSpeed.scale((double)$$1);
                return pPos.offset(Mth.floor($$2.x), 0, Mth.floor($$2.z));
            }
        }
    }

    protected static boolean isEmptyOrWater(LevelAccessor pLevel, BlockPos pPos) {
        return pLevel.isStateAtPosition(pPos, DripstoneUtils::isEmptyOrWater);
    }

    protected static double getDripstoneHeight(double pRadius, double pMaxRadius, double pScale, double pMinRadius) {
        if (pRadius < pMinRadius) {
            pRadius = pMinRadius;
        }

        double $$5 = pRadius / pMaxRadius * 0.384;
        double $$6 = 0.75 * Math.pow($$5, 1.3333333333333333);
        double $$7 = Math.pow($$5, 0.6666666666666666);
        double $$8 = 0.3333333333333333 * Math.log($$5);
        double $$9 = pScale * ($$6 - $$7 - $$8);
        $$9 = Math.max($$9, 0.0);
        return $$9 / 0.384 * pMaxRadius;
    }

    protected static boolean isEmptyOrWaterOrLava(LevelAccessor pLevel, BlockPos pPos) {
        return pLevel.isStateAtPosition(pPos, DripstoneUtils::isEmptyOrWaterOrLava);
    }

    protected static boolean isCircleMostlyEmbeddedInStone(WorldGenLevel pLevel, BlockPos pPos, int pRadius) {
        if (isEmptyOrWaterOrLava(pLevel, pPos)) {
            return false;
        } else {
            float $$4 = 6.0F / (float)pRadius;

            for(float $$5 = 0.0F; $$5 < 6.2831855F; $$5 += $$4) {
                int $$6 = (int)(Mth.cos($$5) * (float)pRadius);
                int $$7 = (int)(Mth.sin($$5) * (float)pRadius);
                if (isEmptyOrWaterOrLava(pLevel, pPos.offset($$6, 0, $$7))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isBlackIceBaseOrLava(BlockState pState) {
        return isBlackIceBase(pState) || pState.is(Blocks.LAVA);
    }

    public static boolean isBlackIceBase(BlockState pState) {
        return pState.is(JNEBlocks.BLACK_ICE.get()) || pState.is(JNETags.Blocks.BLACK_ICE_REPLACEABLE);
    }
}
