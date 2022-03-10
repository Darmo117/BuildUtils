package net.darmo_creations.build_utils.todo_list;

import net.darmo_creations.build_utils.DataManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

/**
 * Manager for global and per-player {@link ToDoList} instances.
 */
public class ToDoListManager extends DataManager<ToDoList> {
  public static final String DATA_NAME = "todo_lists";

  /**
   * Load a list manager from the given tag.
   *
   * @param tag The tag.
   * @return A new list manager.
   */
  public static ToDoListManager load(final CompoundTag tag) {
    ToDoListManager m = new ToDoListManager();
    m.read(tag);
    return m;
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
  public static ToDoListManager attachToGlobalStorage(ServerLevel world) {
    return world.getDataStorage().computeIfAbsent(ToDoListManager::load, ToDoListManager::new, DATA_NAME);
  }
}
