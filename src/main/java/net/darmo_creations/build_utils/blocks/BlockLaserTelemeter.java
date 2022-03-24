package net.darmo_creations.build_utils.blocks;

import net.darmo_creations.build_utils.Utils;
import net.darmo_creations.build_utils.gui.GuiLaserTelemeter;
import net.darmo_creations.build_utils.tile_entities.TileEntityLaserTelemeter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Optional;

/**
 * A block that draws a box of desired size and position to measure distances, areas and volumes.
 *
 * @see TileEntityLaserTelemeter
 */
public class BlockLaserTelemeter extends Block implements IModBlock {
  public BlockLaserTelemeter() {
    super(Block.Properties
        .of(Material.METAL, MaterialColor.COLOR_RED)
        .strength(-1, 3_600_000)
        .noDrops());
  }

  @SuppressWarnings("deprecation")
  @Override
  public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    Optional<TileEntityLaserTelemeter> te = Utils.getTileEntity(TileEntityLaserTelemeter.class, world, pos);

    if (te.isPresent() && player.canUseGameMasterBlocks()) {
      if (world.isClientSide()) {
        Minecraft.getInstance().setScreen(new GuiLaserTelemeter(te.get()));
      }
      return ActionResultType.SUCCESS;
    } else {
      return ActionResultType.FAIL;
    }
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileEntityLaserTelemeter();
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockRenderType getRenderShape(BlockState blockState) {
    return BlockRenderType.MODEL;
  }
}
