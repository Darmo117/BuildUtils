package net.darmo_creations.build_utils.calculator.nodes.expr;

import net.darmo_creations.build_utils.Utils;
import net.minecraft.nbt.CompoundTag;

/**
 * A {@link Node} representing the modulo operator, i.e the remainder of the division of a / b.
 */
public class ModuloOperatorNode extends BiOperatorNode {
  public static final int ID = 404;

  /**
   * Create a modulo operator: remainder of a / b.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public ModuloOperatorNode(final Node left, final Node right) {
    super("%", left, right);
  }

  /**
   * Create a modulo operator from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public ModuloOperatorNode(final CompoundTag tag) {
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
    return Utils.trueModulo(left, right);
  }

  @Override
  public int getID() {
    return ID;
  }
}
