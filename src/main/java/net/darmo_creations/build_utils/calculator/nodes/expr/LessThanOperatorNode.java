package net.darmo_creations.build_utils.calculator.nodes.expr;

import net.minecraft.nbt.CompoundTag;

/**
 * A {@link Node} representing the "less than" operator (a < b).
 * Returns 1 if a < b; otherwise 0.
 */
public class LessThanOperatorNode extends BiOperatorNode {
  public static final int ID = 412;

  /**
   * Create a "less than" operator: left < right.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public LessThanOperatorNode(final Node left, final Node right) {
    super("<", left, right);
  }

  /**
   * Create a "less than" operator from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public LessThanOperatorNode(final CompoundTag tag) {
    super(tag);
  }

  @Override
  protected double evaluateImpl(final double left, final double right) {
    return left < right ? 1 : 0;
  }

  @Override
  public int getID() {
    return ID;
  }
}
