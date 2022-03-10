package net.darmo_creations.build_utils;

/**
 * Interface that classes managed by {@link DataManager} must implement.
 *
 * @param <T> Type of managed data.
 */
public interface ManagedData<T extends ManagedData<T>> extends NBTSerializable {
  /**
   * Sets the manager for this object. Objects implementing this interface should call {@link DataManager#markDirty()}
   * whenever their data is modified.
   *
   * @param manager The manager.
   */
  void setManager(DataManager<T> manager);
}
