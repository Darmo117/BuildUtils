package net.darmo_creations.build_utils.calculator.nodes.expr;

import net.darmo_creations.build_utils.calculator.Scope;
import net.minecraft.nbt.CompoundTag;

import java.util.Objects;

/**
 * A {@link Node} representing a number.
 */
public class NumberNode extends Node {
  public static final int ID = 0;

  private static final String VALUE_KEY = "Value";

  private final double value;

  /**
   * Create a number {@link Node}.
   *
   * @param value Number value.
   */
  public NumberNode(final double value) {
    this.value = value;
  }

  /**
   * Create a number {@link Node} from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public NumberNode(final CompoundTag tag) {
    this(tag.getDouble(VALUE_KEY));
  }

  /**
   * Return the value of this node.
   *
   * @return The associated number.
   */
  @Override
  public double evaluate(final Scope scope) {
    return this.value;
  }

  @Override
  public CompoundTag writeToNBT() {
    CompoundTag tag = super.writeToNBT();
    tag.putDouble(VALUE_KEY, this.value);
    return tag;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    return Double.compare(((NumberNode) o).value, this.value) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.value);
  }

  @Override
  public String toString() {
    return "" + this.value;
  }
}
