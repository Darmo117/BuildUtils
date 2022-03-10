package net.darmo_creations.build_utils.calculator.exceptions;

/**
 * Error raised when the maximum function call depth is reached.
 */
public class MaxDepthReachedException extends EvaluationException {
  /**
   * Create an error.
   *
   * @param depth The depth at which this error was raised.
   */
  public MaxDepthReachedException(final int depth) {
    super("" + depth);
  }

  /**
   * The depth at which this error was raised.
   */
  public int getDepth() {
    return Integer.parseInt(this.getMessage());
  }
}
