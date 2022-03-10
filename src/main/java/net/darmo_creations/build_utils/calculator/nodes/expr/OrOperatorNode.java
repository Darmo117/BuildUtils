package net.darmo_creations.build_utils.calculator.nodes.expr;

import net.minecraft.nbt.CompoundTag;

/**
 * A {@link Node} representing the logical "or" operator.
 * If the left operand is not 0, it is returned; otherwise the right value is returned.
 */
public class OrOperatorNode extends BiOperatorNode {
  public static final int ID = 407;

  /**
   * Create a logical "or" operator.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public OrOperatorNode(final Node left, final Node right) {
    super("&", left, right);
  }

  /**
   * Create a logical "or" operator from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public OrOperatorNode(final CompoundTag tag) {
    super(tag);
  }

  @Override
  protected double evaluateImpl(final double left, final double right) {
    return left != 0 ? left : right;
  }

  @Override
  public int getID() {
    return ID;
  }
}
