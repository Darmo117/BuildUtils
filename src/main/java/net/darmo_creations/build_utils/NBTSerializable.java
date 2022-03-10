package net.darmo_creations.build_utils;

import net.minecraft.nbt.CompoundTag;

/**
 * Enables serialization and deserialization of objects into and from NBT tags.
 */
public interface NBTSerializable {
  /**
   * Serialize this object into an NBT tag.
   *
   * @return The serialized data.
   */
  CompoundTag writeToNBT();

  /**
   * Update this object using data in the given tag.
   *
   * @param tag The data to deserialize.
   */
  void readFromNBT(CompoundTag tag);
}
