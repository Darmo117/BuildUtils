package net.darmo_creations.build_utils.calculator.nodes;

import java.util.Objects;
import java.util.Optional;

/**
 * A simple wrapper class for returned by {@link Statement}s.
 */
@SuppressWarnings("ClassCanBeRecord")
public class StatementResult {
  private final String status;
  private final Double value;

  /**
   * Create a result object.
   *
   * @param status Statement’s execution status.
   * @param value  Statement’s value. May be null.
   */
  public StatementResult(final String status, final Double value) {
    this.status = Objects.requireNonNull(status);
    this.value = value;
  }

  /**
   * Return statement’s execution status.
   */
  public String getStatus() {
    return this.status;
  }

  /**
   * Return statement’s value.
   */
  public Optional<Double> getValue() {
    return Optional.ofNullable(this.value);
  }
}
