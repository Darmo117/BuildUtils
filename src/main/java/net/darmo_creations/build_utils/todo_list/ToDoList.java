package net.darmo_creations.build_utils.todo_list;

import net.darmo_creations.build_utils.DataManager;
import net.darmo_creations.build_utils.ManagedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * A list of tasks to complete. Tasks are sorted alphabetically.
 */
public class ToDoList implements ManagedData<ToDoList>, Iterable<ToDoListItem> {
  public static final int MAX_SIZE = 100;

  public static final String VISIBLE_KEY = "Visible";
  public static final String AUTO_DELETE_KEY = "AutoDeleteChecked";
  public static final String ITEMS_KEY = "Items";

  private DataManager<ToDoList> manager;
  private boolean visible;
  private boolean autoDeleteChecked;
  private final List<ToDoListItem> items;

  /**
   * Create an empty list.
   */
  public ToDoList() {
    this.visible = false; // Hidden by default
    this.autoDeleteChecked = true;
    this.items = new ArrayList<>();
  }

  /**
   * Return whether this list should appear on screen.
   */
  public boolean isVisible() {
    return this.visible;
  }

  /**
   * Set whether this list should appear on screen.
   *
   * @param visible True to show, false to hide.
   */
  public void setVisible(boolean visible) {
    this.visible = visible;
    this.manager.markDirty();
  }

  /**
   * Return whether this list automatically deletes checked items.
   */
  public boolean isAutoDeleteChecked() {
    return this.autoDeleteChecked;
  }

  /**
   * Set whether this list should automatically delete checked items.
   *
   * @param autoDeleteChecked True to auto-delete checked items, false to keep them.
   */
  public void setAutoDeleteChecked(boolean autoDeleteChecked) {
    this.autoDeleteChecked = autoDeleteChecked;
    this.manager.markDirty();
  }

  /**
   * Return the size of this list.
   */
  public int size() {
    return this.items.size();
  }

  /**
   * Return whether this list is empty.
   */
  @SuppressWarnings("unused")
  public boolean isEmpty() {
    return this.items.isEmpty();
  }

  /**
   * Return a copy of the item at the given index.
   *
   * @param index Index of the item to return.
   * @return The item at the given index.
   * @throws IndexOutOfBoundsException If the index is out of range (<tt>index &lt; 0 || index &gt;= size()</tt>).
   */
  public ToDoListItem get(int index) {
    return this.items.get(index).clone();
  }

  /**
   * Set the text at the given index.
   *
   * @param index Index of the item to set the text of.
   * @param text  The new text, must no be null.
   * @throws IndexOutOfBoundsException If the index is out of range (<tt>index &lt; 0 || index &gt;= size()</tt>).
   */
  public void setText(int index, String text) {
    this.items.get(index).setText(text);
    this.manager.markDirty();
  }

  /**
   * Checks/unchecks the item at the given index.
   *
   * @param index   Index of the item to set the check/uncheck.
   * @param checked True to check; false to uncheck.
   * @return True if the item was checked and then auto-deleted.
   * @throws IndexOutOfBoundsException If the index is out of range (<tt>index &lt; 0 || index &gt;= size()</tt>).
   */
  public boolean setChecked(int index, boolean checked) {
    this.items.get(index).setChecked(checked);
    this.manager.markDirty();
    if (this.autoDeleteChecked && checked) {
      this.items.remove(index);
      return true;
    }
    return false;
  }

  /**
   * Add a copy of the given item to the list. If the list is full, the item is not added.
   *
   * @param item The item to add.
   * @return True if the item was added.
   */
  public boolean add(ToDoListItem item) {
    if (this.size() < MAX_SIZE) {
      this.items.add(item.clone());
      this.manager.markDirty();
      return true;
    }
    return false;
  }

  /**
   * Add a copy of the given item to the list at the specified position. If the list is full, the item is not added.
   *
   * @param index The index to insert the item at.
   * @param item  The item to add.
   * @return True if the item was added.
   * @throws IndexOutOfBoundsException If the index is out of range (<tt>index &lt; 0 || index &gt; size()</tt>).
   */
  public boolean add(int index, ToDoListItem item) {
    if (this.size() < MAX_SIZE) {
      this.items.add(index, item.clone());
      this.manager.markDirty();
      return true;
    }
    return false;
  }

  /**
   * Remove the item at the given index.
   *
   * @param index The index of the item to remove.
   * @return The item that was removed.
   * @throws IndexOutOfBoundsException If the index is out of range (<tt>index &lt; 0 || index &gt;= size()</tt>).
   */
  public ToDoListItem remove(int index) {
    ToDoListItem item = this.items.remove(index);
    this.manager.markDirty();
    return item;
  }

  /**
   * Delete all items from this list.
   */
  public void clear() {
    this.items.clear();
    this.manager.markDirty();
  }

  /**
   * Deletes all checked items.
   *
   * @return The number of items that were deleted.
   */
  public int deleteCheckedItems() {
    int oldSize = this.size();
    boolean anyRemoved = this.items.removeIf(ToDoListItem::isChecked);
    if (anyRemoved) {
      this.manager.markDirty();
    }
    return oldSize - this.size();
  }

  /**
   * Sort this list.
   *
   * @param comparator An optional comparator over items text.
   */
  public void sort(final Comparator<String> comparator) {
    this.items.sort(comparator != null
        ? (i1, i2) -> comparator.compare(i1.getText().toLowerCase(), i2.getText().toLowerCase())
        : null);
    this.manager.markDirty();
  }

  @Override
  public CompoundTag writeToNBT() {
    CompoundTag tag = new CompoundTag();
    tag.putBoolean(VISIBLE_KEY, this.visible);
    tag.putBoolean(AUTO_DELETE_KEY, this.autoDeleteChecked);
    ListTag items = new ListTag();
    this.forEach(item -> items.add(item.writeToNBT()));
    tag.put(ITEMS_KEY, items);
    return tag;
  }

  @Override
  public void readFromNBT(CompoundTag tag) {
    this.items.clear();
    this.visible = tag.getBoolean(VISIBLE_KEY);
    this.autoDeleteChecked = tag.getBoolean(AUTO_DELETE_KEY);
    int i = 0;
    for (Tag item : tag.getList(ITEMS_KEY, new CompoundTag().getId())) {
      if (i == MAX_SIZE) {
        break;
      }
      this.add(new ToDoListItem((CompoundTag) item));
      i++;
    }
  }

  @Override
  public void setManager(DataManager<ToDoList> manager) {
    this.manager = manager;
  }

  @Override
  public Iterator<ToDoListItem> iterator() {
    return new IteratorImpl();
  }

  /**
   * Iterator that clones items before returning them.
   */
  private class IteratorImpl implements Iterator<ToDoListItem> {
    private int i = 0;

    @Override
    public boolean hasNext() {
      return this.i < ToDoList.this.items.size();
    }

    @Override
    public ToDoListItem next() {
      return ToDoList.this.items.get(this.i++).clone();
    }
  }
}
