package net.darmo_creations.build_utils.calculator.nodes.expr;

import net.minecraft.nbt.CompoundTag;

/**
 * A {@link Node} representing the "greater than" operator (a > b).
 * Returns 1 if a > b; otherwise 0.
 */
public class GreaterThanOperatorNode extends BiOperatorNode {
  public static final int ID = 410;

  /**
   * Create a "greater than" operator: left > right.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public GreaterThanOperatorNode(final Node left, final Node right) {
    super(">", left, right);
  }

  /**
   * Create a "greater than" operator from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public GreaterThanOperatorNode(final CompoundTag tag) {
    super(tag);
  }

  @Override
  protected double evaluateImpl(final double left, final double right) {
    return left > right ? 1 : 0;
  }

  @Override
  public int getID() {
    return ID;
  }
}
