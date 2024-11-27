package net.jadenxgamer.netherexp.registry.fluid.custom;

import net.jadenxgamer.netherexp.registry.misc_registry.JNESoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class EctoplasmFluidType extends FluidType {

    private static final ResourceLocation STILL = new ResourceLocation("netherexp:block/ectoplasm_still");
    private static final ResourceLocation FLOWING = new ResourceLocation("netherexp:block/ectoplasm_flow");
    private static final ResourceLocation OVERLAY = new ResourceLocation("netherexp:textures/block/ectoplasm_overlay.png");

    public EctoplasmFluidType(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return FLOWING;
            }
            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return OVERLAY;
            }
        });
    }

    @Override
    public boolean move(FluidState state, LivingEntity entity, Vec3 movementVector, double gravity) {
        return true;
    }

    @Override
    public void onVaporize(@Nullable Player player, Level level, BlockPos pos, FluidStack stack) {
        level.playSound(player, pos, JNESoundEvents.ECTOPLASM_FREEZE.get(), SoundSource.BLOCKS, 1.0f, 1.0f);

        for(int l = 0; l < 8; ++l) {
            level.addAlwaysVisibleParticle(ParticleTypes.SOUL, (double)pos.getX() + Math.random(), (double)pos.getY() + Math.random(), (double)pos.getZ() + Math.random(), 0.0, 0.0, 0.0);
        }
    }
}
