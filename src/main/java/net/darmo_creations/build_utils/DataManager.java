package net.darmo_creations.build_utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A {@link DataManager} manages a global and per-player data objects.
 * These objects are saved on the server alongside world data.
 *
 * @param <T> Type of managed data.
 */
public abstract class DataManager<T extends ManagedData<T>> extends SavedData {
  private static final String GLOBAL_DATA_KEY = "GlobalData";
  private static final String PLAYERS_DATA_KEY = "PlayersData";
  private static final String UUID_KEY = "UUID";
  private static final String PLAYER_DATA_KEY = "PlayerData";

  private T globalData;
  private final Map<UUID, T> playerData;

  /**
   * Create an empty manager.
   */
  public DataManager() {
    this.globalData = this.getDefaultDataValue();
    this.globalData.setManager(this);
    this.playerData = new HashMap<>();
  }

  /**
   * Return a default data object.
   */
  protected abstract T getDefaultDataValue();

  /**
   * Return the global data object.
   */
  public T getGlobalData() {
    return this.globalData;
  }

  /**
   * Alias of {@link #setDirty()} for retro-compatibility.
   */
  public void markDirty() {
    this.setDirty();
  }

  /**
   * Return the data object associated to the given data object.
   * If no data object for the player exists, a new one is created then returned.
   *
   * @param player The player.
   * @return The associated data object.
   */
  public T getOrCreatePlayerData(final Player player) {
    UUID playerUUID = player.getGameProfile().getId();
    if (!this.playerData.containsKey(playerUUID)) {
      T data = this.getDefaultDataValue();
      data.setManager(this);
      this.playerData.put(playerUUID, data);
      this.markDirty();
    }
    return this.playerData.get(playerUUID);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    tag.put(GLOBAL_DATA_KEY, this.globalData.writeToNBT());
    ListTag list = new ListTag();
    for (Map.Entry<UUID, T> item : this.playerData.entrySet()) {
      CompoundTag itemTag = new CompoundTag();
      itemTag.putUUID(UUID_KEY, item.getKey());
      itemTag.put(PLAYER_DATA_KEY, item.getValue().writeToNBT());
      list.add(itemTag);
    }
    tag.put(PLAYERS_DATA_KEY, list);
    return tag;
  }

  /**
   * Update this manager from the given tag.
   *
   * @param tag The tag.
   */
  protected void read(final CompoundTag tag) {
    this.globalData = this.getDefaultDataValue();
    this.globalData.setManager(this);
    this.globalData.readFromNBT(tag.getCompound(GLOBAL_DATA_KEY));
    this.playerData.clear();
    for (Tag item : tag.getList(PLAYERS_DATA_KEY, new CompoundTag().getId())) {
      CompoundTag c = (CompoundTag) item;
      T playerData = this.getDefaultDataValue();
      playerData.setManager(this);
      playerData.readFromNBT(c.getCompound(PLAYER_DATA_KEY));
      this.playerData.put(c.getUUID(UUID_KEY), playerData);
    }
  }
}
