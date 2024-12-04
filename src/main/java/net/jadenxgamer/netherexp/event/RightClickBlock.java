package net.jadenxgamer.netherexp.event;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record RightClickBlock(Item interactionItem, Block fromBlock, Block toBlock, AfterUse afterUse, @Nullable SoundEvent sound, @Nullable ParticleOptions particle) {

    public static final Codec<RightClickBlock> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ForgeRegistries.ITEMS.getCodec().fieldOf("interaction_item").forGetter(RightClickBlock::interactionItem),
            ForgeRegistries.BLOCKS.getCodec().fieldOf("from_block").forGetter(RightClickBlock::fromBlock),
            ForgeRegistries.BLOCKS.getCodec().fieldOf("to_block").forGetter(RightClickBlock::toBlock),
            AfterUse.CODEC.optionalFieldOf("behaviors.after_use", AfterUse.NONE).forGetter(RightClickBlock::afterUse),
            ForgeRegistries.SOUND_EVENTS.getCodec().optionalFieldOf("behaviors.sound").forGetter(o -> Optional.ofNullable(o.sound())),
            ParticleTypes.CODEC.optionalFieldOf("behaviors.particle").forGetter(o -> Optional.ofNullable(o.particle()))
    ).apply(instance, (item, block, newBlock, afterUse, sound, particle) -> new RightClickBlock(item, block, newBlock, afterUse, sound.orElse(null), particle.orElse(null))));

    public void transform(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        ItemStack stack = event.getItemStack();
        boolean success = false;

        if (stack.is(interactionItem) && state.is(fromBlock)) {
            level.setBlock(pos, toBlock.defaultBlockState(), Block.UPDATE_ALL);
            success = true;
        }

        if (success) {
            switch (afterUse) {
                case SHRINK -> {
                    if (!player.isCreative()) stack.shrink(1);
                }
                case BREAK -> {
                    if (!player.isCreative()) stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
                }
                default -> {}
            }

            if (sound != null) {
                level.playSound(player, player.getX(), player.getY(), player.getZ(), sound, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            if (particle != null) {
                particle(level, pos.below(), particle);
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    private static void particle(Level level, BlockPos pos, ParticleOptions particle) {
        RandomSource random = level.random;
        for (Direction direction : Direction.values()) {
            BlockPos blockPos = pos.relative(direction);
            if (!level.getBlockState(blockPos).isCollisionShapeFullBlock(level, blockPos) && random.nextInt(120) == 0) {
                Direction.Axis axis = direction.getAxis();
                double e = axis == Direction.Axis.X ? 0.5 + 0.5625 * direction.getStepX() : random.nextFloat();
                double f = axis == Direction.Axis.Y ? 0.5 + 0.5625 * direction.getStepY() : random.nextFloat();
                double g = axis == Direction.Axis.Z ? 0.5 + 0.5625 * direction.getStepZ() : random.nextFloat();
                level.addParticle(particle, pos.getX() + e, pos.getY() + f, pos.getZ() + g, 0.0, 0.0, 0.0);
            }
        }
    }

    private enum AfterUse {
        NONE, SHRINK, BREAK;

        public static final Codec<AfterUse> CODEC = Codec.STRING.xmap(
                value -> switch (value) {
                    case "shrink" -> SHRINK;
                    case "break" -> BREAK;
                    default -> NONE;
                },
                afterUse -> switch (afterUse) {
                    case SHRINK -> "shrink";
                    case BREAK -> "break";
                    default -> "none";
                }
        );
    }
}