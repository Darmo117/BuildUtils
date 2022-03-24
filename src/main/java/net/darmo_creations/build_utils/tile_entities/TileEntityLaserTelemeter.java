package net.darmo_creations.build_utils.tile_entities;

import net.darmo_creations.build_utils.BuildUtils;
import net.darmo_creations.build_utils.blocks.BlockLaserTelemeter;
import net.darmo_creations.build_utils.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Tile entity for laser telemeter.
 * <p>
 * Negative values for length fields mean that the line should be drawn in the negative direction along its axis.
 *
 * @see BlockLaserTelemeter
 * @see ModBlocks#LASER_TELEMETER
 */
public class TileEntityLaserTelemeter extends TileEntity {
  private static final String SIZE_TAG_KEY = "Size";
  private static final String OFFSET_TAG_KEY = "Offset";

  private Vector3i size;
  private BlockPos offset;

  public TileEntityLaserTelemeter() {
    super(BuildUtils.LASER_TELEMETER_BE_TYPE.get());
    this.size = new Vector3i(0, 0, 0);
    this.offset = new BlockPos(0, 0, 0);
  }

  public Vector3i getSize() {
    return this.size;
  }

  public void setSize(Vector3i size) {
    this.size = size;
    this.setChanged();
  }

  public BlockPos getOffset() {
    return this.offset;
  }

  public void setOffset(BlockPos offset) {
    this.offset = offset;
    this.setChanged();
  }

  @Override
  public CompoundNBT save(CompoundNBT compound) {
    compound.put(SIZE_TAG_KEY, NBTUtil.writeBlockPos(new BlockPos(this.size)));
    compound.put(OFFSET_TAG_KEY, NBTUtil.writeBlockPos(this.offset));
    return super.save(compound);
  }

  @Override
  public void load(BlockState blockState, CompoundNBT compound) {
    super.load(blockState, compound);
    this.size = NBTUtil.readBlockPos(compound.getCompound(SIZE_TAG_KEY));
    this.offset = NBTUtil.readBlockPos(compound.getCompound(OFFSET_TAG_KEY));
  }

  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(this.worldPosition, 0, this.getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    //noinspection ConstantConditions
    this.load(this.getLevel().getBlockState(pkt.getPos()), pkt.getTag());
  }

  @Override
  public CompoundNBT getUpdateTag() {
    return this.save(new CompoundNBT());
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public double getViewDistance() {
    return 1000;
  }
}
