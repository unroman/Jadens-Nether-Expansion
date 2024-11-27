package net.jadenxgamer.netherexp.registry.client;

import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.registry.entity.custom.Stampede;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class AgitatedOverlay {
    private static final Minecraft minecraft = Minecraft.getInstance();

    public static final IGuiOverlay HUD = ((gui, guiGraphics, partialTick, width, height) -> {
        Entity entity = minecraft.cameraEntity;
        if (entity instanceof Player player) {
            if (player.isPassenger() && player.getVehicle() instanceof Stampede stampede) {
                int minute = stampede.getAgitated() / 1200;
                guiGraphics.blit(new ResourceLocation(NetherExp.MOD_ID, "textures/gui/agitated.png"), (width / 2 -6), height - 50, minute * 12, 0, 12, 12);
            }
        }
    });
}
