package net.darmo_creations.build_utils.todo_list;

import net.darmo_creations.build_utils.DataManager;
import net.minecraft.world.server.ServerWorld;

/**
 * Manager for global and per-player {@link ToDoList} instances.
 */
public class ToDoListManager extends DataManager<ToDoList> {
  public static final String DATA_NAME = "todo_lists";

  public ToDoListManager() {
    super(DATA_NAME);
  }

  @Override
  protected ToDoList getDefaultDataValue() {
    return new ToDoList();
  }

  /**
   * Attach a manager to the global storage through a world instance.
   * If no manager instance is already defined, a new one is created and attached to the storage.
   *
   * @param world The world used to access the global storage.
   * @return The manager instance.
   */
  public static ToDoListManager attachToGlobalStorage(ServerWorld world) {
    return world.getDataStorage().computeIfAbsent(ToDoListManager::new, DATA_NAME);
  }
}
