package net.darmo_creations.build_utils.calculator.nodes;

import net.darmo_creations.build_utils.calculator.Scope;
import net.darmo_creations.build_utils.calculator.exceptions.EvaluationException;
import net.darmo_creations.build_utils.calculator.nodes.expr.Node;

import java.util.Locale;
import java.util.Objects;

/**
 * A statement that assigns a value to a variable.
 */
public class AssignVariableStatement extends Statement {
  private final String variableName;
  private final Node node;

  /**
   * Create an variable assignment statement.
   *
   * @param variableName Variable’s name.
   * @param node         Expression {@link Node} tree to evaluate then store into variable.
   */
  public AssignVariableStatement(final String variableName, final Node node) {
    this.variableName = Objects.requireNonNull(variableName);
    this.node = Objects.requireNonNull(node);
  }

  /**
   * Evaluates the expression then stores its value in the variable in the given scope.
   *
   * @throws EvaluationException If an error occured during {@link Node} evaluation.
   * @throws ArithmeticException If a math error occured.
   */
  @Override
  public StatementResult execute(Scope scope) throws EvaluationException, ArithmeticException {
    double value = this.node.evaluate(scope);
    scope.setVariable(this.variableName, value);
    return new StatementResult(String.format(Locale.ENGLISH, "%s <- %f", this.variableName, value), null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    AssignVariableStatement that = (AssignVariableStatement) o;
    return this.variableName.equals(that.variableName) && this.node.equals(that.node);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.variableName, this.node);
  }

  @Override
  public String toString() {
    return String.format(Locale.ENGLISH, "%s := %s", this.variableName, this.node);
  }
}
