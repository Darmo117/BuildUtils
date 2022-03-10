package net.darmo_creations.build_utils.calculator;

import net.darmo_creations.build_utils.NBTSerializable;
import net.darmo_creations.build_utils.calculator.exceptions.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.*;

/**
 * The scope holds the definitions of builtin constants and functions,
 * and user-defined variables and functions.
 */
public class Scope implements NBTSerializable {
  public static final int MAX_CALL_DEPTH = 100;

  private static final String VARIABLES_KEY = "Variables";
  private static final String VARIABLE_NAME_KEY = "Name";
  private static final String VARIABLE_VALUE_KEY = "Value";
  private static final String FUNCTIONS_KEY = "Functions";
  private static final String FUNCTION_NAME_KEY = "Name";
  private static final String FUNCTION_EXPRESSION_KEY = "Expression";

  private final String name;
  private final int maxAllowedDefinitions;
  private final Map<String, Double> builtinConstants;
  private final Map<String, Double> variables;
  private final Map<String, Function> builtinFunctions;
  private final Map<String, Function> functions;

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  private final Optional<Scope> globalScope;
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  private final Optional<Scope> parentScope;

  /**
   * Create a scope with no parents.
   *
   * @param maxAllowedDefinitions The maximum allowed number of variables and functions.
   */
  public Scope(final int maxAllowedDefinitions) {
    if (maxAllowedDefinitions < 0) {
      throw new IllegalArgumentException("maxAllowedDefinitions must be >= 0");
    }
    this.name = "<global>";
    this.maxAllowedDefinitions = maxAllowedDefinitions;
    this.globalScope = Optional.empty();
    this.parentScope = Optional.empty();
    this.variables = new HashMap<>();
    this.functions = new HashMap<>();
    this.builtinConstants = new HashMap<>();
    this.builtinFunctions = new HashMap<>();
    this.initBuiltins();
  }

  /**
   * Create a scope with a parent scope.
   *
   * @param name        This scope’s name.
   * @param globalScope The global scope, should be the same as parentScope.
   * @param parentScope This scope’s parent.
   */
  public Scope(final String name, final Scope globalScope, final Scope parentScope) {
    if (globalScope != parentScope.getGlobalScope()) {
      throw new IllegalArgumentException("global scope is different from parent’s global scope");
    }
    this.name = name;
    this.maxAllowedDefinitions = parentScope.getMaxAllowedDefinitions();
    this.globalScope = Optional.of(globalScope);
    this.parentScope = Optional.of(parentScope);
    this.variables = new HashMap<>();
    this.functions = new HashMap<>();
    this.builtinConstants = new HashMap<>();
    this.builtinFunctions = new HashMap<>();
  }

  /**
   * Return the name of this scope.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Return the maximum allowed number of variables and functions.
   */
  public int getMaxAllowedDefinitions() {
    return this.maxAllowedDefinitions;
  }

  /**
   * Indicate whether this scope is the global scope.
   */
  public boolean isGlobal() {
    return this.name.equals("<global>");
  }

  /**
   * Return the global scope for this scope.
   */
  public Scope getGlobalScope() {
    return this.globalScope.orElse(this);
  }

