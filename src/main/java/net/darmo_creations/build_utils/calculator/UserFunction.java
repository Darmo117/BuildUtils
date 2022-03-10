package net.darmo_creations.build_utils.calculator;

import net.darmo_creations.build_utils.calculator.nodes.expr.Node;
import net.darmo_creations.build_utils.calculator.nodes.expr.NodeNBTHelper;
import net.minecraft.nbt.CompoundTag;

import java.util.List;
import java.util.Objects;

/**
 * User functions are functions that can be defined by users.
 */
public class UserFunction extends Function {
  private static final String NODE_KEY = "Node";

  private final Node node;

  /**
   * Create a function with the given names, parameters and content.
   *
   * @param name           Function’s name.
   * @param parameterNames Function’s parameter names.
   * @param node           Function’s node tree.
   */
  public UserFunction(final String name, final List<String> parameterNames, final Node node) {
    super(name, parameterNames);
    this.node = Objects.requireNonNull(node);
  }

  /**
   * Create a user function from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public UserFunction(CompoundTag tag) {
    super(tag);
    this.node = NodeNBTHelper.getNodeForTag(tag.getCompound(NODE_KEY));
  }

  @Override
  protected double evaluateImpl(final Scope scope) {
    return this.node.evaluate(scope);
  }

  @Override
  public CompoundTag writeToNBT() {
    CompoundTag tag = super.writeToNBT();
    tag.put(NODE_KEY, this.node.writeToNBT());
    return tag;
  }

  @Override
  public String toString() {
    return String.format("%s(%s) -> %s", this.getName(), String.join(", ", this.getParameterNames()), this.node);
  }
}
