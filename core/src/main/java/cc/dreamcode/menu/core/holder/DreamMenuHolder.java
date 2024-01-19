package cc.dreamcode.menu.core.holder;

import lombok.NonNull;

public interface DreamMenuHolder<P> {

    void open(@NonNull P customer);

    void dispose();
}
