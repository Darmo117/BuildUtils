package net.darmo_creations.build_utils.calculator.nodes.expr;

import net.minecraft.nbt.CompoundTag;

import java.util.Arrays;
import java.util.List;

/**
 * A {@link Node} representing an operator with two operands.
 */
public abstract class BiOperatorNode extends OperatorNode {
  /**
   * Create a binary operator with two operands.
   *
   * @param symbol Operator’s symbol.
   * @param left   Left operand.
   * @param right  Right operand.
   */
  public BiOperatorNode(final String symbol, final Node left, final Node right) {
    super(symbol, 2, Arrays.asList(left, right));
  }

  /**
   * Create a binary operator from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public BiOperatorNode(final CompoundTag tag) {
    super(tag);
  }

  @Override
  protected final double evaluateImpl(final List<Double> values) {
    return this.evaluateImpl(values.get(0), values.get(1));
  }

  /**
   * Delegate method that returns the result of the operator.
   *
   * @param left  Value of the left operand.
   * @param right Value of the right operand.
   * @return Operator’s result.
   */
  protected abstract double evaluateImpl(final double left, final double right);

  @Override
  public String toString() {
    return String.format("%s %s %s", this.operands.get(0), this.getName(), this.operands.get(1));
  }
}
