package net.darmo_creations.build_utils.calculator.exceptions;

/**
 * Error raised when there was an attempt to delete a builtin function.
 */
public class BuiltinFunctionDeletionAttemptException extends EvaluationException {
  /**
   * Create an exception.
   *
   * @param functionName Function’s name.
   */
  public BuiltinFunctionDeletionAttemptException(final String functionName) {
    super(functionName);
  }
}
