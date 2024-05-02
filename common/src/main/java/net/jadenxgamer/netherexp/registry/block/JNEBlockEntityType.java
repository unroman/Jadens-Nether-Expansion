package net.jadenxgamer.netherexp.registry.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.registry.block.entity.JNEBrushableBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class JNEBlockEntityType {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(NetherExp.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<JNEBrushableBlockEntity>> BRUSHABLE_BLOCK = BLOCK_ENTITY_TYPES.register("brushable_block", () ->
            BlockEntityType.Builder.of(JNEBrushableBlockEntity::new, JNEBlocks.SUSPICIOUS_SOUL_SAND.get()).build(null));

    public static void init() {
        BLOCK_ENTITY_TYPES.register();
    }
}
