package net.darmo_creations.build_utils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.darmo_creations.build_utils.BuildUtils;
import net.darmo_creations.build_utils.calculator.Calculator;
import net.darmo_creations.build_utils.calculator.Function;
import net.darmo_creations.build_utils.calculator.exceptions.*;
import net.darmo_creations.build_utils.calculator.nodes.StatementResult;
import net.darmo_creations.build_utils.commands.argument_types.CalculatorExpressionArgument;
import net.darmo_creations.build_utils.commands.argument_types.CalculatorVariableNameArgument;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.command.EnumArgument;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A command that lets users perform mathematical computations, define variables and simple functions.
 * A maximum of 100 variables and 100 functions can be defined by each user and in the global calculator.
 * <p>
 * A new calculator instance is created for each entity that executes the command.
 */
public class CalculatorCommand {
  private static final SimpleCommandExceptionType MISSING_PLAYER_ERROR = new SimpleCommandExceptionType(
      new TranslatableComponent("commands.calculator.error.missing_player")
  );
  private static final DynamicCommandExceptionType UNDEF_VAR_ERROR = new DynamicCommandExceptionType(
      varName -> new TranslatableComponent("commands.calculator.error.undefined_variable", varName)
  );
  private static final DynamicCommandExceptionType UNDEF_FUNC_ERROR = new DynamicCommandExceptionType(
      funcName -> new TranslatableComponent("commands.calculator.error.undefined_function", funcName)
  );
  private static final DynamicCommandExceptionType DEL_BUILTIN_CONST_ERROR = new DynamicCommandExceptionType(
      varName -> new TranslatableComponent("commands.calculator.error.delete_builtin_constant", varName)
  );
  private static final DynamicCommandExceptionType DEL_BUILTIN_FUNC_ERROR = new DynamicCommandExceptionType(
      funcName -> new TranslatableComponent("commands.calculator.error.delete_builtin_function", funcName)
  );
  private static final DynamicCommandExceptionType MAX_DECLARATIONS_ERROR = new DynamicCommandExceptionType(
      nb -> new TranslatableComponent("commands.calculator.error.max_declaration_quota_reached", nb)
  );
  private static final SimpleCommandExceptionType SYNTAX_ERROR = new SimpleCommandExceptionType(
      new TranslatableComponent("commands.calculator.error.syntax_error")
  );
  private static final Dynamic3CommandExceptionType INVALID_PARAMS_ERROR = new Dynamic3CommandExceptionType(
      (funcName, expected, actual) -> new TranslatableComponent("commands.calculator.error.invalid_function_params", funcName, expected, actual)
  );
  private static final DynamicCommandExceptionType MAX_DEPTH_ERROR = new DynamicCommandExceptionType(
      depth -> new TranslatableComponent("commands.calculator.error.max_depth_reached", depth)
  );
  private static final DynamicCommandExceptionType MATH_ERROR = new DynamicCommandExceptionType(
      message -> new TranslatableComponent("commands.calculator.error.math_error", message)
  );

  private static final Style BUILTINS_STYLE = Style.EMPTY.withColor(ChatFormatting.AQUA);

  public static final String VAR_SCOPE_ARG = "var_scope";
  public static final String EXPRESSION_ARG = "expression";
  public static final String TYPE_ARG = "type";
  public static final String LIST_TYPE_ARG = "list_type";

