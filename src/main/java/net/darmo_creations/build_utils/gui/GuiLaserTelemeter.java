package net.darmo_creations.build_utils.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.darmo_creations.build_utils.BuildUtils;
import net.darmo_creations.build_utils.Utils;
import net.darmo_creations.build_utils.network.PacketLaserTelemeterData;
import net.darmo_creations.build_utils.tile_entities.TileEntityLaserTelemeter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * GUI for the laser telemeter block.
 */
@OnlyIn(Dist.CLIENT)
public class GuiLaserTelemeter extends Screen {
  // Widgets
  private TextFieldWidget lengthXTextField;
  private TextFieldWidget lengthYTextField;
  private TextFieldWidget lengthZTextField;
  private TextFieldWidget xOffsetTextField;
  private TextFieldWidget yOffsetTextField;
  private TextFieldWidget zOffsetTextField;

  // Data
  private final TileEntityLaserTelemeter tileEntity;
  private final Vector3i size;
  private final BlockPos offset;

  // Layout
  public static final int TITLE_MARGIN = 30;
  public static final int MARGIN = 4;
  public static final int BUTTON_WIDTH = 150;
  public static final int BUTTON_HEIGHT = 20;

  /**
   * Creates a GUI for the given tile entity.
   *
   * @param tileEntity The tile entity.
   */
  public GuiLaserTelemeter(TileEntityLaserTelemeter tileEntity) {
    super(new TranslationTextComponent("gui.build_utils.laser_telemeter.title"));
    this.tileEntity = tileEntity;
    this.size = tileEntity.getSize();
    this.offset = tileEntity.getOffset();
  }

  @Override
  protected void init() {
    final int middle = this.width / 2;
    final int leftButtonX = middle - BUTTON_WIDTH - MARGIN;
    final int rightButtonX = middle + MARGIN;

    int y = this.height / 2 - 2 * BUTTON_HEIGHT - MARGIN / 2;
    int btnW = (int) (BUTTON_WIDTH * 0.75);

    //noinspection ConstantConditions
    this.xOffsetTextField = this.addWidget(
        new TextFieldWidget(this.minecraft.font, (int) (middle - btnW * 1.5), y, btnW, BUTTON_HEIGHT, null));
    this.xOffsetTextField.setValue("" + this.offset.getX());
    //noinspection ConstantConditions
    this.yOffsetTextField = this.addWidget(
        new TextFieldWidget(this.minecraft.font, middle - btnW / 2, y, btnW, BUTTON_HEIGHT, null));
    this.yOffsetTextField.setValue("" + this.offset.getY());
    //noinspection ConstantConditions
    this.zOffsetTextField = this.addWidget(
        new TextFieldWidget(this.minecraft.font, middle + btnW / 2, y, btnW, BUTTON_HEIGHT, null));
    this.zOffsetTextField.setValue("" + this.offset.getZ());

    y += BUTTON_HEIGHT * 3 + MARGIN + this.font.lineHeight + 1;

    //noinspection ConstantConditions
    this.lengthXTextField = this.addWidget(
        new TextFieldWidget(this.minecraft.font, (int) (middle - btnW * 1.5), y, btnW, BUTTON_HEIGHT, null));
    this.lengthXTextField.setValue("" + this.size.getX());
    //noinspection ConstantConditions
    this.lengthYTextField = this.addWidget(
        new TextFieldWidget(this.minecraft.font, middle - btnW / 2, y, btnW, BUTTON_HEIGHT, null));
    this.lengthYTextField.setValue("" + this.size.getY());
    //noinspection ConstantConditions
    this.lengthZTextField = this.addWidget(
        new TextFieldWidget(this.minecraft.font, middle + btnW / 2, y, btnW, BUTTON_HEIGHT, null));
    this.lengthZTextField.setValue("" + this.size.getZ());

    y += BUTTON_HEIGHT + 8 * MARGIN;

    this.addButton(new Button(
        leftButtonX, y,
        BUTTON_WIDTH, BUTTON_HEIGHT,
        new TranslationTextComponent("gui.done"),
        b -> this.onDone()
    ));

    this.addButton(new Button(
        rightButtonX, y,
        BUTTON_WIDTH, BUTTON_HEIGHT,
        new TranslationTextComponent("gui.cancel"),
        b -> this.onCancel()
    ));
  }

