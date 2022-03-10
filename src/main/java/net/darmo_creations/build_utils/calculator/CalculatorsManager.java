package net.darmo_creations.build_utils.calculator;

import net.darmo_creations.build_utils.DataManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

/**
 * Manager for global and per-player {@link Calculator} instances.
 */
public class CalculatorsManager extends DataManager<Calculator> {
  public static final String DATA_NAME = "calculators";

  /**
   * Load a calculator manager from the given tag.
   *
   * @param tag The tag.
   * @return A new calculator manager.
   */
  public static CalculatorsManager load(final CompoundTag tag) {
    CalculatorsManager m = new CalculatorsManager();
    m.read(tag);
    return m;
  }

  @Override
  protected Calculator getDefaultDataValue() {
    return new Calculator();
  }

  /**
   * Attaches a manager to the global storage through a world instance.
   * If no manager instance is already defined, a new one is created and attached to the storage.
   *
   * @param world The world used to access the global storage.
   * @return The manager instance.
   */
  public static CalculatorsManager attachToGlobalStorage(ServerLevel world) {
    return world.getDataStorage().computeIfAbsent(CalculatorsManager::load, CalculatorsManager::new, DATA_NAME);
  }
}
