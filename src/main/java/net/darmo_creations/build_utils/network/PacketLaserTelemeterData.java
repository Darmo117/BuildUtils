package net.darmo_creations.build_utils.network;

import net.darmo_creations.build_utils.BuildUtils;
import net.darmo_creations.build_utils.Utils;
import net.darmo_creations.build_utils.tile_entities.TileEntityLaserTelemeter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Data packet used to send new laser telemeter settings from client to server.
 */
public class PacketLaserTelemeterData implements Packet<PacketLaserTelemeterData.Handler> {
  private final BlockPos tileEntityPos;
  private final Vec3i boxSize;
  private final BlockPos boxOffset;

  /**
   * Create a packet.
   *
   * @param tileEntityPos Position of the tile entity to update.
   * @param boxSize       Box size.
   * @param boxOffset     Box offset.
   */
  public PacketLaserTelemeterData(final BlockPos tileEntityPos, final Vec3i boxSize, final BlockPos boxOffset) {
    this.tileEntityPos = tileEntityPos;
    this.boxSize = boxSize;
    this.boxOffset = boxOffset;
  }

  /**
   * Create a packet from a byte buffer.
   *
   * @param buf The buffer.
   */
  public PacketLaserTelemeterData(final FriendlyByteBuf buf) {
    this.tileEntityPos = buf.readBlockPos();
    this.boxSize = buf.readBlockPos();
    this.boxOffset = buf.readBlockPos();
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(this.tileEntityPos);
    buf.writeBlockPos(new BlockPos(this.boxSize));
    buf.writeBlockPos(this.boxOffset);
  }

  @Override
  public void handle(PacketLaserTelemeterData.Handler listener) {
  }

  /**
   * Server-side handler for {@link PacketLaserTelemeterData} message type.
   */
  public static class Handler implements PacketListener {
    /**
     * Handle packet coming from client.
     *
     * @param packet The packet.
     * @param ctx    Packet’s context.
     */
    public static void handle(PacketLaserTelemeterData packet, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
        ServerPlayer sender = context.getSender();
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
    public void onDisconnect(Component c) {
    }

    @Override
    public Connection getConnection() {
      return null;
    }
  }
}
