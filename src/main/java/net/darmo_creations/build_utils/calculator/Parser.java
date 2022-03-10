package net.darmo_creations.build_utils.calculator;

import net.darmo_creations.build_utils.calculator.antlr4_parser.CalculatorLexer;
import net.darmo_creations.build_utils.calculator.antlr4_parser.CalculatorParser;
import net.darmo_creations.build_utils.calculator.exceptions.SyntaxErrorException;
import net.darmo_creations.build_utils.calculator.nodes.Statement;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.util.BitSet;

/**
 * The parser for the calculator. Handles raw expression parsing by constructing a statement and expression tree.
 */
public class Parser {
  /**
   * Parses the given expression.
   *
   * @param expression The expression to parse.
   * @return The corresponding statement.
   * @throws SyntaxErrorException If any syntax error was encountered.
   */
  public static Statement parse(final String expression) throws SyntaxErrorException {
    CalculatorLexer lexer = new CalculatorLexer(CharStreams.fromString(expression));
    CalculatorParser parser = new CalculatorParser(new CommonTokenStream(lexer));
    ErrorListener errorListener = new ErrorListener();
    parser.addErrorListener(errorListener);
    return new StatementVisitor().visit(parser.start());
  }

  /**
   * Simple error listener to throw syntax errors instead of just logging them.
   */
  private static class ErrorListener implements ANTLRErrorListener {
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
      throw new SyntaxErrorException(msg);
    }

    @Override
    public void reportAmbiguity(org.antlr.v4.runtime.Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
    }

    @Override
    public void reportAttemptingFullContext(org.antlr.v4.runtime.Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
    }

    @Override
    public void reportContextSensitivity(org.antlr.v4.runtime.Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
    }
  }
}
