package net.jadenxgamer.netherexp.forge;

import net.jadenxgamer.netherexp.mixin.block.ItemPropertiesAccessor;
import net.jadenxgamer.netherexp.registry.block.JNEBlockEntityType;
import net.jadenxgamer.netherexp.registry.block.entity.client.JNEBrushableBlockRenderer;
import net.jadenxgamer.netherexp.registry.entity.JNEEntityType;
import net.jadenxgamer.netherexp.registry.entity.client.*;
import net.jadenxgamer.netherexp.registry.item.JNEItems;
import net.jadenxgamer.netherexp.registry.item.custom.AntidoteItem;
import net.jadenxgamer.netherexp.registry.item.custom.SanctumCompassItem;
import net.jadenxgamer.netherexp.registry.particle.JNEParticleTypes;
import net.jadenxgamer.netherexp.registry.particle.custom.*;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.HugeExplosionParticle;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class NetherExpForgeClient {

    public static void init() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(NetherExpForgeClient::onClientSetup);
        eventBus.addListener(NetherExpForgeClient::renderParticles);
        eventBus.addListener(NetherExpForgeClient::itemTints);
        eventBus.addListener(NetherExpForgeClient::registerGuiOverlays);
        eventBus.addListener(NetherExpForgeClient::registerLayer);
    }

    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