  @Override
  public void resize(Minecraft minecraft, int width, int height) {
    String lx = this.lengthXTextField.getValue();
    String ly = this.lengthYTextField.getValue();
    String lz = this.lengthZTextField.getValue();
    String x = this.xOffsetTextField.getValue();
    String y = this.yOffsetTextField.getValue();
    String z = this.zOffsetTextField.getValue();
    this.init(minecraft, width, height);
    this.lengthXTextField.setValue(lx);
    this.lengthYTextField.setValue(ly);
    this.lengthZTextField.setValue(lz);
    this.xOffsetTextField.setValue(x);
    this.yOffsetTextField.setValue(y);
    this.zOffsetTextField.setValue(z);
  }

  private static int getValue(final TextFieldWidget TextFieldWidget) {
    try {
      return Integer.parseInt(TextFieldWidget.getValue());
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  /**
   * Update client-side block entity and send packet to server.
   */
  private void onDone() {
    Vector3i size = new Vector3i(getValue(this.lengthXTextField), getValue(this.lengthYTextField), getValue(this.lengthZTextField));
    BlockPos offset = new BlockPos(getValue(this.xOffsetTextField), getValue(this.yOffsetTextField), getValue(this.zOffsetTextField));
    this.tileEntity.setSize(size);
    this.tileEntity.setOffset(offset);
    BuildUtils.NETWORK_CHANNEL.sendToServer(new PacketLaserTelemeterData(this.tileEntity.getBlockPos(), size, offset));
    //noinspection ConstantConditions
    this.minecraft.setScreen(null);
  }

  /**
   * Discard changes and close this GUI.
   */
  private void onCancel() {
    //noinspection ConstantConditions
    this.minecraft.setScreen(null);
  }

  @Override
  public boolean keyPressed(int charCode, int keyCode, int modifiers) {
    if (keyCode == 36 || keyCode == 104) {
      this.onDone();
      return true;
    }
    return super.keyPressed(charCode, keyCode, modifiers);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    this.lengthXTextField.mouseClicked(mouseX, mouseY, mouseButton);
    this.lengthYTextField.mouseClicked(mouseX, mouseY, mouseButton);
    this.lengthZTextField.mouseClicked(mouseX, mouseY, mouseButton);
    this.xOffsetTextField.mouseClicked(mouseX, mouseY, mouseButton);
    this.yOffsetTextField.mouseClicked(mouseX, mouseY, mouseButton);
    this.zOffsetTextField.mouseClicked(mouseX, mouseY, mouseButton);
    return true;
  }

  @Override
  public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
    //noinspection ConstantConditions
    final FontRenderer font = this.minecraft.font;
    final int fontHeight = font.lineHeight;
    final int middle = this.width / 2;

    this.renderBackground(poseStack);

    drawCenteredString(poseStack, font, new TranslationTextComponent("gui.build_utils.laser_telemeter.title"),
        middle, (TITLE_MARGIN - fontHeight) / 2, Utils.WHITE);

    int y = this.height / 2 - 2 * BUTTON_HEIGHT - MARGIN / 2 - fontHeight - 1;

    int btnW = (int) (BUTTON_WIDTH * 0.75);
    drawString(poseStack, font, new TranslationTextComponent("gui.build_utils.laser_telemeter.x_offset_field.label"),
        (int) (middle - btnW * 1.5), y, Utils.GRAY);
    drawString(poseStack, font, new TranslationTextComponent("gui.build_utils.laser_telemeter.y_offset_field.label"),
        middle - btnW / 2, y, Utils.GRAY);
    drawString(poseStack, font, new TranslationTextComponent("gui.build_utils.laser_telemeter.z_offset_field.label"),
        middle + btnW / 2, y, Utils.GRAY);

    y += 3 * BUTTON_HEIGHT + MARGIN + fontHeight;

    drawString(poseStack, font, new TranslationTextComponent("gui.build_utils.laser_telemeter.length_x_field.label"),
        (int) (middle - btnW * 1.5), y, Utils.GRAY);
    drawString(poseStack, font, new TranslationTextComponent("gui.build_utils.laser_telemeter.length_y_field.label"),
        middle - btnW / 2, y, Utils.GRAY);
    drawString(poseStack, font, new TranslationTextComponent("gui.build_utils.laser_telemeter.length_z_field.label"),
        middle + btnW / 2, y, Utils.GRAY);

    this.lengthXTextField.render(poseStack, mouseX, mouseY, partialTicks);
    this.lengthYTextField.render(poseStack, mouseX, mouseY, partialTicks);
    this.lengthZTextField.render(poseStack, mouseX, mouseY, partialTicks);
    this.xOffsetTextField.render(poseStack, mouseX, mouseY, partialTicks);
    this.yOffsetTextField.render(poseStack, mouseX, mouseY, partialTicks);
    this.zOffsetTextField.render(poseStack, mouseX, mouseY, partialTicks);

    super.render(poseStack, mouseX, mouseY, partialTicks);
  }
}
