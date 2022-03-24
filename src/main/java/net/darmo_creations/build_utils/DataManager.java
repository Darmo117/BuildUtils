package net.darmo_creations.build_utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A {@link DataManager} manages a global and per-player data objects.
 * These objects are saved on the server alongside world data.
 *
 * @param <T> Type of managed data.
 */
public abstract class DataManager<T extends ManagedData<T>> extends WorldSavedData {
  private static final String GLOBAL_DATA_KEY = "GlobalData";
  private static final String PLAYERS_DATA_KEY = "PlayersData";
  private static final String UUID_KEY = "UUID";
  private static final String PLAYER_DATA_KEY = "PlayerData";

  private T globalData;
  private final Map<UUID, T> playerData;

  /**
   * Create a manager with the given name.
   *
   * @param name Manager’s name.
   */
  public DataManager(final String name) {
    super(name);
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
   * Return the data object associated to the given data object.
   * If no data object for the player exists, a new one is created then returned.
   *
   * @param player The player.
   * @return The associated data object.
   */
  public T getOrCreatePlayerData(final PlayerEntity player) {
    UUID playerUUID = player.getGameProfile().getId();
    if (!this.playerData.containsKey(playerUUID)) {
      T data = this.getDefaultDataValue();
      data.setManager(this);
      this.playerData.put(playerUUID, data);
      this.setDirty();
    }
    return this.playerData.get(playerUUID);
  }

  @Override
  public CompoundNBT save(CompoundNBT tag) {
    tag.put(GLOBAL_DATA_KEY, this.globalData.writeToNBT());
    ListNBT list = new ListNBT();
    for (Map.Entry<UUID, T> item : this.playerData.entrySet()) {
      CompoundNBT itemTag = new CompoundNBT();
      itemTag.putUUID(UUID_KEY, item.getKey());
      itemTag.put(PLAYER_DATA_KEY, item.getValue().writeToNBT());
      list.add(itemTag);
    }
    tag.put(PLAYERS_DATA_KEY, list);
    return tag;
  }

  @Override
  public void load(CompoundNBT tag) {
    this.globalData = this.getDefaultDataValue();
    this.globalData.setManager(this);
    this.globalData.readFromNBT(tag.getCompound(GLOBAL_DATA_KEY));
    this.playerData.clear();
    for (INBT item : tag.getList(PLAYERS_DATA_KEY, new CompoundNBT().getId())) {
      CompoundNBT c = (CompoundNBT) item;
      T playerData = this.getDefaultDataValue();
      playerData.setManager(this);
      playerData.readFromNBT(c.getCompound(PLAYER_DATA_KEY));
      this.playerData.put(c.getUUID(UUID_KEY), playerData);
    }
  }
}
