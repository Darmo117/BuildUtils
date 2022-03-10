package net.darmo_creations.build_utils.calculator;

import java.util.List;

/**
 * Builtin functions are predefined functions common to all calculator instances.
 */
public class BuiltinFunction extends Function {
  private final java.util.function.Function<Scope, Double> function;

  /**
   * Create a builtin function.
   *
   * @param name           Function’s name.
   * @param parameterNames Function’s parameter names.
   * @param function       The Java function.
   */
  public BuiltinFunction(final String name, final List<String> parameterNames,
                         final java.util.function.Function<Scope, Double> function) {
    super(name, parameterNames);
    this.function = function;
  }

  @Override
  protected double evaluateImpl(final Scope scope) {
    return this.function.apply(scope);
  }

  @Override
  public String toString() {
    return String.format("%s(%s) -> <builtin>", this.getName(), String.join(", ", this.getParameterNames()));
  }
}
