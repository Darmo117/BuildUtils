package net.darmo_creations.build_utils.gui;

import net.darmo_creations.build_utils.BuildUtils;
import net.darmo_creations.build_utils.items.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/**
 * Creative mode tab for this mod.
 */
public class CreativeTab extends CreativeModeTab {
  public CreativeTab() {
    super(BuildUtils.MODID);
  }

  @Override
  public ItemStack makeIcon() {
    return new ItemStack(ModItems.RULER);
  }
}
