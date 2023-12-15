package net.jadenxgamer.netherexp.registry.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.registry.config.other.blocks.*;
import net.jadenxgamer.netherexp.registry.config.other.sounds.BlockSoundsConfigs;
import net.jadenxgamer.netherexp.registry.config.other.worldgen.SoulSandValleyConfigs;

@Config(name = NetherExp.MOD_ID)
@Config.Gui.Background("netherexp:textures/block/pyrite_nether_bricks_left.png")
public class NetherExpConfigs implements ConfigData {

    // BLOCKS //

    @ConfigEntry.Gui.CollapsibleObject()
    public Blocks blocks = new Blocks();

    public static class Blocks {
        @ConfigEntry.Gui.CollapsibleObject()
        @ConfigEntry.Gui.Tooltip
        public DecayableConfigs decayableConfigs = new DecayableConfigs();

        @ConfigEntry.Gui.CollapsibleObject()
        @ConfigEntry.Gui.Tooltip
        public PathBlockConfigs pathBlockConfigs = new PathBlockConfigs();

        @ConfigEntry.Gui.CollapsibleObject()
        @ConfigEntry.Gui.Tooltip
        public WitherBoneBlockConfigs witherBoneBlockConfigs = new WitherBoneBlockConfigs();

        @ConfigEntry.Gui.CollapsibleObject()
        @ConfigEntry.Gui.Tooltip
        public FossilOreConfigs fossilOreConfigs = new FossilOreConfigs();

        @ConfigEntry.Gui.CollapsibleObject()
        @ConfigEntry.Gui.Tooltip
        public SoulGlassConfigs soulGlassConfigs = new SoulGlassConfigs();

        @ConfigEntry.Gui.CollapsibleObject()
        @ConfigEntry.Gui.Tooltip
        public GeyserConfigs geyserConfigs = new GeyserConfigs();

        @ConfigEntry.Gui.CollapsibleObject()
        @ConfigEntry.Gui.Tooltip
        public RenewableConfigs renewableConfigs = new RenewableConfigs();
    }

    // SOUNDS //

    @ConfigEntry.Gui.CollapsibleObject()
    public Sounds sounds = new Sounds();

    public static class Sounds {
        @ConfigEntry.Gui.CollapsibleObject()
        @ConfigEntry.Gui.Tooltip
        public BlockSoundsConfigs blockSoundsConfigs = new BlockSoundsConfigs();
    }

    // VISUAL EFFECTS //

    @ConfigEntry.Gui.CollapsibleObject()
    public VisualEffects visualeffects = new VisualEffects();

    public static class VisualEffects {
        @ConfigEntry.Gui.Tooltip
        public boolean ectoplasm_particles = true;

        @ConfigEntry.Gui.Tooltip
        public boolean black_ice_particles = true;
    }

    // WORLDGEN //

    @ConfigEntry.Gui.CollapsibleObject()
    public WorldGen worldgen = new WorldGen();

    public static class WorldGen {
        @ConfigEntry.Gui.RequiresRestart
        @ConfigEntry.Gui.Tooltip
        public int wrath_region_weight = 5;

        @ConfigEntry.Gui.CollapsibleObject()
        @ConfigEntry.Gui.Tooltip
        public SoulSandValleyConfigs soulSandValleyConfigs = new SoulSandValleyConfigs();
    }
}