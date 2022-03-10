package net.darmo_creations.build_utils.calculator.nodes.expr;

import net.minecraft.nbt.CompoundTag;

/**
 * A {@link Node} representing the subtraction operator (a - b).
 */
public class SubtractionOperatorNode extends BiOperatorNode {
  public static final int ID = 401;

  /**
   * Create a subtraction operator: left - right.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public SubtractionOperatorNode(final Node left, final Node right) {
    super("-", left, right);
  }

  /**
   * Create a subtraction operator from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public SubtractionOperatorNode(final CompoundTag tag) {
    super(tag);
  }

  @Override
  protected double evaluateImpl(final double left, final double right) {
    return left - right;
  }

  @Override
  public int getID() {
    return ID;
  }
}
