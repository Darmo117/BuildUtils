package net.darmo_creations.build_utils.network;

import net.darmo_creations.build_utils.BuildUtils;
import net.darmo_creations.build_utils.Utils;
import net.darmo_creations.build_utils.tile_entities.TileEntityLaserTelemeter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Data packet used to send new laser telemeter settings from client to server.
 */
public class PacketLaserTelemeterData implements IPacket<PacketLaserTelemeterData.Handler> {
  private BlockPos tileEntityPos;
  private Vector3i boxSize;
  private BlockPos boxOffset;

  /**
   * Create a packet.
   *
   * @param tileEntityPos Position of the tile entity to update.
   * @param boxSize       Box size.
   * @param boxOffset     Box offset.
   */
  public PacketLaserTelemeterData(final BlockPos tileEntityPos, final Vector3i boxSize, final BlockPos boxOffset) {
    this.tileEntityPos = tileEntityPos;
    this.boxSize = boxSize;
    this.boxOffset = boxOffset;
  }

  /**
   * Create a packet from a byte buffer.
   *
   * @param buf The buffer.
   */
  public PacketLaserTelemeterData(final PacketBuffer buf) {
    this.read(buf);
  }

  @Override
  public void write(PacketBuffer buf) {
    buf.writeBlockPos(this.tileEntityPos);
    buf.writeBlockPos(new BlockPos(this.boxSize));
    buf.writeBlockPos(this.boxOffset);
  }

  @Override
  public void read(PacketBuffer buf) {
    this.tileEntityPos = buf.readBlockPos();
    this.boxSize = buf.readBlockPos();
    this.boxOffset = buf.readBlockPos();
  }

  @Override
  public void handle(PacketLaserTelemeterData.Handler listener) {
  }

  /**
   * Server-side handler for {@link PacketLaserTelemeterData} message type.
   */
  public static class Handler implements INetHandler {
    /**
     * Handle packet coming from client.
     *
     * @param packet The packet.
     * @param ctx    Packet’s context.
     */
    public static void handle(PacketLaserTelemeterData packet, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
        PlayerEntity sender = context.getSender();
        if (sender != null) {
          Utils.getTileEntity(TileEntityLaserTelemeter.class, sender.level, packet.tileEntityPos)
              .ifPresent(te -> {
                try {
                  te.setSize(packet.boxSize);
                  te.setOffset(packet.boxOffset);
                } catch (IllegalArgumentException e) {
                  BuildUtils.LOGGER.catching(e);
                }
              });
        }
      });
      context.setPacketHandled(true);
    }

    @Override
    public void onDisconnect(ITextComponent component) {
    }

    @Override
    public NetworkManager getConnection() {
      return null;
    }
  }
}
