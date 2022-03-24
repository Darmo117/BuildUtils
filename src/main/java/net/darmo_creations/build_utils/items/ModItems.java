package net.darmo_creations.build_utils.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import java.util.*;

/**
 * This class declares all items for this mod.
 */
@SuppressWarnings("unused")
public final class ModItems {
  public static final Item RULER = new ItemRuler().setRegistryName("ruler");
  public static final Item CREATIVE_WAND = new ItemCreativeWand().setRegistryName("creative_wand");

  /**
   * The list of all explicitly declared items for this mod.
   */
  public static final List<Item> ITEMS = new LinkedList<>();
  /**
   * The list of all generated items for this mod’s blocks.
   */
  public static final Map<Block, BlockItem> ITEM_BLOCKS = new HashMap<>();

  static {
    Arrays.stream(ModItems.class.getDeclaredFields())
        .filter(field -> Item.class.isAssignableFrom(field.getType()))
        .map(field -> {
          Item item;
          try {
            item = (Item) field.get(null);
          } catch (IllegalAccessException e) {
            // Should never happen
            throw new RuntimeException(e);
          }
          return item;
        })
        .forEach(ITEMS::add);
  }

  private ModItems() {
  }
}