  /**
   * Initialize the builtin constants and functions.
   */
  private void initBuiltins() {
    this.builtinConstants.put("pi", Math.PI);

    BuiltinFunction[] functions = {
        new BuiltinFunction("floor", Collections.singletonList("_x_"),
            values -> Math.floor(values.getVariable("_x_"))),
        new BuiltinFunction("ceil", Collections.singletonList("_x_"),
            values -> Math.ceil(values.getVariable("_x_"))),
        new BuiltinFunction("sqrt", Collections.singletonList("_x_"),
            values -> Math.sqrt(values.getVariable("_x_"))),
        new BuiltinFunction("cbrt", Collections.singletonList("_x_"),
            values -> Math.cbrt(values.getVariable("_x_"))),
        new BuiltinFunction("exp", Collections.singletonList("_x_"),
            values -> Math.exp(values.getVariable("_x_"))),
        new BuiltinFunction("cos", Collections.singletonList("_x_"),
            values -> Math.cos(values.getVariable("_x_"))),
        new BuiltinFunction("sin", Collections.singletonList("_x_"),
            values -> Math.sin(values.getVariable("_x_"))),
        new BuiltinFunction("tan", Collections.singletonList("_x_"),
            values -> Math.tan(values.getVariable("_x_"))),
        new BuiltinFunction("acos", Collections.singletonList("_x_"),
            values -> Math.acos(values.getVariable("_x_"))),
        new BuiltinFunction("asin", Collections.singletonList("_x_"),
            values -> Math.asin(values.getVariable("_x_"))),
        new BuiltinFunction("atan", Collections.singletonList("_x_"),
            values -> Math.atan(values.getVariable("_x_"))),
        new BuiltinFunction("atan2", Arrays.asList("_x1_", "_x2_"),
            values -> Math.atan2(values.getVariable("_x1_"), values.getVariable("_x2_"))),
        new BuiltinFunction("log", Collections.singletonList("_x_"),
            values -> Math.log(values.getVariable("_x_"))),
        new BuiltinFunction("log10", Collections.singletonList("_x_"),
            values -> Math.log10(values.getVariable("_x_"))),
        new BuiltinFunction("abs", Collections.singletonList("_x_"),
            values -> Math.abs(values.getVariable("_x_"))),
        new BuiltinFunction("degrees", Collections.singletonList("_x_"),
            values -> Math.toDegrees(values.getVariable("_x_"))),
        new BuiltinFunction("radians", Collections.singletonList("_x_"),
            values -> Math.toRadians(values.getVariable("_x_"))),
        new BuiltinFunction("round", Collections.singletonList("_x_"),
            values -> (double) Math.round(values.getVariable("_x_"))),
        new BuiltinFunction("sign", Collections.singletonList("_x_"),
            values -> Math.signum(values.getVariable("_x_"))),
        new BuiltinFunction("max", Arrays.asList("_x1_", "_x2_"),
            values -> Math.max(values.getVariable("_x1_"), values.getVariable("_x2_"))),
        new BuiltinFunction("min", Arrays.asList("_x1_", "_x2_"),
            values -> Math.min(values.getVariable("_x1_"), values.getVariable("_x2_"))),
    };
    for (BuiltinFunction f : functions) {
      this.builtinFunctions.put(f.getName(), f);
    }
  }

  /**
   * Indicate whether the given variable or constant exists.
   *
   * @param name             Variable/constant’s name.
   * @param checkGlobalScope Whether to check in the global scope.
   * @return True if a user-defined variable or a builtin constant with this name exists.
   */
  public boolean variableExists(final String name, boolean checkGlobalScope) {
    return this.variables.containsKey(name) || this.builtinConstants.containsKey(name)
        || checkGlobalScope && this.globalScope.map(s -> s.variableExists(name, false)).orElse(false);
  }

  /**
   * Return the value of the variable or constant with the given name.
   *
   * @param name Variable/constant’s name.
   * @return Variable/constant’s value.
   * @throws UndefinedVariableException If no variable nor constant with this name exist.
   */
  public double getVariable(final String name) throws UndefinedVariableException {
    if (this.variables.containsKey(name)) {
      return this.variables.get(name);
    } else if (this.globalScope.isPresent()) {
      return this.globalScope.get().getVariable(name);
    } else if (this.builtinConstants.containsKey(name)) {
      return this.builtinConstants.get(name);
    } else {
      throw new UndefinedVariableException(name);
    }
  }

