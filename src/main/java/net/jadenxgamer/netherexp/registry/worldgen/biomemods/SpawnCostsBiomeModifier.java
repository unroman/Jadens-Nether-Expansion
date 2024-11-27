package net.jadenxgamer.netherexp.registry.worldgen.biomemods;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.registry.entity.JNEEntityType;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public record SpawnCostsBiomeModifier(HolderSet<Biome> biomes, double energyBudget, double charge, EntityType<?> entityType) implements BiomeModifier {
    private static final RegistryObject<Codec<? extends BiomeModifier>> CODEC = RegistryObject.create(new ResourceLocation(NetherExp.MOD_ID, "spawn_costs"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, NetherExp.MOD_ID);
    
    @Override
    public void modify(Holder<Biome> biomes, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && this.biomes.contains(biomes)) {
            builder.getMobSpawnSettings().addMobCharge(entityType, charge, energyBudget);
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return CODEC.get();
    }

    public static Codec<SpawnCostsBiomeModifier> createCodec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                Biome.LIST_CODEC.fieldOf("biomes").forGetter(SpawnCostsBiomeModifier::biomes),
                Codec.DOUBLE.fieldOf("energy_budget").forGetter(SpawnCostsBiomeModifier::energyBudget),
                Codec.DOUBLE.fieldOf("charge").forGetter(SpawnCostsBiomeModifier::charge),
                ForgeRegistries.ENTITY_TYPES.getCodec().fieldOf("entity_type").forGetter(SpawnCostsBiomeModifier::entityType)
        ).apply(instance, SpawnCostsBiomeModifier::new));
    }
}
