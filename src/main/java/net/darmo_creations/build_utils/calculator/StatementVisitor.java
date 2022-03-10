package net.darmo_creations.build_utils.calculator;

import net.darmo_creations.build_utils.calculator.antlr4_parser.CalculatorBaseVisitor;
import net.darmo_creations.build_utils.calculator.antlr4_parser.CalculatorParser;
import net.darmo_creations.build_utils.calculator.nodes.AssignVariableStatement;
import net.darmo_creations.build_utils.calculator.nodes.DefineFunctionStatement;
import net.darmo_creations.build_utils.calculator.nodes.ExpressionStatement;
import net.darmo_creations.build_utils.calculator.nodes.Statement;
import net.darmo_creations.build_utils.calculator.nodes.expr.Node;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Visitor for “start” and “statement” grammar rules. Generates a {@link Statement}.
 */
public class StatementVisitor extends CalculatorBaseVisitor<Statement> {
  @Override
  public Statement visitStart(CalculatorParser.StartContext ctx) {
    return this.visit(ctx.statement());
  }

  @Override
  public Statement visitExpression(CalculatorParser.ExpressionContext ctx) {
    return new ExpressionStatement(this.visitExpression(ctx.exp()));
  }

  @Override
  public Statement visitVariableDef(CalculatorParser.VariableDefContext ctx) {
    return new AssignVariableStatement(ctx.var.getText(), this.visitExpression(ctx.expr));
  }

  @Override
  public Statement visitFunctionDef(CalculatorParser.FunctionDefContext ctx) {
    List<String> parameters = ctx.ID().stream().skip(1).map(ParseTree::getText).collect(Collectors.toList());
    return new DefineFunctionStatement(ctx.fname.getText(), parameters, this.visitExpression(ctx.expr));
  }

  /**
   * Generate Node tree for the given expression tree.
   */
  private Node visitExpression(ParseTree expression) {
    return new ExpressionVisitor().visit(expression);
  }
}
