package net.darmo_creations.build_utils.gui;

import net.darmo_creations.build_utils.BuildUtils;
import net.darmo_creations.build_utils.items.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * Creative mode tab for this mod.
 */
public class CreativeTab extends ItemGroup {
  public CreativeTab() {
    super(BuildUtils.MODID);
  }

  @Override
  public ItemStack makeIcon() {
    return new ItemStack(ModItems.RULER);
  }
}
