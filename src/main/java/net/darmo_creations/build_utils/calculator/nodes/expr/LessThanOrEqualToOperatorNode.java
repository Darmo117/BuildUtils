package net.darmo_creations.build_utils.calculator.nodes.expr;

import net.minecraft.nbt.CompoundTag;

/**
 * A {@link Node} representing the "less than or equal to" operator (a ≤ b).
 * Returns 1 if a ≤ b; otherwise 0.
 */
public class LessThanOrEqualToOperatorNode extends BiOperatorNode {
  public static final int ID = 413;

  /**
   * Create a "less than or equal to" operator: left ≤ right.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public LessThanOrEqualToOperatorNode(final Node left, final Node right) {
    super("<=", left, right);
  }

  /**
   * Create a "less than or equal to" operator from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public LessThanOrEqualToOperatorNode(final CompoundTag tag) {
    super(tag);
  }

  @Override
  protected double evaluateImpl(final double left, final double right) {
    return left <= right ? 1 : 0;
  }

  @Override
  public int getID() {
    return ID;
  }
}
