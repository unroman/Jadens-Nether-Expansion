package net.jadenxgamer.netherexp.mixin.item;

import net.jadenxgamer.netherexp.registry.item.custom.NetheriteArtifactItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(
            method = "getCraftingRemainingItem",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void netherexp$getCraftingRemainingItem(CallbackInfoReturnable<Item> cir) {
        // makes this item non-consumable
        Item item = ((Item) (Object) this);
        if (item instanceof NetheriteArtifactItem) {
            cir.setReturnValue(item);
        }
    }
}
