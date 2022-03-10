package net.darmo_creations.build_utils.tile_entities.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.darmo_creations.build_utils.blocks.BlockLaserTelemeter;
import net.darmo_creations.build_utils.blocks.ModBlocks;
import net.darmo_creations.build_utils.tile_entities.TileEntityLaserTelemeter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;

/**
 * Renderer for the tile entity associated to laser telemeters.
 * <p>
 * Renders the axes/box.
 *
 * @see TileEntityLaserTelemeterRenderer
 * @see BlockLaserTelemeter
 * @see ModBlocks#LASER_TELEMETER
 */
@OnlyIn(Dist.CLIENT)
public class TileEntityLaserTelemeterRenderer implements BlockEntityRenderer<TileEntityLaserTelemeter> {
  @Override
  public void render(TileEntityLaserTelemeter te, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
    Player player = Objects.requireNonNull(Minecraft.getInstance().player);

    if (player.canUseGameMasterBlocks() || player.isSpectator()) {
      Vec3i size = te.getBoxSize();
      BlockPos offset = te.getBoxOffset();
      int boxX1 = offset.getX();
      int boxY1 = offset.getY();
      int boxZ1 = offset.getZ();
      int boxX2 = boxX1 + size.getX();
      int boxY2 = boxY1 + size.getY();
      int boxZ2 = boxZ1 + size.getZ();
      LevelRenderer.renderLineBox(
          poseStack, bufferSource.getBuffer(RenderType.lines()),
          boxX1, boxY1, boxZ1, boxX2, boxY2, boxZ2,
          1F, 1F, 1F, 1F, 0F, 0F, 0F
      );
    }
  }

  @Override
  public boolean shouldRenderOffScreen(TileEntityLaserTelemeter te) {
    return true;
  }

  @Override
  public int getViewDistance() {
    return 1000;
  }
}
