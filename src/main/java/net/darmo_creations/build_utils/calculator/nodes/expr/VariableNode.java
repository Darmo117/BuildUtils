package net.darmo_creations.build_utils.calculator.nodes.expr;

import net.darmo_creations.build_utils.calculator.Scope;
import net.darmo_creations.build_utils.calculator.exceptions.UndefinedVariableException;
import net.minecraft.nbt.CompoundTag;

import java.util.Objects;

/**
 * A {@link Node} representing a variable.
 */
public class VariableNode extends Node {
  public static final int ID = 1;

  private static final String NAME_KEY = "Name";

  private final String name;

  /**
   * Create a variable {@link Node}.
   *
   * @param name Variable’s name.
   */
  public VariableNode(final String name) {
    this.name = Objects.requireNonNull(name);
  }

  /**
   * Create a number {@link Node} from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public VariableNode(final CompoundTag tag) {
    this(tag.getString(NAME_KEY));
  }

  /**
   * Return the value of the associated variable.
   *
   * @return The variable’s value.
   * @throws UndefinedVariableException If no variable or constant with this name exists in the given scope.
   */
  @Override
  public double evaluate(final Scope scope) throws UndefinedVariableException {
    return scope.getVariable(this.name);
  }

  @Override
  public CompoundTag writeToNBT() {
    CompoundTag tag = super.writeToNBT();
    tag.putString(NAME_KEY, this.name);
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
    return this.name.equals(((VariableNode) o).name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name);
  }

  @Override
  public String toString() {
    return this.name;
  }
}
