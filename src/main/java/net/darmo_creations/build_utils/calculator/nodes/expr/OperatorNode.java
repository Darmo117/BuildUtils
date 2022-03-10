package net.darmo_creations.build_utils.calculator.nodes.expr;

import net.darmo_creations.build_utils.calculator.Scope;
import net.darmo_creations.build_utils.calculator.exceptions.EvaluationException;
import net.darmo_creations.build_utils.calculator.exceptions.SyntaxErrorException;
import net.minecraft.nbt.CompoundTag;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link Node} representing an operator. Operators are special kinds of functions
 * that use an infixed symbol instead of a prefixed name.
 */
public abstract class OperatorNode extends FunctionNode {
  /**
   * Create an operator.
   *
   * @param symbol   Operator’s symbol.
   * @param arity    Operator’s arity, i.e. its number of operands.
   * @param operands Operator’s operands.
   * @throws SyntaxErrorException If the number of operands does not match the arity.
   */
  public OperatorNode(final String symbol, final int arity, final List<Node> operands) throws SyntaxErrorException {
    super(symbol, operands);
    if (this.operands.size() != arity) {
      throw new SyntaxErrorException(String.format("operator %s expected %d arguments, got %d", symbol, arity, operands.size()));
    }
  }

  /**
   * Create an operator from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public OperatorNode(final CompoundTag tag) {
    super(tag);
  }

  /**
   * Evaluate the operator then return its value.
   *
   * @return Operator’s result.
   * @throws EvaluationException If an error occured during {@link Node} evaluation.
   * @throws ArithmeticException If a math error occured.
   */
  @Override
  public double evaluate(final Scope scope) throws EvaluationException, ArithmeticException {
    return this.evaluateImpl(this.operands.stream().map(node -> node.evaluate(scope)).collect(Collectors.toList()));
  }

  /**
   * Delegate method that returns the result of the operator.
   *
   * @param values Values of the operands.
   * @return Operator’s result.
   */
  protected abstract double evaluateImpl(final List<Double> values);
}
