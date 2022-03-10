package net.darmo_creations.build_utils.calculator.nodes.expr;

import net.minecraft.nbt.CompoundTag;

/**
 * A {@link Node} representing the exponent operator (a<sup>b</sup>).
 */
public class PowerOperatorNode extends BiOperatorNode {
  public static final int ID = 405;

  /**
   * Create an exponent operator: left<sup>right</sup>.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public PowerOperatorNode(final Node left, final Node right) {
    super("^", left, right);
  }

  /**
   * Create an exponent operator from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public PowerOperatorNode(final CompoundTag tag) {
    super(tag);
  }

  @Override
  protected double evaluateImpl(final double left, final double right) {
    return Math.pow(left, right);
  }

  @Override
  public int getID() {
    return ID;
  }
}
