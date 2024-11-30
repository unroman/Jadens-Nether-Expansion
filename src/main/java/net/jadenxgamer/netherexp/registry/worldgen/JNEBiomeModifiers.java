package net.jadenxgamer.netherexp.registry.worldgen;

import com.mojang.serialization.Codec;
import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.registry.worldgen.biome.SpawnCostsBiomeModifier;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class JNEBiomeModifiers {
    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, NetherExp.MOD_ID);

    public static final RegistryObject<Codec<? extends BiomeModifier>> SPAWN_COSTS = BIOME_MODIFIER_SERIALIZERS.register("spawn_costs", SpawnCostsBiomeModifier::createCodec);

    public static void init(IEventBus eventBus) {
        BIOME_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
