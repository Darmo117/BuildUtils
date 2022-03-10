package net.darmo_creations.build_utils.calculator.exceptions;

/**
 * Error raised when there was an attempt to define a new variable or function after maximum quota has been reached.
 */
public class MaxDefinitionsException extends EvaluationException {
  private final int number;

  /**
   * Create an exception.
   *
   * @param number Maximum definition quota.
   */
  public MaxDefinitionsException(int number) {
    super("" + number);
    this.number = number;
  }

  /**
   * Return the maximum definition quota.
   */
  public int getNumber() {
    return this.number;
  }
}
