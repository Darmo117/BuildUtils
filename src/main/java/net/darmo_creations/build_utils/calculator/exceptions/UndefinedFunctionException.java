package net.darmo_creations.build_utils.calculator.exceptions;

import net.darmo_creations.build_utils.calculator.Calculator;

/**
 * Error raised when an undefined function is encountered
 * while an expression is being evaluated by the {@link Calculator}.
 */
public class UndefinedFunctionException extends EvaluationException {
  public UndefinedFunctionException(String identifier) {
    super(identifier);
  }
}
