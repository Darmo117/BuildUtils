package net.darmo_creations.build_utils.items;

import net.darmo_creations.build_utils.BuildUtils;
import net.darmo_creations.build_utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Item used to fill areas with blocks.
 * <p>
 * Usage:
 * <li>Right-click on block to select the first position.
 * <li>Right-click on another block to select the second position.
 * <li>Sneak-right-click on a block to select it as filler. If no block is targetted, air will be selected.
 */
public class ItemCreativeWand extends Item {
  private static final String POS1_TAG_KEY = "Pos1";
  private static final String POS2_TAG_KEY = "Pos2";
  private static final String STATE_TAG_KEY = "BlockState";

  // Maximum number of blocks that can be filled at the same time
  private static final int VOLUME_LIMIT = 32768;

  public ItemCreativeWand() {
    super(new Properties().stacksTo(1).tab(BuildUtils.CREATIVE_MODE_TAB));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
    ItemStack heldItem = player.getItemInHand(hand);
    WandData data = WandData.fromTag(heldItem.getTag());

    InteractionResult result;
    if (hand == InteractionHand.OFF_HAND) {
      setBlockState(Blocks.AIR.defaultBlockState(), data, world, player);
      heldItem.setTag(data.toTag());
      result = InteractionResult.SUCCESS;
    } else if (data.isReady()) {
      BlockState state = data.blockState;
      Pair<BlockPos, BlockPos> positions = Utils.normalizePositions(data.firstPosition, data.secondPosition);
      BlockPos posMin = positions.getLeft();
      BlockPos posMax = positions.getRight();
      int volume = (posMax.getX() - posMin.getX() + 1)
          * (posMax.getY() - posMin.getY() + 1)
          * (posMax.getZ() - posMin.getZ() + 1);

      if (volume > VOLUME_LIMIT) {
        Utils.sendMessage(world, player, new TranslatableComponent(
            "item.build_utils.creative_wand.error.too_many_blocks",
            volume, VOLUME_LIMIT
        ).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
        result = InteractionResult.FAIL;
      } else {
        List<BlockPos> list = new ArrayList<>();

        for (BlockPos p : BlockPos.betweenClosed(posMin, posMax)) {
          world.setBlock(p, state, 2);
          list.add(p);
        }

        for (BlockPos blockPos : list) {
          world.blockUpdated(blockPos, world.getBlockState(blockPos).getBlock());
        }

        Utils.sendMessage(world, player, new TranslatableComponent(
            "item.build_utils.creative_wand.feedback.filled_area",
            volume,
            state.getBlock().getRegistryName()
        ).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN)));
        result = InteractionResult.SUCCESS;
      }
    } else {
      Utils.sendMessage(world, player, new TranslatableComponent("Cannot fill area!")
          .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
      result = InteractionResult.FAIL;
    }

    return new InteractionResultHolder<>(result, heldItem);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    if (player == null) {
      return InteractionResult.FAIL;
    }
    Level world = context.getLevel();
    if (!player.hasPermissions(2)) {
      Utils.sendMessage(world, player, new TranslatableComponent(
          "item.build_utils.creative_wand.error.permissions"
      ).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
    }
    BlockPos pos = context.getClickedPos();
    ItemStack heldItem = context.getItemInHand();
    WandData data = WandData.fromTag(heldItem.getTag());

    if (context.getHand() == InteractionHand.OFF_HAND) {
      setBlockState(world.getBlockState(pos), data, world, player);
    } else {
      if (data.firstPosition == null || data.secondPosition != null) {
        data.firstPosition = pos;
        data.secondPosition = null;
        Utils.sendMessage(world, player, new TranslatableComponent(
            "item.build_utils.creative_wand.feedback.pos1_selected",
            Utils.blockPosToString(pos)
        ).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)));
      } else {
        data.secondPosition = pos;
        Utils.sendMessage(world, player, new TranslatableComponent(
            "item.build_utils.creative_wand.feedback.pos2_selected",
            Utils.blockPosToString(pos)
        ).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)));
      }
    }

    heldItem.setTag(data.toTag());
    return InteractionResult.SUCCESS;
  }

  @Override
  public void appendHoverText(ItemStack stack, Level world, List<Component> components, TooltipFlag tooltipFlag) {
    WandData data = WandData.fromTag(stack.getTag());
    components.add(new TranslatableComponent(
        "item.build_utils.creative_wand.tooltip.pos1",
        data.firstPosition != null ? Utils.blockPosToString(data.firstPosition) : "-"
    ).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)));
    components.add(new TranslatableComponent(
        "item.build_utils.creative_wand.tooltip.pos2",
        data.secondPosition != null ? Utils.blockPosToString(data.secondPosition) : "-"
    ).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)));
    components.add(new TranslatableComponent(
        "item.build_utils.creative_wand.tooltip.blockstate",
        data.blockState != null ? Utils.blockstateToString(data.blockState) : "-"
    ).setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)));
  }

  /**
   * Set block state of tool then send a confirmation message to player.
   *
   * @param blockState Block state to use.
   * @param data       Wand data.
   * @param world      World the player is in.
   * @param player     Player to send chat message to.
   */
  private static void setBlockState(final BlockState blockState, WandData data, final Level world, Player player) {
    data.blockState = blockState;
    Utils.sendMessage(world, player, new TranslatableComponent(
        "item.build_utils.creative_wand.feedback.blockstate_selected",
        Utils.blockstateToString(blockState)
    ).setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)));
  }

  /**
   * Class holding data for the wand that can serialize/deserialize NBT tags.
   */
  private static class WandData {
    /**
     * Create a data instance from the given NBT tags.
     * If tag is null, an empty instance is returned.
     *
     * @param data NBT tag to deserialize.
     * @return The WandData object.
     */
    static WandData fromTag(CompoundTag data) {
      if (data != null) {
        CompoundTag tag1 = data.getCompound(POS1_TAG_KEY);
        CompoundTag tag2 = data.getCompound(POS2_TAG_KEY);
        CompoundTag tagState = data.getCompound(STATE_TAG_KEY);
        BlockPos pos1 = !tag1.isEmpty() ? NbtUtils.readBlockPos(tag1) : null;
        BlockPos pos2 = !tag2.isEmpty() ? NbtUtils.readBlockPos(tag2) : null;
        BlockState state = !tagState.isEmpty() ? NbtUtils.readBlockState(tagState) : null;
        return new WandData(pos1, pos2, state);
      } else {
        return new WandData();
      }
    }

    BlockPos firstPosition;
    BlockPos secondPosition;
    BlockState blockState;

    /**
     * Create an empty object.
     */
    WandData() {
      this(null, null, null);
    }

    /**
     * Create an object for the given positions and block state.
     */
    WandData(BlockPos firstPosition, BlockPos secondPosition, BlockState blockState) {
      this.firstPosition = firstPosition;
      this.secondPosition = secondPosition;
      this.blockState = blockState;
    }

    /**
     * Data object is considered ready when both positions and blockstate are set.
     */
    boolean isReady() {
      return this.firstPosition != null && this.secondPosition != null && this.blockState != null;
    }

    /**
     * Convert this data object to NBT tags.
     */
    CompoundTag toTag() {
      CompoundTag root = new CompoundTag();

      if (this.firstPosition != null) {
        root.put(POS1_TAG_KEY, NbtUtils.writeBlockPos(this.firstPosition));
      }
      if (this.secondPosition != null) {
        root.put(POS2_TAG_KEY, NbtUtils.writeBlockPos(this.secondPosition));
      }
      if (this.blockState != null) {
        root.put(STATE_TAG_KEY, NbtUtils.writeBlockState(this.blockState));
      }

      return root;
    }
  }
}
