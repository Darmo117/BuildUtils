package net.darmo_creations.build_utils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.darmo_creations.build_utils.BuildUtils;
import net.darmo_creations.build_utils.todo_list.ToDoList;
import net.darmo_creations.build_utils.todo_list.ToDoListItem;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.RangeArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.command.EnumArgument;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A command that lets users edit to-do lists to keep track of thing to do.
 * A maximum of 100 items are allowed per list.
 * <p>
 * A new list is created for each player that executes the command.
 */
public class ToDoListCommand {
  private static final SimpleCommandExceptionType MISSING_PLAYER_ERROR = new SimpleCommandExceptionType(
      new TranslatableComponent("commands.todo.error.missing_player")
  );
  private static final SimpleCommandExceptionType FULL_GLOBAL_LIST_ERROR = new SimpleCommandExceptionType(
      new TranslatableComponent("commands.todo.global.error.list_full")
  );
  private static final DynamicCommandExceptionType FULL_PLAYER_LIST_ERROR = new DynamicCommandExceptionType(
      playerName -> new TranslatableComponent("commands.todo.player.error.list_full", playerName)
  );
  private static final DynamicCommandExceptionType OUT_OF_BOUNDS_ERROR = new DynamicCommandExceptionType(
      index -> new TranslatableComponent("commands.todo.error.out_of_bounds", index)
  );

  private static final String TEXT_ARG = "text";
  private static final String INDICES_ARG = "indices";
  private static final String INDEX_ARG = "index";
  private static final String SORT_ARG = "sort_order";
  private static final String OPTION_ARG = "option";
  private static final String OPTION_VALUE_ARG = "option_value";
  private static final String FROM_INDEX_ARG = "from";
  private static final String TO_INDEX_ARG = "to";

