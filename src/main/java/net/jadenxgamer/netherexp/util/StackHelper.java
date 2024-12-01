package net.jadenxgamer.netherexp.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class StackHelper {

    public static Player getEntityHoldingStack(ItemStack stack) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            if (player.getMainHandItem() == stack) {
                return player;
            } else if (player.getOffhandItem() == stack) {
                return player;
            }
        }
        return null;
    }
}
