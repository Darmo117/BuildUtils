package net.darmo_creations.build_utils.gui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.darmo_creations.build_utils.BuildUtils;
import net.darmo_creations.build_utils.Utils;
import net.darmo_creations.build_utils.todo_list.ToDoList;
import net.darmo_creations.build_utils.todo_list.ToDoListItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

/**
 * GUI overlay for todo lists.
 */
@OnlyIn(Dist.CLIENT)
public class ToDoListsOverlay extends GuiComponent {
  private static final int PADDING = 3;

  private final Minecraft minecraft;

  public ToDoListsOverlay(final Minecraft minecraft) {
    this.minecraft = minecraft;
  }

  /**
   * Render this overlay.
   */
  public void render(PoseStack poseStack, final Window window) {
    ToDoList globalList = BuildUtils.TODO_LISTS_MANAGER.getGlobalData();
    //noinspection ConstantConditions
    ToDoList playerList = BuildUtils.TODO_LISTS_MANAGER.getOrCreatePlayerData(this.minecraft.player);
    if (globalList.isVisible()) {
      this.renderList(poseStack, window, globalList, null, true);
    }
    if (playerList.isVisible()) {
      this.renderList(poseStack, window, playerList, this.minecraft.player.getGameProfile().getName(), false);
    }
  }

  /**
   * Render a list on the right or left side of the screen.
   *
   * @param list       List to render.
   * @param playerName Name of the player associated to the list.
   * @param leftSide   True to draw on the left side, false for the right.
   */
  private void renderList(PoseStack poseStack, final Window window, final ToDoList list, final String playerName,
                          final boolean leftSide) {
    String title;
    if (playerName == null) {
      title = I18n.get("gui.build_utils.todo_list.title.global");
    } else {
      title = I18n.get("gui.build_utils.todo_list.title.player", playerName);
    }

    Font font = this.minecraft.font;
    int width = font.width(title);
    int height = font.lineHeight * 2;

    List<String> itemStrings = new ArrayList<>();
    int nbDigits = 1 + (int) Math.floor(Math.log10(list.size()));
    int i = 0;
    for (ToDoListItem item : list) {
      String text = String.format(
          "%s%0" + nbDigits + "d. %s%s",
          item.isChecked() ? ChatFormatting.GREEN : ChatFormatting.RED,
          i + 1,
          ChatFormatting.RESET,
          item.getText()
      );
      itemStrings.add(text);
      height += font.lineHeight;
      width = Math.max(width, font.width(text));
      i++;
    }
    width += 2 * PADDING;

    int x = leftSide ? 0 : (window.getGuiScaledWidth() - width);
    int y = 0;

//    int bg = (int) (this.minecraft.options.textBackgroundOpacity * 255) << 24;
//    fill(poseStack, x, y, x + width, y + height + 2 * PADDING, bg); // FIXME not transparent

    y += PADDING;
    drawCenteredString(poseStack, this.minecraft.font, title, x + width / 2, y, Utils.WHITE);
    y += font.lineHeight;
    drawCenteredString(poseStack, this.minecraft.font, "---", x + width / 2, y, Utils.WHITE);
    y += font.lineHeight;
    for (String line : itemStrings) {
      drawString(poseStack, this.minecraft.font, line, x + PADDING, y, Utils.WHITE);
      y += font.lineHeight;
    }
  }
}