  /**
   * Register this command in the given dispatcher.
   */
  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(buildCommand("todo", false));
    LiteralCommandNode<CommandSourceStack> globalCommand = dispatcher.register(buildCommand("todoglobal", true));
    dispatcher.register(Commands.literal("todog")
        .requires(commandSource -> commandSource.hasPermission(2))
        .redirect(globalCommand)
    );
  }

  private static LiteralArgumentBuilder<CommandSourceStack> buildCommand(final String name, final boolean global) {
    return Commands.literal(name)
        .requires(commandSource -> commandSource.hasPermission(global ? 2 : 0))
        .then(Commands.literal("clear")
            .executes(context -> clearList(context, global)))
        .then(Commands.literal("add")
            .then(Commands.argument(TEXT_ARG, StringArgumentType.greedyString())
                .executes(context -> {
                  addItem(context, global, false);
                  return 1;
                })))
        .then(Commands.literal("insert")
            .then(Commands.argument(INDEX_ARG, IntegerArgumentType.integer(1))
                .then(Commands.argument(TEXT_ARG, StringArgumentType.greedyString())
                    .executes(context -> {
                      addItem(context, global, true);
                      return 1;
                    }))))
        .then(Commands.literal("remove")
            .then(Commands.literal("checked")
                .executes(context -> removeItems(context, global, true)))
            .then(Commands.argument(INDICES_ARG, new RangeArgument.Ints())
                .executes(context -> removeItems(context, global, false))))
        .then(Commands.literal("edit")
            .then(Commands.argument(INDEX_ARG, IntegerArgumentType.integer(1))
                .then(Commands.argument(TEXT_ARG, StringArgumentType.greedyString())
                    .executes(context -> {
                      setItemText(context, global);
                      return 1;
                    }))))
        .then(Commands.literal("move")
            .then(Commands.argument(FROM_INDEX_ARG, IntegerArgumentType.integer(1))
                .then(Commands.literal("to")
                    .then(Commands.argument(TO_INDEX_ARG, IntegerArgumentType.integer(1))
                        .executes(context -> {
                          moveItem(context, global);
                          return 1;
                        })))))
        .then(Commands.literal("sort")
            .then(Commands.argument(SORT_ARG, EnumArgument.enumArgument(SortOrder.class))
                .executes(context -> {
                  sortList(context, global);
                  return 1;
                })))
        .then(Commands.literal("check")
            .then(Commands.argument(INDEX_ARG, IntegerArgumentType.integer(1))
                .executes(context -> {
                  checkItem(context, global);
                  return 1;
                })))
        .then(Commands.literal("uncheck")
            .then(Commands.argument(INDEX_ARG, IntegerArgumentType.integer(1))
                .executes(context -> {
                  uncheckItem(context, global);
                  return 1;
                })))
        .then(Commands.literal("option")
            .then(Commands.literal("get")
                .then(Commands.argument(OPTION_ARG, EnumArgument.enumArgument(ListOption.class))
                    .executes(context -> {
                      getListOption(context, global);
                      return 1;
                    })))
            .then(buildOptions(Commands.literal("set"), global)))
        ;
  }

  /**
   * Build the options from {@link ListOption#values()}.
   */
  private static ArgumentBuilder<CommandSourceStack, ?> buildOptions(ArgumentBuilder<CommandSourceStack, ?> root, boolean global) {
    for (ListOption option : ListOption.values()) {
      root = root.then(Commands.literal(option.name())
          .then(Commands.argument(OPTION_VALUE_ARG, option.getArgumentType())
              .executes(context -> {
                setListOption(context, global, option);
                return 1;
              })));
    }
    return root;
  }

  /**
   * Return a todo list and its associated player’s name from the given context.
   *
   * @param context Command’s context.
   * @param global  Whether the method should return the global todo list
   *                or the one for the player that executed the command.
   * @return A pair containing the player’s name and its todo list.
   * Name is null if the global list is queried.
   */
  public static Pair<String, ToDoList> getList(final CommandContext<CommandSourceStack> context, final boolean global) throws CommandSyntaxException {
    String username = null;
    ToDoList list;

    Optional<Entity> entity = Optional.ofNullable(context.getSource().getEntity());
    if (entity.isEmpty() || !(entity.get() instanceof Player)) {
      throw MISSING_PLAYER_ERROR.create();
    }
    if (global) {
      list = BuildUtils.TODO_LISTS_MANAGER.getGlobalData();
    } else {
      Player player = (Player) entity.get();
      username = player.getGameProfile().getName();
      list = BuildUtils.TODO_LISTS_MANAGER.getOrCreatePlayerData(player);
    }

    return new ImmutablePair<>(username, list);
  }

  /**
   * Reset the given list.
   *
   * @param context Context of the command.
   * @param global  Whether to use the global or player todo list.
   * @return The number of deleted items.
   */
  private static int clearList(final CommandContext<CommandSourceStack> context, final boolean global)
      throws CommandSyntaxException {
    Pair<String, ToDoList> data = getList(context, global);
    ToDoList list = data.getRight();
    int size = list.size();
    list.clear();
    TranslatableComponent component;
    if (global) {
      component = new TranslatableComponent("commands.todo.global.feedback.cleared");
    } else {
      component = new TranslatableComponent("commands.todo.player.feedback.cleared", data.getLeft());
    }
    context.getSource().sendSuccess(component, true);
    return size;
  }

  /**
   * Add an item to the given list.
   *
   * @param context Context of the command.
   * @param global  Whether to use the global or player todo list.
   */
  private static void addItem(final CommandContext<CommandSourceStack> context, final boolean global, final boolean insert)
      throws CommandSyntaxException {
    Pair<String, ToDoList> data = getList(context, global);
    String playerName = data.getLeft();
    ToDoList list = data.getRight();
    String text = StringArgumentType.getString(context, TEXT_ARG);
    boolean added;
    if (insert) {
      int index = Math.min(list.size(), IntegerArgumentType.getInteger(context, INDEX_ARG) - 1);
      added = list.add(index, new ToDoListItem(text));
    } else {
      added = list.add(new ToDoListItem(text));
    }
    if (added) {
      if (!list.isVisible()) {
        TranslatableComponent component;
        text = ChatFormatting.ITALIC + text + ChatFormatting.RESET;
        if (global) {
          component = new TranslatableComponent("commands.todo.global.feedback.item_added", text);
        } else {
          component = new TranslatableComponent("commands.todo.player.feedback.item_added", text, playerName);
        }
        context.getSource().sendSuccess(component, true);
      }
    } else {
      if (global) {
        throw FULL_GLOBAL_LIST_ERROR.create();
      }
      throw FULL_PLAYER_LIST_ERROR.create(playerName);
    }
  }

  /**
   * Remove an item from the given list.
   *
   * @param context Context of the command.
   * @param global  Whether to use the global or player todo list.
   * @return The number of items that were removed.
   */
  private static int removeItems(final CommandContext<CommandSourceStack> context, final boolean global, final boolean checked)
      throws CommandSyntaxException {
    Pair<String, ToDoList> data = getList(context, global);
    String playerName = data.getLeft();
    ToDoList list = data.getRight();
    if (checked) {
      int removed = list.deleteCheckedItems();
      if (removed != 0) {
        String key = "commands.todo.%s.feedback.checked_items_removed".formatted(global ? "global" : "player");
        TranslatableComponent component;
        if (global) {
          component = new TranslatableComponent(key, removed);
        } else {
          component = new TranslatableComponent(key, removed, playerName);
        }
        context.getSource().sendSuccess(component, true);
      } else {
        String key = "commands.todo.%s.feedback.no_checked_items_removed".formatted(global ? "global" : "player");
        TranslatableComponent component;
        if (global) {
          component = new TranslatableComponent(key);
        } else {
          component = new TranslatableComponent(key, playerName);
        }
        context.getSource().sendSuccess(component, true);
      }
      return removed;

    } else {
      MinMaxBounds.Ints indices = RangeArgument.Ints.getRange(context, INDICES_ARG);
      int min = Optional.ofNullable(indices.getMin()).map(i -> i - 1).orElse(0);
      int max = Optional.ofNullable(indices.getMax()).map(i -> i - 1).orElse(list.size() - 1);
      if (min >= list.size() || min < 0) {
        throw OUT_OF_BOUNDS_ERROR.create(min + 1);
      }
      if (max >= list.size() || max < 0) {
        throw OUT_OF_BOUNDS_ERROR.create(max + 1);
      }
      for (int i = min; i <= max; i++) {
        list.remove(min);
      }
      int removedNb = max - min + 1;
      String key = "commands.todo.%s.feedback.items_removed".formatted(global ? "global" : "player");
      TranslatableComponent component;
      if (global) {
        component = new TranslatableComponent(key, removedNb);
      } else {
        component = new TranslatableComponent(key, removedNb, playerName);
      }
      context.getSource().sendSuccess(component, true);
      return removedNb;
    }
  }

  /**
   * Set the text of the given item.
   *
   * @param context Context of the command.
   * @param global  Whether to use the global or player todo list.
   */
  private static void setItemText(final CommandContext<CommandSourceStack> context, final boolean global) throws CommandSyntaxException {
    Pair<String, ToDoList> data = getList(context, global);
    String playerName = data.getLeft();
    ToDoList list = data.getRight();
    int index = IntegerArgumentType.getInteger(context, INDEX_ARG) - 1;
    if (index >= list.size()) {
      throw OUT_OF_BOUNDS_ERROR.create(index + 1);
    }
    String text = StringArgumentType.getString(context, TEXT_ARG);
    list.setText(index, text);
    if (!list.isVisible()) {
      TranslatableComponent component;
      text = ChatFormatting.ITALIC + text + ChatFormatting.RESET;
      if (global) {
        component = new TranslatableComponent("commands.todo.global.feedback.item_text_changed", index + 1, text);
      } else {
        component = new TranslatableComponent("commands.todo.player.feedback.item_text_changed", index + 1, text, playerName);
      }
      context.getSource().sendSuccess(component, true);
    }
  }

  /**
   * Check an item from the given list.
   *
   * @param context Context of the command.
   * @param global  Whether to use the global or player todo list.
   */
  private static void checkItem(final CommandContext<CommandSourceStack> context, final boolean global)
      throws CommandSyntaxException {
    Pair<String, ToDoList> data = getList(context, global);
    ToDoList list = data.getRight();
    int index = IntegerArgumentType.getInteger(context, INDEX_ARG) - 1;
    boolean deleted;
    try {
      deleted = list.setChecked(index, true);
    } catch (IndexOutOfBoundsException e) {
      throw OUT_OF_BOUNDS_ERROR.create(index + 1);
    }
    if (!list.isVisible()) {
      String key = "commands.todo.%s.feedback.item_checked".formatted(global ? "global" : "player");
      if (deleted) {
        key += ".item_checked_deleted";
      }
      TranslatableComponent component;
      if (global) {
        component = new TranslatableComponent(key, index + 1);
      } else {
        component = new TranslatableComponent(key, index + 1, data.getLeft());
      }
      context.getSource().sendSuccess(component, true);
    }
  }

  /**
   * Uncheck an item from the given list.
   *
   * @param context Context of the command.
   * @param global  Whether to use the global or player todo list.
   */
  private static void uncheckItem(final CommandContext<CommandSourceStack> context, final boolean global)
      throws CommandSyntaxException {
    Pair<String, ToDoList> data = getList(context, global);
    ToDoList list = data.getRight();
    int index = IntegerArgumentType.getInteger(context, INDEX_ARG) - 1;
    try {
      list.setChecked(index, false);
    } catch (IndexOutOfBoundsException e) {
      throw OUT_OF_BOUNDS_ERROR.create(index + 1);
    }
    if (!list.isVisible()) {
      String key = "commands.todo.%s.feedback.item_unchecked".formatted(global ? "global" : "player");
      TranslatableComponent component;
      if (global) {
        component = new TranslatableComponent(key, index + 1);
      } else {
        component = new TranslatableComponent(key, index + 1, data.getLeft());
      }
      context.getSource().sendSuccess(component, true);
    }
  }

  /**
   * Move an item in the given list.
   *
   * @param context Context of the command.
   * @param global  Whether to use the global or player todo list.
   */
  private static void moveItem(final CommandContext<CommandSourceStack> context, final boolean global)
      throws CommandSyntaxException {
    Pair<String, ToDoList> data = getList(context, global);
    ToDoList list = data.getRight();
    int from = IntegerArgumentType.getInteger(context, FROM_INDEX_ARG) - 1;
    int to = Math.min(list.size(), IntegerArgumentType.getInteger(context, TO_INDEX_ARG)) - 1;
    list.add(to, list.remove(from));
  }

  /**
   * Sort the given list.
   *
   * @param context Context of the command.
   * @param global  Whether to use the global or player todo list.
   */
  private static void sortList(final CommandContext<CommandSourceStack> context, final boolean global)
      throws CommandSyntaxException {
    Pair<String, ToDoList> data = getList(context, global);
    ToDoList list = data.getRight();
    SortOrder order = context.getArgument(SORT_ARG, SortOrder.class);
    list.sort(order.comparator);
    if (!list.isVisible()) {
      String key = "commands.todo.%s.feedback.list_sorted".formatted(global ? "global" : "player");
      TranslatableComponent component;
      if (global) {
        component = new TranslatableComponent(key);
      } else {
        component = new TranslatableComponent(key, data.getLeft());
      }
      context.getSource().sendSuccess(component, true);
    }
  }

  /**
   * Get the value of an option of the given list.
   *
   * @param context Context of the command.
   * @param global  Whether to use the global or player todo list.
   */
  private static void getListOption(final CommandContext<CommandSourceStack> context, final boolean global)
      throws CommandSyntaxException {
    Pair<String, ToDoList> data = getList(context, global);
    ToDoList list = data.getRight();
    ListOption option = context.getArgument(OPTION_ARG, ListOption.class);
    String value = option.getValueRepresentation(list);
    context.getSource().sendSuccess(new TextComponent(value), true);
  }

  /**
   * Set an option of the given list.
   *
   * @param context Context of the command.
   * @param global  Whether to use the global or player todo list.
   * @param option  The option to set the value of.
   */
  private static void setListOption(final CommandContext<CommandSourceStack> context, final boolean global, final ListOption option)
      throws CommandSyntaxException {
    Pair<String, ToDoList> data = getList(context, global);
    ToDoList list = data.getRight();
    Object value = option.getArgument(context, OPTION_VALUE_ARG);
    option.setValue(list, value);
    context.getSource().sendSuccess(
        new TranslatableComponent("commands.todo.feedback.option_set", option, value), true);
  }

  /**
   * Possible sort values.
   */
  @SuppressWarnings("unused")
  private enum SortOrder {
    alphabetical((a, b) -> a.compareTo(b)),
    reverse((a, b) -> -a.compareTo(b));

    private final Comparator<String> comparator;

    SortOrder(final Comparator<String> comparator) {
      this.comparator = comparator;
    }
  }

  /**
   * Enumeration of available list options.
   */
  private enum ListOption {
    autoDeleteChecked(
        toDoListItems -> "" + toDoListItems.isAutoDeleteChecked(),
        (list, value) -> list.setAutoDeleteChecked((Boolean) value),
        BoolArgumentType::bool,
        BoolArgumentType::getBool
    ),
    visible(
        toDoListItems -> "" + toDoListItems.isVisible(),
        (list, value) -> list.setVisible((Boolean) value),
        BoolArgumentType::bool,
        BoolArgumentType::getBool
    );

    private final Function<ToDoList, String> getter;
    private final BiConsumer<ToDoList, Object> setter;
    private final Supplier<ArgumentType<?>> argumentTypeSupplier;
    private final BiFunction<CommandContext<?>, String, ?> argGetter;

    ListOption(final Function<ToDoList, String> getter,
               final BiConsumer<ToDoList, Object> setter,
               final Supplier<ArgumentType<?>> argumentTypeSupplier,
               final BiFunction<CommandContext<?>, String, ?> argGetter) {
      this.getter = getter;
      this.setter = setter;
      this.argumentTypeSupplier = argumentTypeSupplier;
      this.argGetter = argGetter;
    }

    /**
     * Return the argument type for this option.
     */
    public ArgumentType<?> getArgumentType() {
      return this.argumentTypeSupplier.get();
    }

    /**
     * Return the value of the given argument.
     *
     * @param context Command’s context.
     * @param name    Argument’s name.
     * @return Argument’s value.
     */
    public Object getArgument(final CommandContext<?> context, final String name) {
      return this.argGetter.apply(context, name);
    }

    /**
     * Return the string representation of this option’s value for the given list.
     *
     * @param list The list to get the option value from.
     */
    public String getValueRepresentation(final ToDoList list) {
      return this.getter.apply(list);
    }

    /**
     * Set the value of an option for the given list.
     *
     * @param list  The list to set the option of.
     * @param value New value.
     */
    public void setValue(ToDoList list, final Object value) {
      this.setter.accept(list, value);
    }
  }
}
