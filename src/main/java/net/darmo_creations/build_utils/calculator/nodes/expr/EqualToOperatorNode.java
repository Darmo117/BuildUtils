package net.darmo_creations.build_utils.calculator.nodes.expr;

import net.minecraft.nbt.CompoundTag;

/**
 * A {@link Node} representing the equality operator (a = b).
 * Returns 1 if a = b; otherwise 0.
 */
public class EqualToOperatorNode extends BiOperatorNode {
  public static final int ID = 408;

  /**
   * Create an equality operator: left = right.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public EqualToOperatorNode(final Node left, final Node right) {
    super("=", left, right);
  }

  /**
   * Create an equality operator from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public EqualToOperatorNode(final CompoundTag tag) {
    super(tag);
  }

  @Override
  protected double evaluateImpl(final double left, final double right) {
    return left == right ? 1 : 0;
  }

  @Override
  public int getID() {
    return ID;
  }
}
