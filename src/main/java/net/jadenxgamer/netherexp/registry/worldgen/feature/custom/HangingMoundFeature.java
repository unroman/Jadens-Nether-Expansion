package net.jadenxgamer.netherexp.registry.worldgen.feature.custom;

import com.mojang.serialization.Codec;
import net.jadenxgamer.netherexp.registry.misc_registry.JNETags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class HangingMoundFeature extends Feature<NoneFeatureConfiguration> {

    public HangingMoundFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    //TODO: Merge it with MoundFeature later
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin().above(2);
        RandomSource random = context.random();
        BlockState state = level.getBlockState(context.origin().above());
        if (state.is(JNETags.Blocks.MOUND_BLOCKS)) {
            int height = 3 + random.nextInt(4);
            placeMound(level, origin, height, state, random);


            int extraMound = 1 + random.nextInt(3);
            for (int i = 0; i < extraMound; i++) {
                int x = (random.nextBoolean() ? 1 : -1) * 3;
                int z = (random.nextBoolean() ? 1 : -1) * 3;
                int extraMoundHeight = 3 + random.nextInt(4);
                placeMound(level, origin.offset(x, 0, z), extraMoundHeight, state, random);
            }

            return true;
        } else return false;
    }

    private void placeMound(WorldGenLevel level, BlockPos origin, int height, BlockState state, RandomSource random) {
        int radius = 3;

        for (int y = 0; y > -height; y--) {
            BlockPos center = origin.below(-y);

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    double distanceSquared = (x + 0.5) * (x + 0.5) + (z + 0.5) * (z + 0.5);

                    if (distanceSquared <= radius * radius) {
                        BlockPos pos = center.offset(x, 0, z);
                        if (level.getBlockState(pos.above()).isSolid() && level.getBlockState(pos).canBeReplaced()) {
                            level.setBlock(pos, state, 2);
                        }
                    }
                }
            }
        }

        int terrainX = random.nextInt(9) - 4;
        int terrainZ = random.nextInt(9) - 4;

        BlockPos circleCenter = origin.below(height).offset(terrainX, 0, terrainZ);

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                double distanceSquared = (x + 0.5) * (x + 0.5) + (z + 0.5) * (z + 0.5);

                if (distanceSquared <= radius * radius) {
                    BlockPos pos = circleCenter.offset(x, 0, z);
                    if (level.getBlockState(pos.above()).is(JNETags.Blocks.MOUND_BLOCKS) && level.getBlockState(pos).canBeReplaced()) {
                        level.setBlock(pos, state, 2);
                    }
                }
            }
        }
    }
}