  /**
   * Register this command in the given dispatcher.
   */
  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    LiteralCommandNode<CommandSourceStack> command = dispatcher.register(buildCommand("calculator", false));
    LiteralCommandNode<CommandSourceStack> globalCommand = dispatcher.register(buildCommand("calculatorglobal", true));
    dispatcher.register(Commands.literal("c")
        .requires(commandSource -> commandSource.hasPermission(0))
        .redirect(command)
    );
    dispatcher.register(Commands.literal("cg")
        .requires(commandSource -> commandSource.hasPermission(2))
        .redirect(globalCommand)
    );
  }

  private static LiteralArgumentBuilder<CommandSourceStack> buildCommand(final String name, final boolean global) {
    return Commands.literal(name)
        .requires(commandSource -> commandSource.hasPermission(global ? 2 : 0))
        // Reset calculator
        .then(Commands.literal("reset")
            .executes(context -> {
              reset(context, global);
              return 1;
            }))
        // Delete variable
        .then(Commands.literal("delete")
            .then(Commands.argument(TYPE_ARG, EnumArgument.enumArgument(StructureType.class))
                .then(Commands.argument("name", CalculatorVariableNameArgument.variableName(global))
                    .executes(context -> {
                      delete(context, global);
                      return 1;
                    }))))
        // List variables
        .then(Commands.literal("list")
            .then(Commands.argument(VAR_SCOPE_ARG, EnumArgument.enumArgument(VariableScope.class))
                .then(Commands.argument(LIST_TYPE_ARG, EnumArgument.enumArgument(StructureTypes.class))
                    .executes(context -> list(context, global)))))
        // Evaluate expression
        .then(Commands.argument(EXPRESSION_ARG, CalculatorExpressionArgument.expression())
            .executes(context -> {
              evaluate(context, global);
              return 1;
            }));
  }

  /**
   * Return a calculator and its associated player’s name from the given context.
   *
   * @param context Command’s context.
   * @param global  Whether the method should return the global calculator
   *                or the one for the player that executed the command.
   * @return A pair containing the player’s name and its calculator.
   * Name is null if the global calculator is queried.
   */
  public static Pair<String, Calculator> getCalculator(final CommandContext<CommandSourceStack> context, final boolean global) throws CommandSyntaxException {
    String username = null;
    Calculator calculator;

    Optional<Entity> entity = Optional.ofNullable(context.getSource().getEntity());
    if (entity.isEmpty() || !(entity.get() instanceof Player)) {
      throw MISSING_PLAYER_ERROR.create();
    }
    if (global) {
      calculator = BuildUtils.CALCULATORS_MANAGER.getGlobalData();
    } else {
      Player player = (Player) entity.get();
      username = player.getGameProfile().getName();
      calculator = BuildUtils.CALCULATORS_MANAGER.getOrCreatePlayerData(player);
    }

    return new ImmutablePair<>(username, calculator);
  }

  /**
   * Reset the given calculator.
   *
   * @param context Context of the command.
   * @param global  Whether to use the global or player calculator.
   */
  private static void reset(final CommandContext<CommandSourceStack> context, final boolean global) throws CommandSyntaxException {
    Pair<String, Calculator> data = getCalculator(context, global);
    data.getRight().reset();
    TranslatableComponent component;
    if (global) {
      component = new TranslatableComponent("commands.calculator.global.feedback.reset");
    } else {
      component = new TranslatableComponent("commands.calculator.player.feedback.reset", data.getLeft());
    }
    context.getSource().sendSuccess(component, true);
  }

  /**
   * Delete the given variable or function.
   *
   * @param context Context of the command.
   * @param global  Whether to use the global or player calculator.
   */
  private static void delete(final CommandContext<CommandSourceStack> context, final boolean global) throws CommandSyntaxException {
    StructureType type = context.getArgument(TYPE_ARG, StructureType.class);
    String varName = CalculatorVariableNameArgument.getVariableName(context, "name");
    Pair<String, Calculator> data = getCalculator(context, global);
    Calculator calculator = data.getRight();

    try {
      switch (type) {
        case variable -> calculator.deleteVariable(varName);
        case function -> calculator.deleteFunction(varName);
      }
    } catch (UndefinedVariableException e) {
      throw UNDEF_VAR_ERROR.create(varName);
    } catch (BuiltinConstantDeletionAttemptException e) {
      throw DEL_BUILTIN_CONST_ERROR.create(varName);
    } catch (UndefinedFunctionException e) {
      throw UNDEF_FUNC_ERROR.create(varName);
    } catch (BuiltinFunctionDeletionAttemptException e) {
      throw DEL_BUILTIN_FUNC_ERROR.create(varName);
    }

    String key = String.format("commands.calculator.%s.feedback.%s_deleted", global ? "global" : "player", type);
    TranslatableComponent component;
    if (global) {
      component = new TranslatableComponent(key, varName);
    } else {
      component = new TranslatableComponent(key, varName, data.getLeft());
    }
    context.getSource().sendSuccess(component, true);
  }

  /**
   * List variables or functions.
   *
   * @param context Context of the command.
   * @param global  Whether to use the global or player calculator.
   * @return The number of listed elements.
   */
  private static int list(final CommandContext<CommandSourceStack> context, final boolean global) throws CommandSyntaxException {
    VariableScope variableScope = context.getArgument(VAR_SCOPE_ARG, VariableScope.class);
    StructureTypes type = context.getArgument(LIST_TYPE_ARG, StructureTypes.class);
    Pair<String, Calculator> data = getCalculator(context, global);
    Calculator calculator = data.getRight();

    List<Component> list = new ArrayList<>();

    switch (type) {
      case variables:
        switch (variableScope) {
          case all -> {
            list = listVariables(calculator.getBuiltinConstants(), true);
            list.addAll(listVariables(calculator.getVariables(), false));
          }
          case custom -> list = listVariables(calculator.getVariables(), false);
          case builtin -> list = listVariables(calculator.getBuiltinConstants(), true);
        }
        break;
      case functions:
        switch (variableScope) {
          case all -> {
            list = listFunctions(calculator.getBuiltinFunctions(), true);
            list.addAll(listFunctions(calculator.getFunctions(), false));
          }
          case custom -> list = listFunctions(calculator.getFunctions(), false);
          case builtin -> list = listFunctions(calculator.getBuiltinFunctions(), true);
        }
        break;
    }

    String key = String.format("commands.calculator.%s.feedback.list.%s_%s",
        global ? "global" : "player", variableScope, type);
    BaseComponent message;
    if (global) {
      message = new TranslatableComponent(key);
    } else {
      message = new TranslatableComponent(key, data.getLeft());
    }
    for (Component str : list) {
      message.append("\n").append(str);
    }
    context.getSource().sendSuccess(message, true);
    return list.size();
  }

  /**
   * Evaluates a statement then displays its result. If the statement is a single expression,
   * its value is stored in a variable named “_“.
   *
   * @param context Context of the command.
   * @param global  Whether to use the global or player calculator.
   */
  private static void evaluate(final CommandContext<CommandSourceStack> context, final boolean global) throws CommandSyntaxException {
    String expression = CalculatorExpressionArgument.getExpression(context, EXPRESSION_ARG);
    Calculator calculator = getCalculator(context, global).getRight();
    StatementResult result = null;
    CommandSyntaxException exception = null;

    try {
      result = calculator.evaluate(expression);
    } catch (MaxDefinitionsException e) {
      exception = MAX_DECLARATIONS_ERROR.create(e.getNumber());
    } catch (SyntaxErrorException e) {
      exception = SYNTAX_ERROR.create();
    } catch (UndefinedVariableException e) {
      exception = UNDEF_VAR_ERROR.create(e.getMessage());
    } catch (UndefinedFunctionException e) {
      exception = UNDEF_FUNC_ERROR.create(e.getMessage());
    } catch (InvalidFunctionArguments e) {
      exception = INVALID_PARAMS_ERROR.create(e.getFunctionName(), e.getExpected(), e.getActual());
    } catch (MaxDepthReachedException e) {
      exception = MAX_DEPTH_ERROR.create(e.getDepth());
    } catch (ArithmeticException e) {
      exception = MATH_ERROR.create(e.getMessage());
    }

    // Display what the player just typed
    context.getSource().sendSuccess(new TextComponent("$ " + expression), true);
    if (exception != null) {
      throw exception;
    } else {
      //noinspection ConstantConditions
      context.getSource().sendSuccess(new TextComponent(result.getStatus())
          .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)), true);
      // Store result in a special variable
      result.getValue().ifPresent(v -> calculator.setVariable("_", v));
    }
  }

  /**
   * Generate a list of text components for a mapping of variables.
   *
   * @param variables The variables to format.
   * @param builtin   Whether the variables are builtin; modifies styling.
   * @return The list of text components.
   */
  private static List<Component> listVariables(final Map<String, Double> variables, final boolean builtin) {
    return variables.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .map(e -> getTextComponent(String.format(Locale.ENGLISH, "%s = %f", e.getKey(), e.getValue()), builtin))
        .collect(Collectors.toList());
  }

  /**
   * Generate a list of text components for a list of functions.
   *
   * @param functions The functions to format.
   * @param builtin   Whether the functions are builtin; modifies styling.
   * @return The list of text components.
   */
  private static List<Component> listFunctions(final List<Function> functions, final boolean builtin) {
    return functions.stream()
        .sorted(Comparator.comparing(Function::getName))
        .map(f -> getTextComponent(f.toString(), builtin))
        .collect(Collectors.toList());
  }

  /**
   * Return the text component for the given text.
   *
   * @param text    Text to format.
   * @param builtin Whether to apply the “builtin” style.
   * @return The text component.
   */
  private static Component getTextComponent(final String text, final boolean builtin) {
    TextComponent component = new TextComponent(text);
    if (builtin) {
      component.setStyle(BUILTINS_STYLE);
    }
    return component;
  }

  /**
   * Possible values for data structures.
   */
  public enum StructureType {
    variable, function
  }

  /**
   * Possible values for data structures (for listing).
   */
  public enum StructureTypes {
    variables, functions
  }

  /**
   * Possible values for variables/functions scope.
   */
  public enum VariableScope {
    all, builtin, custom
  }
}
