package net.darmo_creations.build_utils.calculator.nodes.expr;

import net.minecraft.nbt.CompoundTag;

/**
 * A {@link Node} representing the division operator (a / b).
 */
public class DivisionOperatorNode extends BiOperatorNode {
  public static final int ID = 403;

  /**
   * Create a division operator: left / right.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public DivisionOperatorNode(final Node left, final Node right) {
    super("/", left, right);
  }

  /**
   * Create a division operator from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public DivisionOperatorNode(final CompoundTag tag) {
    super(tag);
  }

  /**
   * {@inheritDoc}
   *
   * @throws ArithmeticException If the right operand is 0.
   */
  @Override
  protected double evaluateImpl(final double left, final double right) throws ArithmeticException {
    if (right == 0) {
      throw new ArithmeticException("division by 0");
    }
    return left / right;
  }

  @Override
  public int getID() {
    return ID;
  }
}
