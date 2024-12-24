package net.jadenxgamer.netherexp.registry.entity.variants;

import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.biome.Biome;

import java.util.function.IntFunction;

public enum StrayType implements StringRepresentable {
    NORMAL(0, "normal"),
    BLACK(1, "black");

    public static final StringRepresentable.EnumCodec<StrayType> CODEC = StringRepresentable.fromEnum(StrayType::values);
    private static final IntFunction<StrayType> BY_ID = ByIdMap.continuous(StrayType::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    private final int id;
    private final String name;

    StrayType(int pId, String pName) {
        this.id = pId;
        this.name = pName;
    }

    public String getSerializedName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public static StrayType byName(String pName) {
        return CODEC.byName(pName, NORMAL);
    }

    public static StrayType byId(int pIndex) {
        return BY_ID.apply(pIndex);
    }

    public static StrayType byBiome(Holder<Biome> pBiome) {
        return pBiome.is(BiomeTags.IS_NETHER) ? BLACK : NORMAL;
    }
}