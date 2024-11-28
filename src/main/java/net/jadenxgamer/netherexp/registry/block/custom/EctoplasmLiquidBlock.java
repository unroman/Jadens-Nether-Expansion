package net.jadenxgamer.netherexp.registry.block.custom;

import com.google.common.collect.Maps;
import net.jadenxgamer.netherexp.config.JNEConfigs;
import net.jadenxgamer.netherexp.registry.block.JNEBlocks;
import net.jadenxgamer.netherexp.registry.misc_registry.JNESoundEvents;
import net.jadenxgamer.netherexp.registry.particle.JNEParticleTypes;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class EctoplasmLiquidBlock extends LiquidBlock {
    /*
     * Based on Alex's Caves AcidBlock.java code with a few tweaks
     * https://github.com/AlexModGuy/AlexsCaves/blob/main/src/main/java/com/github/alexmodguy/alexscaves/server/block/AcidBlock.java
     * https://www.curseforge.com/minecraft/mc-mods/alexs-caves
     */

    private static Map<Block, Block> FREEZES;

    public EctoplasmLiquidBlock(RegistryObject<FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean bl) {
        super.onPlace(state, level, pos, oldState, bl);
        if (JNEConfigs.ECTOPLASM_RUSTS_NETHERITE.get()) {
            this.checkFreeze(level, pos);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        BlockPos abovePos = pos.above();
        if (state.getFluidState().isSource() && !level.getBlockState(abovePos).isSolidRender(level, abovePos) && JNEConfigs.ENABLE_ECTOPLASM_PARTICLES.get()) {
            if (random.nextInt(55) == 0) {
                double d = (double) pos.getX() + random.nextDouble();
                double e = (double) pos.getY() + 1.0;
                double f = (double) pos.getZ() + random.nextDouble();
                level.addParticle(JNEParticleTypes.ECTORAYS.get(), d, e, f, 0.0, -0.03, 0.0);
            }
            if (random.nextInt(18) == 0) {
                double d = (double) pos.getX() + random.nextDouble();
                double e = (double) pos.getY() + 1.0;
                double f = (double) pos.getZ() + random.nextDouble();
                level.addParticle(JNEParticleTypes.ECTOPLASMA.get(), d, e, f, 0.0, 0.0, 0.0);
            }
            if (random.nextInt(600) == 0 && JNEConfigs.ENABLE_ECTOPLASM_SOUNDS.get()) {
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), JNESoundEvents.ECTOPLASM_WHISPERING.get(), SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }
        }
    }
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean bl) {
        super.neighborChanged(state, level, pos, block, fromPos, bl);
        if (JNEConfigs.ECTOPLASM_RUSTS_NETHERITE.get()) {
            this.checkFreeze(level, pos);
        }
    }

    public void checkFreeze(Level level, BlockPos pos) {
        initFreeze();
        Direction[] faces = Direction.values();
        for (Direction direction : faces) {
            BlockPos offset = pos.offset(direction.getNormal());
            BlockState oldState = level.getBlockState(offset);
            if (FREEZES.containsKey(oldState.getBlock())) {
                BlockState newState = FREEZES.get(oldState.getBlock()).defaultBlockState();
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), JNESoundEvents.SOUL_SLATE_SOLIDIFYING.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                for (Property property : oldState.getProperties()) {
                    newState = newState.hasProperty(property) ? newState.setValue(property, oldState.getValue(property)) : newState;
                }
                level.setBlock(offset, newState, 2);
            }
        }
    }

    private static void initFreeze() {
        if (FREEZES != null) {
            return;
        }
        FREEZES = Util.make(Maps.newHashMap(), (map) -> {
            map.put(JNEBlocks.NETHERITE_PLATED_BLOCK.get(), JNEBlocks.RUSTY_NETHERITE_PLATED_BLOCK.get());
            map.put(JNEBlocks.NETHERITE_GRATE.get(), JNEBlocks.RUSTY_NETHERITE_GRATE.get());
            map.put(JNEBlocks.CUT_NETHERITE_BLOCK.get(), JNEBlocks.RUSTY_CUT_NETHERITE_BLOCK.get());
            map.put(JNEBlocks.CUT_NETHERITE_SLAB.get(), JNEBlocks.RUSTY_CUT_NETHERITE_SLAB.get());
            map.put(JNEBlocks.CUT_NETHERITE_STAIRS.get(), JNEBlocks.RUSTY_CUT_NETHERITE_STAIRS.get());
            map.put(JNEBlocks.CUT_NETHERITE_PILLAR.get(), JNEBlocks.RUSTY_CUT_NETHERITE_PILLAR.get());
        });
    }
}
