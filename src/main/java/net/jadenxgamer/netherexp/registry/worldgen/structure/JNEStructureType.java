package net.jadenxgamer.netherexp.registry.worldgen.structure;

import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.registry.worldgen.structure.custom.GiantJigsawStructure;
import net.jadenxgamer.netherexp.registry.worldgen.structure.custom.MegaFossilStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class JNEStructureType {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE = DeferredRegister.create(Registries.STRUCTURE_TYPE, NetherExp.MOD_ID);

    public static final RegistryObject<StructureType<MegaFossilStructure>> MEGA_FOSSIL = STRUCTURE_TYPE.register("mega_fossil", () ->
            () -> MegaFossilStructure.CODEC);

    public static final RegistryObject<StructureType<GiantJigsawStructure>> GIANT_JIGSAW = STRUCTURE_TYPE.register("giant_jigsaw", () ->
            () -> GiantJigsawStructure.CODEC);

    public static void init(IEventBus eventBus) {
        STRUCTURE_TYPE.register(eventBus);
    }
}
