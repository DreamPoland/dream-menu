package cc.dreamcode.menu.core.setup;

import lombok.NonNull;

public interface MenuPlayerSetup<M, H> {

    M build(@NonNull H h);
}
