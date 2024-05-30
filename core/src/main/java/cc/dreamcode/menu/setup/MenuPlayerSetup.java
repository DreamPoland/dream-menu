package cc.dreamcode.menu.setup;

import lombok.NonNull;

public interface MenuPlayerSetup<M, H> {

    M build(@NonNull H h);
}
