package net.jadenxgamer.netherexp.registry.block.custom;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class LogBlock extends RotatedPillarBlock {

    private final Supplier<Block> stripped;

    public LogBlock(Properties pProperties, Supplier<Block> stripped) {
        super(pProperties);
        this.stripped = stripped;
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        ItemStack stack = context.getItemInHand();
        if (stack.canPerformAction(toolAction)) {
            if (toolAction == ToolActions.AXE_STRIP) {
                return stripped.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
            }
        }

        return super.getToolModifiedState(state, context, toolAction, simulate);
    }
}
