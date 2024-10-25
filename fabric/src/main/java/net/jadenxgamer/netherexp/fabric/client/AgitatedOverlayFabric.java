package net.jadenxgamer.netherexp.fabric.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.registry.entity.custom.Stampede;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class AgitatedOverlayFabric implements HudRenderCallback {
    private final Minecraft minecraft = Minecraft.getInstance();

    @Override
    public void onHudRender(GuiGraphics guiGraphics, float tickDelta) {
        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();
        Entity entity = minecraft.cameraEntity;
        if (entity instanceof Player player) {
            if (player.isPassenger() && player.getVehicle() instanceof Stampede stampede) {
                int minute = stampede.getAgitated() / 1200;
                guiGraphics.blit(new ResourceLocation(NetherExp.MOD_ID, "textures/gui/agitated.png"), (width / 2 -6), height - 50, minute * 12, 0, 12, 12);
            }
        }
    }
}
