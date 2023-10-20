package net.jadenxgamer.netherexp.mixin.block;

import net.jadenxgamer.netherexp.registry.item.ModItems;
import net.jadenxgamer.netherexp.registry.particle.ModParticles;
import net.jadenxgamer.netherexp.registry.sound.ModSoundEvents;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TwistingVinesBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TwistingVinesBlock.class)
public abstract class TwistingVinesMixin
extends AbstractPlantStemBlock {

    private static final BooleanProperty BUDDING = BooleanProperty.of("budding");

    public TwistingVinesMixin(Settings settings, Direction growthDirection, VoxelShape outlineShape, boolean tickWater, double growthChance) {
        super(settings, growthDirection, outlineShape, tickWater, growthChance);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        boolean budding = state.get(BUDDING);
        float f = random.nextFloat();
        double x = (double)pos.getX() + random.nextDouble();
        double y = (double)pos.getY() + 0.8;
        double z = (double)pos.getZ() + random.nextDouble();
        if (!budding && f < 0.3) {
            world.addParticle(ModParticles.RISING_SHROOMNIGHT, x, y, z, MathHelper.nextDouble(random, -0.02, 0.02), 0.08, MathHelper.nextDouble(random, -0.02, 0.02));
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        boolean budding = state.get(BUDDING);
        boolean bl = false;
        if (budding) {
            if (itemStack.isOf(ModItems.NIGHTSPORES)) {
                world.playSound(player, pos.getX(), pos.getY(), pos.getZ(), ModSoundEvents.LIGHTSPORES_APPLY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.setBlockState(pos,state.cycle(BUDDING), Block.NOTIFY_LISTENERS);
                if (!player.isCreative()) {
                    itemStack.decrement(1);
                }
                sporeParticles(world, pos);
                bl = true;
            }
        }
        else {
           if (itemStack.isOf(Items.SHEARS)) {
               dropNight(world, pos);
               world.playSound(player, pos, ModSoundEvents.LIGHTSPORES_SHEAR, SoundCategory.BLOCKS, 1.0f, 1.0f);
               world.setBlockState(pos, state.cycle(BUDDING), Block.NOTIFY_LISTENERS);
               world.emitGameEvent(player, GameEvent.SHEAR, pos);
               itemStack.damage(1, player, p -> p.sendToolBreakStatus(hand));
               bl = true;
            }
        }
        if (!world.isClient() && bl) {
            player.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem()));
        }
        if (bl) {
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }

    private static void dropNight(World world, BlockPos pos) {
        dropStack(world, pos, new ItemStack(ModItems.NIGHTSPORES, 1));
    }

    private static void sporeParticles(World world, BlockPos pos) {
        Random random = world.random;
        for (Direction direction : Direction.values()) {
            BlockPos blockPos = pos.offset(direction);
            if (world.getBlockState(blockPos).isOpaqueFullCube(world, blockPos)) continue;
            Direction.Axis axis = direction.getAxis();
            double e = axis == Direction.Axis.X ? 0.5 + 0.5625 * (double) direction.getOffsetX() : (double) random.nextFloat();
            double f = axis == Direction.Axis.Y ? 0.5 + 0.5625 * (double) direction.getOffsetY() : (double) random.nextFloat();
            double g = axis == Direction.Axis.Z ? 0.5 + 0.5625 * (double) direction.getOffsetZ() : (double) random.nextFloat();
            world.addParticle(ModParticles.FALLING_SHROOMNIGHT, (double)pos.getX() + e, (double)pos.getY() + f, (double)pos.getZ() + g, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BUDDING, AGE);
    }
}