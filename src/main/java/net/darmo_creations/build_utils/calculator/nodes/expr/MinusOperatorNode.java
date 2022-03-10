package net.darmo_creations.build_utils.calculator.nodes.expr;

import net.minecraft.nbt.CompoundTag;

/**
 * A {@link Node} representing the unary minus operator (-a).
 */
public class MinusOperatorNode extends UnaryOperatorNode {
  public static final int ID = 300;

  /**
   * Create a unary minus operator: -operand.
   *
   * @param operand The expression to compute the opposite of.
   */
  public MinusOperatorNode(final Node operand) {
    super("-", operand);
  }

  /**
   * Create a unary minus operator from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public MinusOperatorNode(final CompoundTag tag) {
    super(tag);
  }

  @Override
  protected double evaluateImpl(final double value) {
    return -value;
  }

  @Override
  public int getID() {
    return ID;
  }
}
