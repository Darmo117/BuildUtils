package net.darmo_creations.build_utils.calculator.nodes.expr;

import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for deserializing {@link Node}s from NBT tags.
 */
public final class NodeNBTHelper {
  private static final Map<Integer, Function<CompoundTag, Node>> NODE_PROVIDERS = new HashMap<>();

  static {
    NODE_PROVIDERS.put(NumberNode.ID, NumberNode::new);
    NODE_PROVIDERS.put(VariableNode.ID, VariableNode::new);
    NODE_PROVIDERS.put(FunctionNode.ID, FunctionNode::new);
    NODE_PROVIDERS.put(MinusOperatorNode.ID, MinusOperatorNode::new);
    NODE_PROVIDERS.put(NotOperatorNode.ID, NotOperatorNode::new);
    NODE_PROVIDERS.put(AdditionOperatorNode.ID, AdditionOperatorNode::new);
    NODE_PROVIDERS.put(SubtractionOperatorNode.ID, SubtractionOperatorNode::new);
    NODE_PROVIDERS.put(MultiplicationOperatorNode.ID, MultiplicationOperatorNode::new);
    NODE_PROVIDERS.put(DivisionOperatorNode.ID, DivisionOperatorNode::new);
    NODE_PROVIDERS.put(ModuloOperatorNode.ID, ModuloOperatorNode::new);
    NODE_PROVIDERS.put(PowerOperatorNode.ID, PowerOperatorNode::new);
    NODE_PROVIDERS.put(AndOperatorNode.ID, AndOperatorNode::new);
    NODE_PROVIDERS.put(OrOperatorNode.ID, OrOperatorNode::new);
    NODE_PROVIDERS.put(EqualToOperatorNode.ID, EqualToOperatorNode::new);
    NODE_PROVIDERS.put(NotEqualToOperatorNode.ID, NotEqualToOperatorNode::new);
    NODE_PROVIDERS.put(GreaterThanOperatorNode.ID, GreaterThanOperatorNode::new);
    NODE_PROVIDERS.put(GreaterThanOrEqualToOperatorNode.ID, GreaterThanOrEqualToOperatorNode::new);
    NODE_PROVIDERS.put(LessThanOperatorNode.ID, LessThanOperatorNode::new);
    NODE_PROVIDERS.put(LessThanOrEqualToOperatorNode.ID, LessThanOrEqualToOperatorNode::new);
  }

  /**
   * Return the node corresponding to the given tag.
   *
   * @param tag The tag to deserialize.
   * @return The node.
   * @throws IllegalArgumentException If no {@link Node} correspond to the {@link Node#ID_KEY} property.
   */
  public static Node getNodeForTag(final CompoundTag tag) {
    int tagID = tag.getInt(Node.ID_KEY);
    if (!NODE_PROVIDERS.containsKey(tagID)) {
      throw new IllegalArgumentException("Undefined tag ID: " + tagID);
    }
    return NODE_PROVIDERS.get(tagID).apply(tag);
  }

  private NodeNBTHelper() {
  }
}
