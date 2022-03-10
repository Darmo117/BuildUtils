package net.darmo_creations.build_utils.calculator.nodes;

import net.darmo_creations.build_utils.calculator.Scope;
import net.darmo_creations.build_utils.calculator.UserFunction;
import net.darmo_creations.build_utils.calculator.exceptions.EvaluationException;
import net.darmo_creations.build_utils.calculator.nodes.expr.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A statement that defines a function.
 */
public class DefineFunctionStatement extends Statement {
  private final String functionName;
  private final List<String> parameterNames;
  private final Node node;

  /**
   * Create an function definition statement.
   *
   * @param functionName   Function’s name.
   * @param parameterNames Function’s parameter names.
   * @param node           Function’s expression {@link Node} tree.
   */
  public DefineFunctionStatement(final String functionName, final List<String> parameterNames, final Node node) {
    this.functionName = Objects.requireNonNull(functionName);
    this.parameterNames = new ArrayList<>(parameterNames);
    this.node = Objects.requireNonNull(node);
  }

  /**
   * Defines the function in the given scope.
   *
   * @throws EvaluationException If an error occured during {@link Node} evaluation.
   */
  @Override
  public StatementResult execute(Scope scope) throws EvaluationException {
    scope.setFunction(new UserFunction(this.functionName, this.parameterNames, this.node));
    return new StatementResult(scope.getFunction(this.functionName).toString(), null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    DefineFunctionStatement that = (DefineFunctionStatement) o;
    return this.functionName.equals(that.functionName) && this.parameterNames.equals(that.parameterNames) && this.node.equals(that.node);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.functionName, this.parameterNames, this.node);
  }

  @Override
  public String toString() {
    return String.format("%s(%s) := %s", this.functionName, String.join(", ", this.parameterNames), this.node);
  }
}
