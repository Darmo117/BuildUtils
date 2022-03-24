package net.darmo_creations.build_utils.items;

import net.darmo_creations.build_utils.BuildUtils;
import net.darmo_creations.build_utils.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/**
 * Item used to measure lengths, areas and volumes.
 * <p>
 * Usage:
 * <li>Right-click on block to select first position.
 * <li>Right-click on another block to select second position.
 * The size, areas and volume the selected 3D rectangle will appear in the chat.
 */
public class ItemRuler extends Item {
  private static final String POS_TAG_KEY = "Pos";

  public ItemRuler() {
    super(new Item.Properties().stacksTo(1).tab(BuildUtils.CREATIVE_MODE_TAB));
  }

  @Override
  public ActionResultType useOn(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    if (player == null) {
      return ActionResultType.FAIL;
    }
    ItemStack heldItem = context.getItemInHand();
    BlockPos pos = context.getClickedPos();
    World world = context.getLevel();
    RulerData data = RulerData.fromTag(heldItem.getTag());

    if (data.position == null) {
      data.position = pos;
      Utils.sendMessage(world, player, new StringTextComponent(
          "Selected first position: " + Utils.blockPosToString(pos))
          .setStyle(Style.EMPTY.withColor(TextFormatting.AQUA)));
    } else {
      Utils.sendMessage(world, player, new StringTextComponent(
          "Selected second position: " + Utils.blockPosToString(pos))
          .setStyle(Style.EMPTY.withColor(TextFormatting.DARK_AQUA)));

      Vector3i lengths = Utils.getLengths(data.position, pos);
      int lengthX = lengths.getX();
      int lengthY = lengths.getY();
      int lengthZ = lengths.getZ();
      Utils.sendMessage(world, player, new StringTextComponent(
          String.format("Size (XYZ): %d x %d x %d", lengthX, lengthY, lengthZ))
          .setStyle(Style.EMPTY.withColor(TextFormatting.GREEN)));

      // Do not display any area if at least two dimensions have a length of 1 (single line of blocks selected)
      if (lengthX + lengthY != 2 && lengthX + lengthZ != 2 && lengthY + lengthZ != 2) {
        Vector3i areas = Utils.getAreas(data.position, pos);
        int areaX = areas.getX();
        int areaY = areas.getY();
        int areaZ = areas.getZ();
        // Only display relevent area if player selected a 1-block-thick volume
        if (lengthX == 1 || lengthY == 1 || lengthZ == 1) {
          int area;
          if (lengthX == 1) {
            area = areaX;
          } else if (lengthZ == 1) {
            area = areaZ;
          } else {
            area = areaY;
          }
          Utils.sendMessage(world, player, new StringTextComponent(
              String.format("Area: %d", area))
              .setStyle(Style.EMPTY.withColor(TextFormatting.DARK_GREEN)));
        } else {
          Utils.sendMessage(world, player, new StringTextComponent(
              String.format("Areas (XYZ): %d, %d, %d", areaX, areaY, areaZ))
              .setStyle(Style.EMPTY.withColor(TextFormatting.DARK_GREEN)));
        }
      }

      int volume = Utils.getVolume(data.position, pos);
      Utils.sendMessage(world, player, new StringTextComponent(
          String.format("Volume: %d", volume))
          .setStyle(Style.EMPTY.withColor(TextFormatting.GOLD)));

      data.position = null;
    }

    heldItem.setTag(data.toTag());
    return ActionResultType.SUCCESS;
  }

  /**
   * Class holding data for the ruler that can serialize/deserialize NBT tags.
   */
  private static class RulerData {
    /**
     * Create a data instance from the given NBT tags.
     * If tag is null, an empty instance is returned.
     *
     * @param data NBT tag to deserialize.
     * @return The RulerData object.
     */
    static RulerData fromTag(CompoundNBT data) {
      if (data != null) {
        CompoundNBT tag = data.getCompound(POS_TAG_KEY);
        return new RulerData(!tag.isEmpty() ? NBTUtil.readBlockPos(tag) : null);
      } else {
        return new RulerData();
      }
    }

    BlockPos position;

    /**
     * Create an empty object.
     */
    RulerData() {
      this(null);
    }

    /**
     * Create an object for the given positions.
     */
    RulerData(BlockPos position) {
      this.position = position;
    }

    /**
     * Convert this data object to NBT tags.
     */
    CompoundNBT toTag() {
      CompoundNBT root = new CompoundNBT();

      if (this.position != null) {
        root.put(POS_TAG_KEY, NBTUtil.writeBlockPos(this.position));
      }

      return root;
    }
  }
}
