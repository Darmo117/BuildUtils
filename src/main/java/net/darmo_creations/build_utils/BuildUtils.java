package net.darmo_creations.build_utils;

import net.darmo_creations.build_utils.blocks.IModBlock;
import net.darmo_creations.build_utils.blocks.ModBlocks;
import net.darmo_creations.build_utils.commands.ToDoListCommand;
import net.darmo_creations.build_utils.gui.CreativeTab;
import net.darmo_creations.build_utils.gui.ToDoListsOverlay;
import net.darmo_creations.build_utils.items.ModItems;
import net.darmo_creations.build_utils.network.PacketLaserTelemeterData;
import net.darmo_creations.build_utils.tile_entities.TileEntityLaserTelemeter;
import net.darmo_creations.build_utils.tile_entities.render.TileEntityLaserTelemeterRenderer;
import net.darmo_creations.build_utils.todo_list.ToDoListManager;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This mod adds tools, blocks and commands to facilitate building things.
 */
@Mod(BuildUtils.MODID)
public class BuildUtils {
  public static final String MODID = "build_utils";
  public static final Logger LOGGER = LogManager.getLogger();

  private static final String CHANNEL_PROTOCOL_VERSION = "1";
  /**
   * Mod’s network channel.
   */
  public static final SimpleChannel NETWORK_CHANNEL = NetworkRegistry.newSimpleChannel(
      new ResourceLocation(MODID, "main"),
      () -> CHANNEL_PROTOCOL_VERSION,
      CHANNEL_PROTOCOL_VERSION::equals,
      CHANNEL_PROTOCOL_VERSION::equals
  );

  /**
   * Mod’s creative mode tab.
   */
  public static final CreativeTab CREATIVE_MODE_TAB = new CreativeTab();

  /**
   * Regsitry for block entities.
   */
  public static final DeferredRegister<TileEntityType<?>> BLOCK_ENTITIES_REGISTER =
      DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);
  /**
   * Registry entry for laser telemeter block entity.
   */
  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<TileEntityLaserTelemeter>> LASER_TELEMETER_BE_TYPE = BuildUtils.BLOCK_ENTITIES_REGISTER.register(
      "laser_telemeter_block_entity",
      () -> TileEntityType.Builder.of(TileEntityLaserTelemeter::new, ModBlocks.LASER_TELEMETER).build(null)
  );

  /**
   * Manager for player and global todo lists.
   */
  public static ToDoListManager TODO_LISTS_MANAGER;

  @OnlyIn(Dist.CLIENT)
  private final static ToDoListsOverlay TODO_LIST_OVERLAY = new ToDoListsOverlay(Minecraft.getInstance());

  public BuildUtils() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::setup);
    BLOCK_ENTITIES_REGISTER.register(modEventBus);
    MinecraftForge.EVENT_BUS.register(this);
  }

  private void setup(final FMLCommonSetupEvent event) {
    NETWORK_CHANNEL.registerMessage(
        0,
        PacketLaserTelemeterData.class,
        PacketLaserTelemeterData::write,
        PacketLaserTelemeterData::new,
        PacketLaserTelemeterData.Handler::handle
    );
    ClientRegistry.bindTileEntityRenderer(LASER_TELEMETER_BE_TYPE.get(), TileEntityLaserTelemeterRenderer::new);
  }

  /**
   * Forge-related events.
   */
  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
  public static class ForgeEvents {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderPost(final RenderGameOverlayEvent.Post event) {
      TODO_LIST_OVERLAY.render(event.getMatrixStack());
    }

    @SubscribeEvent
    public static void onCommandsRegistry(final RegisterCommandsEvent event) {
      ToDoListCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
      IWorld world = event.getWorld();
      if (world instanceof ServerWorld && world == ((ServerWorld) world).getServer().overworld()) {
        TODO_LISTS_MANAGER = ToDoListManager.attachToGlobalStorage((ServerWorld) world);
      }
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
      IWorld world = event.getWorld();
      if (world instanceof ServerWorld && world == ((ServerWorld) world).getServer().overworld()) {
        TODO_LISTS_MANAGER = null;
      }
    }
  }

  /**
   * Registries-related events.
   */
  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {
    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
      blockRegistryEvent.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
      itemRegistryEvent.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
      // BlockItems
      itemRegistryEvent.getRegistry().registerAll(ModBlocks.BLOCKS.stream()
          .filter(block -> !(block instanceof IModBlock) || ((IModBlock) block).hasGeneratedItemBlock())
          .map(block -> {
            BlockItem itemBlock = new BlockItem(block, new Item.Properties().tab(CREATIVE_MODE_TAB));
            //noinspection ConstantConditions
            itemBlock.setRegistryName(block.getRegistryName());
            ModItems.ITEM_BLOCKS.put(block, itemBlock);
            return itemBlock;
          })
          .toArray(Item[]::new)
      );
    }
  }
}