//        event.registerAboveAll("shotgun_temprature", ShotgunTemperatureOverlayForge.HUD);
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(JNEEntityType.APPARITION.get(), ApparitionRenderer::new);
        EntityRenderers.register(JNEEntityType.WISP.get(), WispRenderer::new);
        EntityRenderers.register(JNEEntityType.VESSEL.get(), VesselRenderer::new);
        EntityRenderers.register(JNEEntityType.ECTO_SLAB.get(), EctoSlabRenderer::new);
        EntityRenderers.register(JNEEntityType.BANSHEE.get(), BansheeRenderer::new);
        EntityRenderers.register(JNEEntityType.STAMPEDE.get(), StampedeRenderer::new);
        EntityRenderers.register(JNEEntityType.CARCASS.get(), CarcassRenderer::new);
        EntityRenderers.register(JNEEntityType.SOUL_BULLET.get(), SoulBulletRenderer::new);
        EntityRenderers.register(JNEEntityType.BLOOD_DROP.get(), NoopRenderer::new);
        EntityRenderers.register(JNEEntityType.PHASMO_ARROW.get(), PhasmoArrowRenderer::new);
        EntityRenderers.register(JNEEntityType.MIST_CHARGE.get(), MistChargeRenderer::new);
        EntityRenderers.register(JNEEntityType.GRAVE_CLOUD.get(), NoopRenderer::new);
        EntityRenderers.register(JNEEntityType.WILL_O_WISP.get(), ThrownItemRenderer::new);
        EntityRenderers.register(JNEEntityType.ANTIDOTE.get(), ThrownItemRenderer::new);
        EntityRenderers.register(JNEEntityType.GRENADE_EFFECT_CLOUD.get(), NoopRenderer::new);
        BlockEntityRenderers.register(JNEBlockEntityType.BRUSHABLE_BLOCK.get(), JNEBrushableBlockRenderer::new);
        event.enqueueWork(() ->
                ItemPropertiesAccessor.netherexp$invokeRegisterItemProperties(
                JNEItems.SANCTUM_COMPASS.get(),
                new ResourceLocation("angle"),
                new CompassItemPropertyFunction((level, stack, entity) -> SanctumCompassItem.getStructurePosition(stack.getOrCreateTag()))
        ));
    }

    public static void itemTints(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tint) -> tint > 0 ? -1 : AntidoteItem.getColor(stack), JNEItems.ANTIDOTE.get());
        event.register((stack, tint) -> tint > 0 ? -1 : AntidoteItem.getColor(stack), JNEItems.GRENADE_ANTIDOTE.get());
    }

    public static void renderParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(JNEParticleTypes.ENIGMA_KERNEL.get(), EnigmaKernelParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.FIRE_SPARK.get(), FireSparkParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.SMALL_SOUL_FIRE_FLAME.get(), FlameParticle.SmallFlameProvider::new);
        event.registerSpriteSet(JNEParticleTypes.TREACHEROUS_FLAME.get(), FlameParticle.Provider::new);
        event.registerSpriteSet(JNEParticleTypes.GOLD_GLIMMER.get(), GlimmerParticle.NormalFactory::new);
        event.registerSpriteSet(JNEParticleTypes.REDSTONE_SPARK.get(), GlimmerParticle.NormalFactory::new);
        event.registerSpriteSet(JNEParticleTypes.REDSTONE_EXPLOSION.get(), HugeExplosionParticle.Provider::new);
        event.registerSpecial(JNEParticleTypes.REDSTONE_EXPLOSION_EMITTER.get(), (new RedstoneExplosionEmitterParticle.Factory()));
        event.registerSpriteSet(JNEParticleTypes.FALLING_NETHER_WART.get(), FallingParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.FALLING_WARPED_WART.get(), FallingParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.FALLING_SHROOMLIGHT.get(), FallingParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.FALLING_SHROOMNIGHT.get(), FallingParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.RISING_SHROOMNIGHT.get(), RisingParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.CRIMSON_SMOG.get(), SmogParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.WARPED_SMOG.get(), SmogParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.BLACK_SMOKE.get(), SmogParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.WHITE_SMOKE.get(), SmogParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.RED_SMOKE.get(), SmogParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.SOUL_EMBER.get(), SmallRisingParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.ECTORAYS.get(), EctoraysParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.ECTOPLASMA.get(), EctoplasmaParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.BLACK_AEROSOL.get(), AerosolParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.SWIRL_POP.get(), RisingParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.GRASP_MIST.get(), GraspMistParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.WISP.get(), GlimmerParticle.LongFactory::new);
        event.registerSpriteSet(JNEParticleTypes.COLORED_WISP.get(), GlimmerParticle.ColoredFactory::new);
        event.registerSpriteSet(JNEParticleTypes.MAGMA_CREAM.get(), RisingParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.IMMUNITY_EFFECT.get(), SpellParticle.MobProvider::new);
        event.registerSpriteSet(JNEParticleTypes.FALLING_BLOOD.get(), BloodFallAndLandParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.LANDING_BLOOD.get(), BloodLandParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.CANDLE_BURST.get(), BurstParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.FIREBALL_TRAIL.get(), GlimmerParticle.GhastFireballFactory::new);
        event.registerSpriteSet(JNEParticleTypes.SMALL_FIREBALL_TRAIL.get(), GlimmerParticle.FireballFactory::new);

        // MOD COMPAT
        event.registerSpriteSet(JNEParticleTypes.FALLING_SHROOMBLIGHT.get(), FallingParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.UMBRAL_SMOG.get(), SmogParticle.Factory::new);
        event.registerSpriteSet(JNEParticleTypes.SHALE_SWIRL_POP.get(), RisingParticle.Factory::new);
    }


    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(JNEModelLayers.APPARITION_LAYER, ApparitionModel::createBodyLayer);
        event.registerLayerDefinition(JNEModelLayers.WISP_LAYER, WispModel::createBodyLayer);
        event.registerLayerDefinition(JNEModelLayers.VESSEL_LAYER, VesselModel::createBodyLayer);
        event.registerLayerDefinition(JNEModelLayers.ECTO_SLAB_LAYER, EctoSlabModel::createBodyLayer);
        event.registerLayerDefinition(JNEModelLayers.BANSHEE_LAYER, BansheeModel::createBodyLayer);
        event.registerLayerDefinition(JNEModelLayers.STAMPEDE_LAYER, StampedeModel::createBodyLayer);
        event.registerLayerDefinition(JNEModelLayers.CARCASS_LAYER, CarcassModel::createBodyLayer);
        event.registerLayerDefinition(JNEModelLayers.MIST_CHARGE_LAYER, MistChargeModel::createBodyLayer);
        event.registerLayerDefinition(JNEModelLayers.GHAST_FIREBALL_LAYER, GhastFireBallModel::createBodyLayer);
        event.registerLayerDefinition(JNEModelLayers.FIREBALL_LAYER, FireballModel::createBodyLayer);
    }
}