  /**
   * Set the value of a variable.
   *
   * @param name  Variable’s name.
   * @param value Variable’s value.
   * @return Variable’s previous value if it was already defined.
   * @throws MaxDefinitionsException If the maximum quota of variables definitions has been reached.
   */
  @SuppressWarnings("UnusedReturnValue")
  public Optional<Double> setVariable(String name, double value) throws MaxDefinitionsException {
    if (this.variables.size() == this.maxAllowedDefinitions
        && !this.variables.containsKey(name)) {
      throw new MaxDefinitionsException(this.maxAllowedDefinitions);
    }
    return Optional.ofNullable(this.variables.put(name, value));
  }

  /**
   * Delete the given variable.
   *
   * @param name Variable’s name.
   * @return Variable’s value if it was already defined.
   * @throws UndefinedVariableException                 If no variable nor constant with this name exist.
   * @throws OutOfScopeVariableDeletionAttemptException If the variable or constant is defined in a parent scope.
   * @throws BuiltinConstantDeletionAttemptException    If no variable with this name exist but a builtin constant does.
   */
  @SuppressWarnings("UnusedReturnValue")
  public double deleteVariable(String name)
      throws UndefinedVariableException, OutOfScopeVariableDeletionAttemptException, BuiltinConstantDeletionAttemptException {
    if (!this.variableExists(name, true)) {
      throw new UndefinedVariableException(name);
    } else if (!this.variableExists(name, false)) {
      throw new OutOfScopeVariableDeletionAttemptException(name);
    } else if (!this.variables.containsKey(name) && this.builtinConstants.containsKey(name)) {
      throw new BuiltinConstantDeletionAttemptException(name);
    }
    return this.variables.remove(name);
  }

  /**
   * Return a mapping of all variables defined in this scope.
   */
  public Map<String, Double> getVariables() {
    return new HashMap<>(this.variables);
  }

  /**
   * Return a mapping of all builtin constants.
   */
  public Map<String, Double> getBuiltinConstants() {
    if (this.isGlobal()) {
      return new HashMap<>(this.builtinConstants);
    } else {
      //noinspection OptionalGetWithoutIsPresent
      return this.globalScope.get().getBuiltinConstants();
    }
  }

  /**
   * Indicate whether the given function exists.
   *
   * @param name             Function’s name.
   * @param checkGlobalScope Whether to check in the global scope.
   * @return True if a function with this name exists.
   */
  public boolean functionExists(final String name, boolean checkGlobalScope) {
    return this.functions.containsKey(name) || this.builtinFunctions.containsKey(name)
        || checkGlobalScope && this.globalScope.map(s -> s.functionExists(name, false)).orElse(false);
  }

  /**
   * Return the function with the given name.
   *
   * @param name Function’s name.
   * @return The function.
   * @throws UndefinedFunctionException If no function with this name exists.
   */
  public Function getFunction(final String name) throws UndefinedFunctionException {
    if (this.functions.containsKey(name)) {
      return this.functions.get(name);
    } else if (this.globalScope.isPresent()) {
      return this.globalScope.get().getFunction(name);
    } else if (this.builtinFunctions.containsKey(name)) {
      return this.builtinFunctions.get(name);
    } else {
      throw new UndefinedFunctionException(name);
    }
  }

  /**
   * Define a function. Overrides any function with the same name previously defined by a user.
   *
   * @param function The function.
   * @return The function with the same name that was overwritten by the given function if any.
   * @throws MaxDefinitionsException If the maximum quota of functions definitions has been reached.
   */
  @SuppressWarnings("UnusedReturnValue")
  public Optional<Function> setFunction(Function function) throws MaxDefinitionsException {
    if (this.functions.size() == this.maxAllowedDefinitions
        && !this.functions.containsKey(function.getName())) {
      throw new MaxDefinitionsException(this.maxAllowedDefinitions);
    }
    return Optional.ofNullable(this.functions.put(function.getName(), function));
  }

