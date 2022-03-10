package net.darmo_creations.build_utils.blocks;

import net.darmo_creations.build_utils.Utils;
import net.darmo_creations.build_utils.gui.GuiLaserTelemeter;
import net.darmo_creations.build_utils.tile_entities.TileEntityLaserTelemeter;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Optional;

/**
 * A block that draws a box of desired size and position to measure distances, areas and volumes.
 *
 * @see TileEntityLaserTelemeter
 */
public class BlockLaserTelemeter extends BaseEntityBlock implements IModBlock {
  public BlockLaserTelemeter() {
    super(BlockBehaviour.Properties
        .of(Material.METAL, MaterialColor.COLOR_RED)
        .strength(-1, 3600000)
        .noDrops());
  }

  @SuppressWarnings("deprecation")
  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    Optional<TileEntityLaserTelemeter> te = Utils.getTileEntity(TileEntityLaserTelemeter.class, world, pos);

    if (te.isPresent() && player.canUseGameMasterBlocks()) {
      if (world.isClientSide()) {
        Minecraft.getInstance().setScreen(new GuiLaserTelemeter(te.get()));
      }
      return InteractionResult.SUCCESS;
    } else {
      return InteractionResult.FAIL;
    }
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileEntityLaserTelemeter(pos, state);
  }

  @Override
  public RenderShape getRenderShape(BlockState p_49232_) {
    return RenderShape.MODEL;
  }
}
