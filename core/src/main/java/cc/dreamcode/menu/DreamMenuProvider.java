package cc.dreamcode.menu;

import lombok.NonNull;

import java.util.function.Consumer;

public interface DreamMenuProvider<D, T, P> {

    D createDefault(@NonNull String title, int rows);

    D createDefault(@NonNull String title, int rows, @NonNull Consumer<D> consumer);

    D createDefault(@NonNull T type, @NonNull String title);

    D createDefault(@NonNull T type, @NonNull String title, @NonNull Consumer<D> consumer);

    P createPaginated(@NonNull D d);

    P createPaginated(@NonNull D d, @NonNull Consumer<D> consumer);

    default P createPaginated(@NonNull String title, int rows) {
        return this.createPaginated(this.createDefault(title, rows));
    }

    default P createPaginated(@NonNull String title, int rows, @NonNull Consumer<P> consumer) {
        final P paginated = this.createPaginated(this.createDefault(title, rows));
        consumer.accept(paginated);
        return paginated;
    }

    default P createPaginated(@NonNull T type, @NonNull String title) {
        return this.createPaginated(this.createDefault(type, title));
    }

    default P createPaginated(@NonNull T type, @NonNull String title, @NonNull Consumer<P> consumer) {
        final P paginated = this.createPaginated(this.createDefault(type, title));
        consumer.accept(paginated);
        return paginated;
    }
}
