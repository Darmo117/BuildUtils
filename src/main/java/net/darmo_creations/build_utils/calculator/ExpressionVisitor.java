package net.darmo_creations.build_utils.calculator;

import net.darmo_creations.build_utils.calculator.antlr4_parser.CalculatorBaseVisitor;
import net.darmo_creations.build_utils.calculator.antlr4_parser.CalculatorParser;
import net.darmo_creations.build_utils.calculator.nodes.expr.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Visitor for “exp” grammar rule. Generates a {@link Node} tree.
 */
public class ExpressionVisitor extends CalculatorBaseVisitor<Node> {
  /**
   * Maps unary operator symbols to the corresponding {@link Node} provider.
   */
  public static final Map<String, java.util.function.Function<Node, OperatorNode>> UNARY_OPERATORS = new HashMap<>();
  /**
   * Maps binary operator symbols to the corresponding {@link Node} provider.
   */
  public static final Map<String, BiFunction<Node, Node, OperatorNode>> BINARY_OPERATORS = new HashMap<>();

  static {
    UNARY_OPERATORS.put("-", MinusOperatorNode::new);
    UNARY_OPERATORS.put("!", NotOperatorNode::new);

    BINARY_OPERATORS.put("*", MultiplicationOperatorNode::new);
    BINARY_OPERATORS.put("/", DivisionOperatorNode::new);
    BINARY_OPERATORS.put("%", ModuloOperatorNode::new);
    BINARY_OPERATORS.put("+", AdditionOperatorNode::new);
    BINARY_OPERATORS.put("-", SubtractionOperatorNode::new);
    BINARY_OPERATORS.put("^", PowerOperatorNode::new);
    BINARY_OPERATORS.put("&", AndOperatorNode::new);
    BINARY_OPERATORS.put("|", OrOperatorNode::new);
    BINARY_OPERATORS.put("=", EqualToOperatorNode::new);
    BINARY_OPERATORS.put("!=", NotEqualToOperatorNode::new);
    BINARY_OPERATORS.put(">", GreaterThanOperatorNode::new);
    BINARY_OPERATORS.put(">=", GreaterThanOrEqualToOperatorNode::new);
    BINARY_OPERATORS.put("<", LessThanOperatorNode::new);
    BINARY_OPERATORS.put("<=", LessThanOrEqualToOperatorNode::new);
  }

  @Override
  public Node visitParentheses(CalculatorParser.ParenthesesContext ctx) {
    return this.visit(ctx.expr);
  }

  @Override
  public Node visitOperator(CalculatorParser.OperatorContext ctx) {
    Node left = this.visit(ctx.left);
    Node right = this.visit(ctx.right);
    String operator = ctx.operator.getText();
    return BINARY_OPERATORS.get(operator).apply(left, right);
  }

  @Override
  public Node visitUnaryOperator(CalculatorParser.UnaryOperatorContext ctx) {
    String operator = ctx.operator.getText();
    Node operand = this.visit(ctx.operand);
    if (operator.equals("+")) {
      return operand;
    } else {
      return UNARY_OPERATORS.get(operator).apply(operand);
    }
  }

  @Override
  public Node visitFunction(CalculatorParser.FunctionContext ctx) {
    String name = ctx.name.getText();
    List<Node> arguments = ctx.exp().stream().map(this::visit).collect(Collectors.toList());
    return new FunctionNode(name, arguments);
  }

  @Override
  public Node visitVariable(CalculatorParser.VariableContext ctx) {
    return new VariableNode(ctx.getText());
  }

  @Override
  public Node visitNumber(CalculatorParser.NumberContext ctx) {
    return new NumberNode(Double.parseDouble(ctx.getText()));
  }

  @Override
  public Node visitBoolean(CalculatorParser.BooleanContext ctx) {
    String value = ctx.value.getText();
    return new NumberNode(value.equals("true") ? 1 : 0);
  }
}
