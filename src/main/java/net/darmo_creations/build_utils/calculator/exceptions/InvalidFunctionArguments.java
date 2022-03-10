package net.darmo_creations.build_utils.calculator.exceptions;

/**
 * Error raised when a function was called with invalid parameters.
 */
public class InvalidFunctionArguments extends EvaluationException {
  private final int expected;
  private final int actual;

  /**
   * Create an exception for the given function.
   *
   * @param functionName Function’s name.
   * @param expected     Expected number of parameters.
   * @param actual       Actual number of parameters.
   */
  public InvalidFunctionArguments(final String functionName, final int expected, final int actual) {
    super(functionName);
    this.expected = expected;
    this.actual = actual;
  }

  /**
   * Return function’s name.
   */
  public String getFunctionName() {
    return super.getMessage();
  }

  /**
   * Return expected number of parameters.
   */
  public int getExpected() {
    return this.expected;
  }

  /**
   * Return actual number of parameters.
   */
  public int getActual() {
    return this.actual;
  }
}
