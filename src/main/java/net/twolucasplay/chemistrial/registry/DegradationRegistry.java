package net.twolucasplay.chemistrial.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import java.util.Map;

public class DegradationRegistry {
    // Easily add, edit, or remove modifiers right here!
    public static final Map<Block, Block> DEGRADATION_MAP = Map.of(
            Blocks.STONE, Blocks.COBBLESTONE,
            Blocks.COBBLESTONE, Blocks.GRAVEL,
            Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE,
            Blocks.SMOOTH_BASALT, Blocks.BASALT,
            Blocks.BRICKS, Blocks.CRACKED_STONE_BRICKS,
            Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN
    );
}