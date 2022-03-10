package net.darmo_creations.build_utils.calculator;

import net.darmo_creations.build_utils.DataManager;
import net.darmo_creations.build_utils.ManagedData;
import net.darmo_creations.build_utils.calculator.exceptions.*;
import net.darmo_creations.build_utils.calculator.nodes.StatementResult;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A calculator that can parse and evaluate simple mathematical expressions and declare variables and functions.
 */
public class Calculator implements ManagedData<Calculator> {
  public static final int MAX_VARS_PER_PLAYER = 100;

  private static final String SCOPE_KEY = "Scope";

  private DataManager<Calculator> manager;
  private Scope scope;

  /**
   * Create a calculator.
   */
  public Calculator() {
    this.scope = new Scope(MAX_VARS_PER_PLAYER);
  }

  /**
   * Return a mapping of all user-defined variables.
   */
  public Map<String, Double> getVariables() {
    return this.scope.getVariables();
  }

  /**
   * Return a mapping of all builtin constants.
   */
  public Map<String, Double> getBuiltinConstants() {
    return this.scope.getBuiltinConstants();
  }

  /**
   * Return a list of all user-defined functions.
   */
  public List<Function> getFunctions() {
    return new ArrayList<>(this.scope.getFunctions().values());
  }

  /**
   * Return a list of all builtin functions.
   */
  public List<Function> getBuiltinFunctions() {
    return new ArrayList<>(this.scope.getBuiltinFunctions().values());
  }

  /**
   * Evaluate an expression then return its result.
   *
   * @param expression The expression to parse and evaluate.
   * @return A {@link StatementResult} object containing the status and value of the execution.
   * @throws ArithmeticException If any math error occurs.
   */
  public StatementResult evaluate(final String expression)
      throws SyntaxErrorException, ArithmeticException, EvaluationException {
    StatementResult execute = Parser.parse(expression).execute(this.scope);
    this.manager.markDirty();
    return execute;
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
    Optional<Double> oldValue = this.scope.setVariable(name, value);
    this.manager.markDirty();
    return oldValue;
  }

  /**
   * Delete the variable that has the given name.
   *
   * @param name Name of the variable.
   * @return The value of the variable.
   * @throws UndefinedVariableException              If no variable with this name exists.
   * @throws BuiltinConstantDeletionAttemptException If the identifier corresponds to a builtin variable.
   */
  @SuppressWarnings("UnusedReturnValue")
  public double deleteVariable(String name)
      throws UndefinedVariableException, BuiltinConstantDeletionAttemptException {
    double oldValue = this.scope.deleteVariable(name);
    this.manager.markDirty();
    return oldValue;
  }

  /**
   * Delete the function that has the given name.
   *
   * @param name Name of the function.
   * @return The function.
   * @throws UndefinedVariableException              If no function with this name exists.
   * @throws BuiltinFunctionDeletionAttemptException If the identifier corresponds to a builtin function.
   */
  @SuppressWarnings("UnusedReturnValue")
  public Function deleteFunction(String name)
      throws UndefinedVariableException, BuiltinFunctionDeletionAttemptException {
    Function function = this.scope.deleteFunction(name);
    this.manager.markDirty();
    return function;
  }

  /**
   * Reset the context of this calculator, i.e. delete all user-defined variables and functions.
   */
  public void reset() {
    this.scope.reset();
    this.manager.markDirty();
  }

  @Override
  public CompoundTag writeToNBT() {
    CompoundTag tag = new CompoundTag();
    tag.put(SCOPE_KEY, this.scope.writeToNBT());
    return tag;
  }

  @Override
  public void readFromNBT(CompoundTag tag) {
    this.scope = new Scope(MAX_VARS_PER_PLAYER);
    this.scope.readFromNBT(tag.getCompound(SCOPE_KEY));
  }

  @Override
  public void setManager(DataManager<Calculator> manager) {
    this.manager = manager;
  }
}
