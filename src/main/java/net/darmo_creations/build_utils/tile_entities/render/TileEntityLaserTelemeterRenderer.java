package net.darmo_creations.build_utils.tile_entities.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.darmo_creations.build_utils.blocks.BlockLaserTelemeter;
import net.darmo_creations.build_utils.blocks.ModBlocks;
import net.darmo_creations.build_utils.tile_entities.TileEntityLaserTelemeter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
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
public class TileEntityLaserTelemeterRenderer extends TileEntityRenderer<TileEntityLaserTelemeter> {
  public TileEntityLaserTelemeterRenderer(TileEntityRendererDispatcher rendererDispatcher) {
    super(rendererDispatcher);
  }

  @Override
  public void render(TileEntityLaserTelemeter te, float partialTick, MatrixStack poseStack, IRenderTypeBuffer bufferSource, int combinedLight, int combinedOverlay) {
    PlayerEntity player = Objects.requireNonNull(Minecraft.getInstance().player);

    if (player.canUseGameMasterBlocks() || player.isSpectator()) {
      Vector3i size = te.getSize();
      BlockPos offset = te.getOffset();
      int boxX1 = offset.getX();
      int boxY1 = offset.getY();
      int boxZ1 = offset.getZ();
      int boxX2 = boxX1 + size.getX();
      int boxY2 = boxY1 + size.getY();
      int boxZ2 = boxZ1 + size.getZ();
      WorldRenderer.renderLineBox(
          poseStack, bufferSource.getBuffer(RenderType.lines()),
          boxX1, boxY1, boxZ1, boxX2, boxY2, boxZ2,
          1F, 1F, 1F, 1F, 0F, 0F, 0F
      );
    }
  }

  @Override
  public boolean shouldRenderOffScreen(TileEntityLaserTelemeter te) {
    return true; // FIXME ignored?
  }
}
