// Generated from /home/damien/IdeaProjects/NaissanceE_mod/grammar/Calculator.g4 by ANTLR 4.9.2
package net.darmo_creations.build_utils.calculator.antlr4_parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CalculatorParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CalculatorVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CalculatorParser#start}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart(CalculatorParser.StartContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Expression}
	 * labeled alternative in {@link CalculatorParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(CalculatorParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VariableDef}
	 * labeled alternative in {@link CalculatorParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDef(CalculatorParser.VariableDefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FunctionDef}
	 * labeled alternative in {@link CalculatorParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDef(CalculatorParser.FunctionDefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Operator}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(CalculatorParser.OperatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Function}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(CalculatorParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Variable}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(CalculatorParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(CalculatorParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryOperator}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOperator(CalculatorParser.UnaryOperatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean(CalculatorParser.BooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Parentheses}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParentheses(CalculatorParser.ParenthesesContext ctx);
}
