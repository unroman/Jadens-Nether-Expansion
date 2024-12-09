package net.jadenxgamer.netherexp.mixin.item;

import net.jadenxgamer.netherexp.registry.effect.JNEMobEffects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public class BucketItemMixin {

    @Inject(
            method = "use",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void netherexp$preventPlacementWithBetrayed(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        // prevents fluid placement for players with Betrayed Effect
        if (player != null && player.hasEffect(JNEMobEffects.BETRAYED.get())) {
            cir.setReturnValue(null);
        }
    }
}
