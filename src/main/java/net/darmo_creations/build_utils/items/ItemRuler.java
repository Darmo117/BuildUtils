package net.darmo_creations.build_utils.items;

import net.darmo_creations.build_utils.BuildUtils;
import net.darmo_creations.build_utils.Utils;
import net.darmo_creations.build_utils.calculator.Calculator;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

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
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    if (player == null) {
      return InteractionResult.FAIL;
    }
    ItemStack heldItem = context.getItemInHand();
    BlockPos pos = context.getClickedPos();
    Level world = context.getLevel();
    RulerData data = RulerData.fromTag(heldItem.getTag());

    if (data.position == null) {
      data.position = pos;
      Utils.sendMessage(world, player, new TextComponent(
          "Selected first position: " + Utils.blockPosToString(pos))
          .setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)));
    } else {
      Utils.sendMessage(world, player, new TextComponent(
          "Selected second position: " + Utils.blockPosToString(pos))
          .setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)));

      Calculator calculator = BuildUtils.CALCULATORS_MANAGER.getOrCreatePlayerData(player);
      // Declare variables storing the positions in the player’s calculator
      calculator.setVariable("ruler_x1", data.position.getX());
      calculator.setVariable("ruler_y1", data.position.getY());
      calculator.setVariable("ruler_z1", data.position.getZ());
      calculator.setVariable("ruler_x2", pos.getX());
      calculator.setVariable("ruler_y2", pos.getY());
      calculator.setVariable("ruler_z2", pos.getZ());

      Vec3i lengths = Utils.getLengths(data.position, pos);
      int lengthX = lengths.getX();
      int lengthY = lengths.getY();
      int lengthZ = lengths.getZ();
      Utils.sendMessage(world, player, new TextComponent(
          String.format("Size (XYZ): %d x %d x %d", lengthX, lengthY, lengthZ))
          .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
      // Declare variables storing the lengths
      calculator.setVariable("ruler_lx", lengthX);
      calculator.setVariable("ruler_ly", lengthY);
      calculator.setVariable("ruler_lz", lengthZ);

      // Do not display any area if at least two dimensions have a length of 1 (single line of blocks selected)
      if (lengthX + lengthY != 2 && lengthX + lengthZ != 2 && lengthY + lengthZ != 2) {
        Vec3i areas = Utils.getAreas(data.position, pos);
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
          Utils.sendMessage(world, player, new TextComponent(
              String.format("Area: %d", area))
              .setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN)));
          // Declare variables storing the area
          calculator.setVariable("ruler_area", area);
        } else {
          Utils.sendMessage(world, player, new TextComponent(
              String.format("Areas (XYZ): %d, %d, %d", areaX, areaY, areaZ))
              .setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN)));
          // Declare variables storing the areas
          calculator.setVariable("ruler_ax", areaX);
          calculator.setVariable("ruler_ay", areaY);
          calculator.setVariable("ruler_az", areaZ);
        }
      }

      int volume = Utils.getVolume(data.position, pos);
      Utils.sendMessage(world, player, new TextComponent(
          String.format("Volume: %d", volume))
          .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
      // Declare variables storing the volume
      calculator.setVariable("ruler_vol", volume);

      data.position = null;
    }

    heldItem.setTag(data.toTag());
    return InteractionResult.SUCCESS;
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
    static RulerData fromTag(CompoundTag data) {
      if (data != null) {
        CompoundTag tag = data.getCompound(POS_TAG_KEY);
        return new RulerData(!tag.isEmpty() ? NbtUtils.readBlockPos(tag) : null);
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
    CompoundTag toTag() {
      CompoundTag root = new CompoundTag();

      if (this.position != null) {
        root.put(POS_TAG_KEY, NbtUtils.writeBlockPos(this.position));
      }

      return root;
    }
  }
}
