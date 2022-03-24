package net.darmo_creations.build_utils.todo_list;

import net.minecraft.nbt.CompoundNBT;

import java.util.Objects;

/**
 * An item of a {@link ToDoList}. Items can be checked to mark completion.
 */
public class ToDoListItem implements Cloneable, Comparable<ToDoListItem> {
  public static final String TEXT_KEY = "Text";
  public static final String CHECKED_KEY = "Checked";

  private String text;
  private boolean checked;

  /**
   * Create an unchecked item with the given text.
   *
   * @param text The text.
   */
  public ToDoListItem(String text) {
    this.text = Objects.requireNonNull(text);
    this.checked = false;
  }

  /**
   * Create an item from the given NBT tag.
   *
   * @param tag The tag.
   */
  public ToDoListItem(CompoundNBT tag) {
    this.text = tag.getString(TEXT_KEY);
    this.checked = tag.getBoolean(CHECKED_KEY);
  }

  /**
   * Return this item’s text.
   */
  public String getText() {
    return this.text;
  }

  /**
   * Set the text of this item.
   *
   * @param text The new text.
   */
  public void setText(String text) {
    this.text = Objects.requireNonNull(text);
  }

  /**
   * Return whether this item is checked.
   */
  public boolean isChecked() {
    return this.checked;
  }

  /**
   * Set whether this item is checked.
   *
   * @param checked True to check; false to uncheck.
   */
  public void setChecked(boolean checked) {
    this.checked = checked;
  }

  /**
   * Serialize this object into an NBT tag.
   *
   * @return The serialized data.
   */
  public CompoundNBT writeToNBT() {
    CompoundNBT tag = new CompoundNBT();
    tag.putString(TEXT_KEY, this.text);
    tag.putBoolean(CHECKED_KEY, this.checked);
    return tag;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    ToDoListItem that = (ToDoListItem) o;
    return this.checked == that.checked && this.text.equals(that.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.text, this.checked);
  }

  @Override
  public ToDoListItem clone() {
    try {
      ToDoListItem clone = (ToDoListItem) super.clone();
      clone.text = this.text;
      clone.checked = this.checked;
      return clone;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  @Override
  public int compareTo(ToDoListItem o) {
    return this.getText().compareTo(o.getText());
  }
}
