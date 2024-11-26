package net.jadenxgamer.netherexp.registry.fluid;

import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.config.JNEConfigs;
import net.jadenxgamer.netherexp.registry.fluid.custom.EctoplasmFluidType;
import net.jadenxgamer.netherexp.registry.item.JNEItems;
import net.jadenxgamer.netherexp.registry.misc_registry.JNESoundEvents;
import net.jadenxgamer.netherexp.registry.particle.JNEParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class JNEFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, NetherExp.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, NetherExp.MOD_ID);

    private static ForgeFlowingFluid.Properties ectoplasmProperties() {
        return new ForgeFlowingFluid.Properties(ECTOPLASM_TYPE, ECTOPLASM_SOURCE, ECTOPLASM_FLOWING).bucket(JNEItems.ECTOPLASM_BUCKET);
    }

    public static final RegistryObject<FluidType> ECTOPLASM_TYPE = FLUID_TYPES.register("ectoplasm", () ->
        new EctoplasmFluidType(FluidType.Properties.create().lightLevel(7).density(0).viscosity(0)
                .canPushEntity(false).canSwim(false).canConvertToSource(true).canDrown(false)
                .pathType(BlockPathTypes.WATER).supportsBoating(false).temperature(-196)
                .canExtinguish(false).adjacentPathType(BlockPathTypes.DANGER_OTHER)
                .canHydrate(false)));
    public static final RegistryObject<FlowingFluid> ECTOPLASM_SOURCE = FLUIDS.register("ectoplasm", () ->
            new ForgeFlowingFluid.Source(ectoplasmProperties()));
    public static final RegistryObject<FlowingFluid> ECTOPLASM_FLOWING = FLUIDS.register("flowing_ectoplasm", () ->
            new ForgeFlowingFluid.Flowing(ectoplasmProperties()));

    private static void ectoplasmParticles(Level level, BlockPos pos, RandomSource random) {
        BlockPos abovePos = pos.above();
        if (level.getBlockState(abovePos).isAir() && !level.getBlockState(abovePos).isSolidRender(level, abovePos) && JNEConfigs.ENABLE_ECTOPLASM_PARTICLES.get()) {
            if (random.nextInt(55) == 0) {
                double d = (double) pos.getX() + random.nextDouble();
                double e = (double) pos.getY() + 1.0;
                double f = (double) pos.getZ() + random.nextDouble();
                level.addParticle(JNEParticleTypes.ECTORAYS.get(), d, e, f, 0.0, -0.03, 0.0);
            }
            if (random.nextInt(18) == 0 && JNEConfigs.ENABLE_ECTOPLASM_PARTICLES.get()) {
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

    public static void init(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);
    }
}
