package net.jadenxgamer.netherexp.registry.effect;

import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.compat.CompatUtil;
import net.jadenxgamer.netherexp.registry.effect.custom.FogSightEffect;
import net.jadenxgamer.netherexp.registry.effect.custom.ImmunityEffect;
import net.jadenxgamer.netherexp.registry.effect.custom.IncurableEffect;
import net.jadenxgamer.netherexp.registry.effect.custom.UnboundedSpeedEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class JNEMobEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, NetherExp.MOD_ID);

    public static final RegistryObject<MobEffect> FOGSIGHT = MOB_EFFECTS.register("fogsight", () ->
            new FogSightEffect(MobEffectCategory.BENEFICIAL, 6497843));

    public static final RegistryObject<MobEffect> UNBOUNDED_SPEED = MOB_EFFECTS.register("unbounded_speed", () ->
            new UnboundedSpeedEffect(MobEffectCategory.BENEFICIAL, 1787717));

    public static final RegistryObject<MobEffect> BETRAYED = MOB_EFFECTS.register("betrayed", () ->
            new IncurableEffect(MobEffectCategory.NEUTRAL, 11730944));

    // IMMUNITY EFFECTS

    public static final RegistryObject<MobEffect> SPEED_IMMUNITY = MOB_EFFECTS.register("speed_immunity", () ->
            new ImmunityEffect(MobEffectCategory.HARMFUL, 3402751, new ResourceLocation("minecraft", "speed")));

    public static final RegistryObject<MobEffect> SLOWNESS_IMMUNITY = MOB_EFFECTS.register("slowness_immunity", () ->
            new ImmunityEffect(MobEffectCategory.BENEFICIAL, 9154528, new ResourceLocation("minecraft", "slowness")));

    public static final RegistryObject<MobEffect> STRENGTH_IMMUNITY = MOB_EFFECTS.register("strength_immunity", () ->
            new ImmunityEffect(MobEffectCategory.HARMFUL, 16762624, new ResourceLocation("minecraft", "strength")));

    public static final RegistryObject<MobEffect> JUMP_BOOST_IMMUNITY = MOB_EFFECTS.register("jump_boost_immunity", () ->
            new ImmunityEffect(MobEffectCategory.HARMFUL, 16646020, new ResourceLocation("minecraft", "jump_boost")));

    public static final RegistryObject<MobEffect> REGENERATION_IMMUNITY = MOB_EFFECTS.register("regeneration_immunity", () ->
            new ImmunityEffect(MobEffectCategory.HARMFUL, 13458603, new ResourceLocation("minecraft", "regeneration")));

    public static final RegistryObject<MobEffect> FIRE_RESISTANCE_IMMUNITY = MOB_EFFECTS.register("fire_resistance_immunity", () ->
            new ImmunityEffect(MobEffectCategory.HARMFUL, 16750848, new ResourceLocation("minecraft", "fire_resistance")));

    public static final RegistryObject<MobEffect> WATER_BREATHING_IMMUNITY = MOB_EFFECTS.register("water_breathing_immunity", () ->
            new ImmunityEffect(MobEffectCategory.HARMFUL, 10017472, new ResourceLocation("minecraft", "water_breathing")));

    public static final RegistryObject<MobEffect> INVISIBILITY_IMMUNITY = MOB_EFFECTS.register("invisibility_immunity", () ->
            new ImmunityEffect(MobEffectCategory.HARMFUL, 16185078, new ResourceLocation("minecraft", "invisibility")));

    public static final RegistryObject<MobEffect> WEAKNESS_IMMUNITY = MOB_EFFECTS.register("weakness_immunity", () ->
            new ImmunityEffect(MobEffectCategory.BENEFICIAL, 4738376, new ResourceLocation("minecraft", "weakness")));

    public static final RegistryObject<MobEffect> POISON_IMMUNITY = MOB_EFFECTS.register("poison_immunity", () ->
            new ImmunityEffect(MobEffectCategory.BENEFICIAL, 8889187, new ResourceLocation("minecraft", "poison")));

    public static final RegistryObject<MobEffect> RESISTANCE_IMMUNITY = MOB_EFFECTS.register("resistance_immunity", () ->
            new ImmunityEffect(MobEffectCategory.HARMFUL, 9520880, new ResourceLocation("minecraft", "resistance")));

    public static final RegistryObject<MobEffect> ABSORPTION_IMMUNITY = MOB_EFFECTS.register("absorption_immunity", () ->
            new ImmunityEffect(MobEffectCategory.HARMFUL, 2445989, new ResourceLocation("minecraft", "absorption")));

    public static final RegistryObject<MobEffect> HASTE_IMMUNITY = MOB_EFFECTS.register("haste_immunity", () ->
            new ImmunityEffect(MobEffectCategory.HARMFUL, 14270531, new ResourceLocation("minecraft", "haste")));

    public static final RegistryObject<MobEffect> MINING_FATIGUE_IMMUNITY = MOB_EFFECTS.register("mining_fatigue_immunity", () ->
            new ImmunityEffect(MobEffectCategory.BENEFICIAL, 4866583, new ResourceLocation("minecraft", "mining_fatigue")));

    public static final RegistryObject<MobEffect> DARKNESS_IMMUNITY = MOB_EFFECTS.register("darkness_immunity", () ->
            new ImmunityEffect(MobEffectCategory.BENEFICIAL, 2696993, new ResourceLocation("minecraft", "darkness")));

    public static final RegistryObject<MobEffect> LEVITATION_IMMUNITY = MOB_EFFECTS.register("levitation_immunity", () ->
            new ImmunityEffect(MobEffectCategory.BENEFICIAL, 13565951, new ResourceLocation("minecraft", "levitation")));

    public static final RegistryObject<MobEffect> HUNGER_IMMUNITY = MOB_EFFECTS.register("hunger_immunity", () ->
            new ImmunityEffect(MobEffectCategory.BENEFICIAL, 5797459, new ResourceLocation("minecraft", "hunger")));

    public static final RegistryObject<MobEffect> WITHER_IMMUNITY = MOB_EFFECTS.register("wither_immunity", () ->
            new ImmunityEffect(MobEffectCategory.BENEFICIAL, 7561558, new ResourceLocation("minecraft", "wither")));

    public static final RegistryObject<MobEffect> LUCK_IMMUNITY = MOB_EFFECTS.register("luck_immunity", () ->
            new ImmunityEffect(MobEffectCategory.HARMFUL, 5882118, new ResourceLocation("minecraft", "luck")));

    public static final RegistryObject<MobEffect> UNLUCK_IMMUNITY = MOB_EFFECTS.register("unluck_immunity", () ->
            new ImmunityEffect(MobEffectCategory.BENEFICIAL, 12624973, new ResourceLocation("minecraft", "unluck")));

    /**
     * MOD COMPAT
     */

    public static final RegistryObject<MobEffect> BRAIN_DAMAGE_IMMUNITY = MOB_EFFECTS.register("brain_damage_immunity", () ->
            new ImmunityEffect(MobEffectCategory.BENEFICIAL, 0x6e66a4, new ResourceLocation(CompatUtil.OREGANIZED, "stunning")));

    public static void init(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
