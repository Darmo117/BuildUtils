package net.darmo_creations.build_utils.calculator.nodes.expr;

import net.minecraft.nbt.CompoundTag;

/**
 * A {@link Node} representing the multiplication operator (a * b).
 */
public class MultiplicationOperatorNode extends BiOperatorNode {
  public static final int ID = 402;

  /**
   * Create a multiplication operator: left * right.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public MultiplicationOperatorNode(final Node left, final Node right) {
    super("*", left, right);
  }

  /**
   * Create a multiplication operator from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public MultiplicationOperatorNode(final CompoundTag tag) {
    super(tag);
  }

  @Override
  protected double evaluateImpl(final double left, final double right) {
    return left * right;
  }

  @Override
  public int getID() {
    return ID;
  }
}
