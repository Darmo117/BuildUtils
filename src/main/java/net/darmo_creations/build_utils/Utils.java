package net.darmo_creations.build_utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class defines various utility functions.
 */
public final class Utils {
  public static final int WHITE = 0xffffff;
  public static final int GRAY = 0xa0a0a0;

  /**
   * Returns the tile entity of the given class at the given position.
   *
   * @param tileEntityClass Tile entity’s class.
   * @param world           The world.
   * @param pos             Tile entity’s position.
   * @param <T>             Tile entity’s type.
   * @return The tile entity if found, an empty optional otherwise.
   */
  public static <T extends BlockEntity> Optional<T> getTileEntity(Class<T> tileEntityClass, Level world, BlockPos pos) {
    BlockEntity te = world.getBlockEntity(pos);
    if (tileEntityClass.isInstance(te)) {
      return Optional.of(tileEntityClass.cast(te));
    }
    return Optional.empty();
  }

  /**
   * Convert block position to string.
   */
  public static String blockPosToString(BlockPos pos) {
    return String.format("%d %d %d", pos.getX(), pos.getY(), pos.getZ());
  }

  /**
   * Serialize a block state to a string.
   *
   * @param blockState Block state to serialize.
   * @return Serialized string.
   */
  public static String blockstateToString(BlockState blockState) {
    //noinspection ConstantConditions
    String message = blockState.getBlock().getRegistryName().toString();
    Map<Property<?>, Comparable<?>> properties = blockState.getValues();
    if (!properties.isEmpty()) {
      message += " " + properties.entrySet().stream()
          .collect(Collectors.toMap(e -> e.getKey().getName(), Map.Entry::getValue));
    }
    return message;
  }

  /**
   * Performs a true modulo operation using the mathematical definition of "a mod b".
   *
   * @param a Value to get the modulo of.
   * @param b The divisor.
   * @return a mod b
   */
  public static double trueModulo(double a, double b) {
    return ((a % b) + b) % b;
  }

  /**
   * Sends a chat message to a player. Does nothing if the world is remote (i.e. client-side).
   *
   * @param world  The world the player is in.
   * @param player The player.
   * @param text   Message’s text.
   */
  public static void sendMessage(final Level world, Player player, final Component text) {
    if (world.isClientSide()) {
      //noinspection ConstantConditions
      player.sendMessage(text, null);
    }
  }

  /**
   * Get the blocks length along each axis of the volume defined by the given positions.
   *
   * @return A {@link Vec3i} object. Each axis holds the number of blocks along itself.
   */
  public static Vec3i getLengths(BlockPos pos1, BlockPos pos2) {
    Pair<BlockPos, BlockPos> positions = normalizePositions(pos1, pos2);
    BlockPos posMin = positions.getLeft();
    BlockPos posMax = positions.getRight();

    return new Vec3i(
        posMax.getX() - posMin.getX() + 1,
        posMax.getY() - posMin.getY() + 1,
        posMax.getZ() - posMin.getZ() + 1
    );
  }

  /**
   * Get the blocks area of each face of the volume defined by the given positions.
   *
   * @return A {@link Vec3i} object. Each axis holds the area of the face perpendicular to itself.
   */
  public static Vec3i getAreas(BlockPos pos1, BlockPos pos2) {
    Vec3i size = getLengths(pos1, pos2);

    return new Vec3i(
        size.getZ() * size.getY(),
        size.getX() * size.getZ(),
        size.getX() * size.getY()
    );
  }

  /**
   * Get the total number of blocks inside the volume defined by the given positions.
   */
  public static int getVolume(BlockPos pos1, BlockPos pos2) {
    Vec3i size = getLengths(pos1, pos2);
    return size.getX() * size.getY() * size.getZ();
  }

  /**
   * Return the highest and lowest block coordinates for the volume defined by the given two positions.
   *
   * @param pos1 First position.
   * @param pos2 Second position.
   * @return A pair with the lowest and highest positions.
   */
  public static Pair<BlockPos, BlockPos> normalizePositions(BlockPos pos1, BlockPos pos2) {
    BlockPos posMin = new BlockPos(
        Math.min(pos1.getX(), pos2.getX()),
        Math.min(pos1.getY(), pos2.getY()),
        Math.min(pos1.getZ(), pos2.getZ())
    );
    BlockPos posMax = new BlockPos(
        Math.max(pos1.getX(), pos2.getX()),
        Math.max(pos1.getY(), pos2.getY()),
        Math.max(pos1.getZ(), pos2.getZ())
    );
    return new ImmutablePair<>(posMin, posMax);
  }

  private Utils() {
  }
}
