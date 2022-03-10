package net.darmo_creations.build_utils.commands.argument_types;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;

/**
 * Argument type that represents a calculator’s expression.
 */
public class CalculatorExpressionArgument implements ArgumentType<String> {
  /**
   * Generate a new argument instance.
   */
  public static CalculatorExpressionArgument expression() {
    return new CalculatorExpressionArgument();
  }

  /**
   * Return the calculator expression for the given argument.
   *
   * @param context Command’s context to get the name from.
   * @param argName Argument’s name.
   * @return Argument’s value.
   */
  public static String getExpression(final CommandContext<?> context, final String argName) {
    return context.getArgument(argName, String.class);
  }

  @Override
  public String parse(final StringReader reader) {
    String expr = reader.getString().substring(reader.getCursor(), reader.getTotalLength());
    reader.setCursor(reader.getTotalLength());
    return expr;
  }
}
