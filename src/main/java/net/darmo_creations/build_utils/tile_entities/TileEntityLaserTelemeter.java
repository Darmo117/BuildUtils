package net.darmo_creations.build_utils.tile_entities;

import net.darmo_creations.build_utils.BuildUtils;
import net.darmo_creations.build_utils.blocks.BlockLaserTelemeter;
import net.darmo_creations.build_utils.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

/**
 * Block entity for laser telemeter.
 * <p>
 * Negative values for length fields mean that the line should be drawn in the negative direction along its axis.
 *
 * @see BlockLaserTelemeter
 * @see ModBlocks#LASER_TELEMETER
 */
public class TileEntityLaserTelemeter extends BlockEntity {
  private static final String SIZE_TAG_KEY = "Size";
  private static final String OFFSET_TAG_KEY = "Offset";

  private Vec3i size;
  private BlockPos offset;

  /**
   * Create a block entity.
   *
   * @param blockPos   Block’s position.
   * @param blockState Block’s state.
   */
  public TileEntityLaserTelemeter(BlockPos blockPos, BlockState blockState) {
    super(BuildUtils.LASER_TELEMETER_BE_TYPE.get(), blockPos, blockState);
    this.size = new Vec3i(0, 0, 0);
    this.offset = new BlockPos(0, 0, 0);
  }

  /**
   * Return box size.
   */
  public Vec3i getBoxSize() {
    return this.size;
  }

  /**
   * Set box size.
   *
   * @param size New size.
   */
  public void setSize(Vec3i size) {
    this.size = size;
    this.setChanged();
  }

  /**
   * Return box offset relative to this block entity.
   */
  public BlockPos getBoxOffset() {
    return this.offset;
  }

  /**
   * Set box offset relative to this block entity.
   */
  public void setOffset(BlockPos offset) {
    this.offset = offset;
    this.setChanged();
  }

  @Override
  public void load(final CompoundTag compound) {
    super.load(compound);
    this.size = NbtUtils.readBlockPos(compound.getCompound(SIZE_TAG_KEY));
    this.offset = NbtUtils.readBlockPos(compound.getCompound(OFFSET_TAG_KEY));
  }

  @Override
  protected void saveAdditional(CompoundTag compound) {
    super.saveAdditional(compound);
    compound.put(SIZE_TAG_KEY, NbtUtils.writeBlockPos(new BlockPos(this.size)));
    compound.put(OFFSET_TAG_KEY, NbtUtils.writeBlockPos(this.offset));
  }

  @Override
  public AABB getRenderBoundingBox() {
    return INFINITE_EXTENT_AABB;
  }

  @Override
  public boolean onlyOpCanSetNbt() {
    return true;
  }

  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public CompoundTag getUpdateTag() {
    return this.saveWithFullMetadata();
  }
}