  /**
   * Delete the function with the given name.
   *
   * @param name Function’s name.
   * @return The function that was deleted.
   * @throws UndefinedFunctionException                 If no function with this name exists.
   * @throws OutOfScopeFunctionDeletionAttemptException If the function is defined in a parent scope.
   * @throws BuiltinFunctionDeletionAttemptException    If no user-defined function with this name exists but a builtin one does.
   */
  @SuppressWarnings("UnusedReturnValue")
  public Function deleteFunction(String name)
      throws UndefinedFunctionException, OutOfScopeFunctionDeletionAttemptException, BuiltinFunctionDeletionAttemptException {
    if (!this.functionExists(name, true)) {
      throw new UndefinedFunctionException(name);
    } else if (!this.functionExists(name, false)) {
      throw new OutOfScopeFunctionDeletionAttemptException(name);
    } else if (!this.functions.containsKey(name) && this.builtinFunctions.containsKey(name)) {
      throw new BuiltinConstantDeletionAttemptException(name);
    }
    return this.functions.remove(name);
  }

  /**
   * Return a mapping of all user-defined functions.
   */
  public Map<String, Function> getFunctions() {
    return new HashMap<>(this.functions);
  }

  /**
   * Return a mapping of all builtin functions.
   */
  public Map<String, Function> getBuiltinFunctions() {
    if (this.isGlobal()) {
      return new HashMap<>(this.builtinFunctions);
    } else {
      //noinspection OptionalGetWithoutIsPresent
      return this.globalScope.get().getBuiltinFunctions();
    }
  }

  /**
   * Deletes all variables and functions defined in this scope.
   */
  public void reset() {
    this.variables.clear();
    this.functions.clear();
  }

  /**
   * Return the list of names of all scopes in this stack.
   */
  public List<String> getStackTrace() {
    List<String> trace;
    if (!this.parentScope.isPresent()) {
      trace = new ArrayList<>();
    } else {
      trace = this.parentScope.get().getStackTrace();
    }
    trace.add(this.name);
    return trace;
  }

  @Override
  public CompoundTag writeToNBT() {
    if (!this.isGlobal()) {
      throw new UnsupportedOperationException("cannot serialize non-global scope");
    }
    CompoundTag tag = new CompoundTag();
    ListTag variables = new ListTag();
    for (Map.Entry<String, Double> entry : this.variables.entrySet()) {
      CompoundTag item = new CompoundTag();
      item.putString(VARIABLE_NAME_KEY, entry.getKey());
      item.putDouble(VARIABLE_VALUE_KEY, entry.getValue());
      variables.add(item);
    }
    tag.put(VARIABLES_KEY, variables);
    ListTag functions = new ListTag();
    for (Map.Entry<String, Function> entry : this.functions.entrySet()) {
      CompoundTag item = new CompoundTag();
      item.putString(FUNCTION_NAME_KEY, entry.getKey());
      item.put(FUNCTION_EXPRESSION_KEY, entry.getValue().writeToNBT());
      functions.add(item);
    }
    tag.put(FUNCTIONS_KEY, functions);
    return tag;
  }

  @Override
  public void readFromNBT(CompoundTag tag) {
    if (!this.isGlobal()) {
      throw new UnsupportedOperationException("cannot deserialize non-global scope");
    }
    this.variables.clear();
    for (Tag item : tag.getList(VARIABLES_KEY, new CompoundTag().getId())) {
      CompoundTag c = (CompoundTag) item;
      this.variables.put(c.getString(VARIABLE_NAME_KEY), c.getDouble(VARIABLE_VALUE_KEY));
    }
    this.functions.clear();
    for (Tag item : tag.getList(FUNCTIONS_KEY, new CompoundTag().getId())) {
      CompoundTag c = (CompoundTag) item;
      this.functions.put(c.getString(FUNCTION_NAME_KEY), new UserFunction(c.getCompound(FUNCTION_EXPRESSION_KEY)));
    }
  }
}
