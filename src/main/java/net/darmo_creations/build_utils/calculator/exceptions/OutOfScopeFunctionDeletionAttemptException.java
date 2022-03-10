package net.darmo_creations.build_utils.calculator.exceptions;

/**
 * Error raised when there was an attempt to delete a function defined in parent scopes.
 */
public class OutOfScopeFunctionDeletionAttemptException extends EvaluationException {
  /**
   * Create an exception.
   *
   * @param variableName Constant’s name.
   */
  public OutOfScopeFunctionDeletionAttemptException(final String variableName) {
    super(variableName);
  }
}
