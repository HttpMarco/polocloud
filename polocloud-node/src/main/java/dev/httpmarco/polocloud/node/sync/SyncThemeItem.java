package dev.httpmarco.polocloud.node.sync;

import dev.httpmarco.polocloud.node.Node;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SyncThemeItem<T> {

    private final SyncCategory syncCategory;

    public SyncThemeItem(String id) {
        this.syncCategory = new SyncCategory(id);
    }

    public void delete(SyncId id) {
        Node.instance().syncProvider().delete(id);
    }

    public void push(String id, T object) {
        Node.instance().syncProvider().push(newId(id), object);
    }

    @SuppressWarnings("unchecked")
    public List<T> grab() {
        return (List<T>) Node.instance().syncProvider().grab(syncCategory);
    }

    @SuppressWarnings("unchecked")
    public @Nullable T grabOne(String syncId) {
        return (T) Node.instance().syncProvider().grabOne(newId(syncId));
    }

    @Contract("_ -> new")
    private @NotNull SyncId newId(String id) {
        return new SyncId(syncCategory, id);
    }
}
