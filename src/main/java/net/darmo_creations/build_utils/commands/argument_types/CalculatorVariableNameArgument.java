package net.darmo_creations.build_utils.commands.argument_types;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.darmo_creations.build_utils.calculator.Calculator;
import net.darmo_creations.build_utils.calculator.Function;
import net.darmo_creations.build_utils.commands.CalculatorCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.network.FriendlyByteBuf;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Argument type that represents a calculator’s variable/function identifier.
 */
@SuppressWarnings("ClassCanBeRecord")
public class CalculatorVariableNameArgument implements ArgumentType<String> {
  /**
   * Generate a new argument instance.
   */
  public static CalculatorVariableNameArgument variableName(final boolean useGlobal) {
    return new CalculatorVariableNameArgument(useGlobal);
  }

  /**
   * Return the variable/function name value for the given argument.
   *
   * @param context Command’s context to get the name from.
   * @param argName Argument’s name.
   * @return Argument’s value.
   */
  public static String getVariableName(final CommandContext<?> context, final String argName) {
    return context.getArgument(argName, String.class);
  }

  private final boolean useGlobal;

  private CalculatorVariableNameArgument(final boolean useGlobal) {
    this.useGlobal = useGlobal;
  }

  @Override
  public String parse(final StringReader reader) {
    return reader.readUnquotedString();
  }

  @Override
  public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
    if (context.getSource() instanceof CommandSourceStack) {
      //noinspection unchecked
      CommandContext<CommandSourceStack> ctx = (CommandContext<CommandSourceStack>) context;
      CalculatorCommand.StructureType scope = ctx.getArgument("type", CalculatorCommand.StructureType.class);
      Calculator calculator;
      try {
        calculator = CalculatorCommand.getCalculator(ctx, this.useGlobal).getRight();
      } catch (CommandSyntaxException e) {
        return Suggestions.empty();
      }
      return SharedSuggestionProvider.suggest((switch (scope) {
        case variable -> calculator.getVariables().keySet().stream();
        case function -> calculator.getFunctions().stream().map(Function::getName);
      }).sorted().collect(Collectors.toList()), builder);
    } else if (context.getSource() instanceof SharedSuggestionProvider source) {
      //noinspection unchecked
      return source.customSuggestion((CommandContext<SharedSuggestionProvider>) context, builder);
    }
    return Suggestions.empty();
  }

  /**
   * Serializer for this argument type.
   */
  public static class Serializer implements ArgumentSerializer<CalculatorVariableNameArgument> {
    @Override
    public void serializeToNetwork(CalculatorVariableNameArgument arg, FriendlyByteBuf buf) {
      buf.writeByte(arg.useGlobal ? 1 : 0);
    }

    @Override
    public CalculatorVariableNameArgument deserializeFromNetwork(FriendlyByteBuf buf) {
      return new CalculatorVariableNameArgument(buf.readByte() != 0);
    }

    @Override
    public void serializeToJson(CalculatorVariableNameArgument arg, JsonObject jsonObject) {
      jsonObject.addProperty("amount", arg.useGlobal ? "multiple" : "single");
    }
  }
}
