package net.darmo_creations.build_utils.calculator.nodes.expr;

import net.minecraft.nbt.CompoundTag;

/**
 * A {@link Node} representing the logical "and" operator.
 * If the left operand is 0, 0 is returned; otherwise the right operand is returned.
 */
public class AndOperatorNode extends BiOperatorNode {
  public static final int ID = 406;

  /**
   * Create a logical "and" operator.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public AndOperatorNode(final Node left, final Node right) {
    super("&", left, right);
  }

  /**
   * Create a logical "and" operator from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public AndOperatorNode(final CompoundTag tag) {
    super(tag);
  }

  @Override
  protected double evaluateImpl(final double left, final double right) {
    return left == 0 ? 0 : right;
  }

  @Override
  public int getID() {
    return ID;
  }
}
