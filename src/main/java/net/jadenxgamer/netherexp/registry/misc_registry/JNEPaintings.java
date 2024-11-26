package net.jadenxgamer.netherexp.registry.misc_registry;

import net.jadenxgamer.netherexp.NetherExp;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class JNEPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANT = DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, NetherExp.MOD_ID);

    public static final RegistryObject<PaintingVariant> HOUSE = PAINTING_VARIANT.register("house", () ->
            new PaintingVariant(64, 32));

    public static void init(IEventBus eventBus) {
        PAINTING_VARIANT.register(eventBus);
    }
}
