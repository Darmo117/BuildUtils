package net.darmo_creations.build_utils.calculator.nodes;

import net.darmo_creations.build_utils.calculator.Scope;
import net.darmo_creations.build_utils.calculator.exceptions.EvaluationException;
import net.darmo_creations.build_utils.calculator.nodes.expr.Node;

import java.util.Objects;

/**
 * A statement that only contains an expression.
 */
public class ExpressionStatement extends Statement {
  private final Node node;

  /**
   * Create a statement containing an expression.
   *
   * @param node The expression {@link Node} tree.
   */
  public ExpressionStatement(final Node node) {
    this.node = Objects.requireNonNull(node);
  }

  /**
   * Evaluates the expression then returns its value.
   *
   * @throws EvaluationException If an error occured during {@link Node} evaluation.
   * @throws ArithmeticException If a math error occured.
   */
  @Override
  public StatementResult execute(final Scope scope) throws EvaluationException, ArithmeticException {
    double value = this.node.evaluate(scope);
    return new StatementResult("" + value, value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    return this.node.equals(((ExpressionStatement) o).node);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.node);
  }

  @Override
  public String toString() {
    return this.node.toString();
  }
}
