package net.darmo_creations.build_utils.calculator.exceptions;

import net.darmo_creations.build_utils.calculator.Calculator;
import net.darmo_creations.build_utils.calculator.Parser;

/**
 * Error raised when a syntax error occurs while an expression is being parsed by the {@link Calculator}
 * and {@link Parser}.
 */
public class SyntaxErrorException extends RuntimeException {
  public SyntaxErrorException(String message) {
    super(message);
  }
}
