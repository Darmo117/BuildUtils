// Generated from /home/damien/IdeaProjects/NaissanceE_mod/grammar/Calculator.g4 by ANTLR 4.9.2
package net.darmo_creations.build_utils.calculator.antlr4_parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CalculatorParser}.
 */
public interface CalculatorListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CalculatorParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(CalculatorParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link CalculatorParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(CalculatorParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Expression}
	 * labeled alternative in {@link CalculatorParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterExpression(CalculatorParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Expression}
	 * labeled alternative in {@link CalculatorParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitExpression(CalculatorParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VariableDef}
	 * labeled alternative in {@link CalculatorParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterVariableDef(CalculatorParser.VariableDefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VariableDef}
	 * labeled alternative in {@link CalculatorParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitVariableDef(CalculatorParser.VariableDefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FunctionDef}
	 * labeled alternative in {@link CalculatorParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDef(CalculatorParser.FunctionDefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FunctionDef}
	 * labeled alternative in {@link CalculatorParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDef(CalculatorParser.FunctionDefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Operator}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterOperator(CalculatorParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Operator}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitOperator(CalculatorParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Function}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterFunction(CalculatorParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Function}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitFunction(CalculatorParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Variable}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterVariable(CalculatorParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Variable}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitVariable(CalculatorParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Number}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterNumber(CalculatorParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitNumber(CalculatorParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryOperator}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOperator(CalculatorParser.UnaryOperatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryOperator}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOperator(CalculatorParser.UnaryOperatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterBoolean(CalculatorParser.BooleanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitBoolean(CalculatorParser.BooleanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Parentheses}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterParentheses(CalculatorParser.ParenthesesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Parentheses}
	 * labeled alternative in {@link CalculatorParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitParentheses(CalculatorParser.ParenthesesContext ctx);
}
