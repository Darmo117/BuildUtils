package net.darmo_creations.build_utils.calculator;

import net.darmo_creations.build_utils.calculator.exceptions.InvalidFunctionArguments;
import net.darmo_creations.build_utils.calculator.exceptions.MaxDepthReachedException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The base class for calculator functions.
 */
public abstract class Function {
  private static final String NAME_KEY = "Name";
  private static final String PARAMETERS_KEY = "Parameters";

  private final String name;
  private final List<String> parameterNames;

  /**
   * Create a function.
   *
   * @param name           Function’s name.
   * @param parameterNames Parameter names.
   */
  public Function(final String name, final List<String> parameterNames) {
    this.name = Objects.requireNonNull(name);
    this.parameterNames = new ArrayList<>(parameterNames);
  }

  /**
   * Create a function from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public Function(CompoundTag tag) {
    this.name = tag.getString(NAME_KEY);
    this.parameterNames = new ArrayList<>();
    tag.getList(PARAMETERS_KEY, Tag.TAG_STRING).forEach(t -> this.parameterNames.add(t.getAsString()));
  }

  /**
   * Return function’s name.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Return function’s parameter names.
   */
  public List<String> getParameterNames() {
    return new ArrayList<>(this.parameterNames);
  }

  /**
   * Evaluates this function in the given scope with the given parameter values.
   * <p>
   * A new scope is stacked before evaluating the function and unstacked afterwards.
   * This scope contains variables corresponding to the function’s arguments.
   *
   * @param scope      Context the function has to use.
   * @param parameters Values for each parameters.
   * @return The result of the function.
   * @throws InvalidFunctionArguments If the number of parameter values does not match
   *                                  the number of parameters of this function.
   * @throws MaxDepthReachedException If the maximum call depth has been reached.
   */
  public double evaluate(final Scope scope, final List<Double> parameters) {
    if (parameters.size() != this.parameterNames.size()) {
      throw new InvalidFunctionArguments(this.name, this.parameterNames.size(), parameters.size());
    }
    if (scope.getStackTrace().size() > Scope.MAX_CALL_DEPTH) {
      throw new MaxDepthReachedException(Scope.MAX_CALL_DEPTH);
    }
    Scope newScope = new Scope(this.name, scope.getGlobalScope(), scope);
    for (int i = 0; i < this.parameterNames.size(); i++) {
      newScope.setVariable(this.parameterNames.get(i), parameters.get(i));
    }
    return this.evaluateImpl(newScope);
  }

  /**
   * Evaluates the function with the given scope.
   *
   * @param scope Function’s scope.
   * @return The result of the function.
   */
  protected abstract double evaluateImpl(final Scope scope);

  /**
   * Serializes this function into an NBT tag.
   *
   * @return The tag.
   */
  public CompoundTag writeToNBT() {
    CompoundTag tag = new CompoundTag();
    tag.putString(NAME_KEY, this.name);
    ListTag paramsList = new ListTag();
    this.parameterNames.forEach(p -> paramsList.add(StringTag.valueOf(p)));
    tag.put(PARAMETERS_KEY, paramsList);
    return tag;
  }
}
