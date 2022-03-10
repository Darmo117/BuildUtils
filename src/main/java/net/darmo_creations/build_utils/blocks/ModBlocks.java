package net.darmo_creations.build_utils.blocks;

import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class declares all blocks for this mod.
 */
@SuppressWarnings("unused")
public final class ModBlocks {
  public static final Block LASER_TELEMETER = new BlockLaserTelemeter().setRegistryName("laser_telemeter");

  /**
   * The list of all declared blocks for this mod.
   */
  public static final List<Block> BLOCKS = new LinkedList<>();

  static {
    Arrays.stream(ModBlocks.class.getDeclaredFields())
        .filter(field -> Block.class.isAssignableFrom(field.getType()))
        .map(field -> {
          Block block;
          try {
            block = (Block) field.get(null);
          } catch (IllegalAccessException e) {
            // Should never happen
            throw new RuntimeException(e);
          }
          return block;
        })
        .forEach(BLOCKS::add);
  }

  private ModBlocks() {
  }
}
