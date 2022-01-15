package youyihj.herodotusutils.util;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author youyihj
 */
public class Lazy<T, R> implements Supplier<R> {
    private Supplier<T> loader;
    private final Predicate<T> validator;
    private final Function<T, R> processor;
    private boolean loaded;
    private T value;

    protected Lazy(Supplier<T> loader, Predicate<T> validator, Function<T, R> processor) {
        this.loader = loader;
        this.validator = validator;
        this.processor = processor;
    }

    public static <T, R> Lazy<T, R> create(@Nonnull Supplier<T> loader, Predicate<T> validator, Function<T, R> processor) {
        return new Lazy<>(loader, validator, processor);
    }

    public static <T> Lazy<T, T> create(@Nonnull Supplier<T> loader) {
        return new Lazy<>(loader, Objects::nonNull, Function.identity());
    }

    public static <T> Lazy<T, T> createOptional(@Nonnull Supplier<Optional<T>> loader) {
        return create(() -> loader.get().orElse(null));
    }

    public Optional<R> getOptional() {
        if (!loaded) {
            T value = loader.get();
            if (value != null && validator.test(value)) {
                this.value = value;
                this.loaded = true;
                this.loader = null;
                return Optional.of(value).map(processor);
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.ofNullable(value).map(processor);
        }
    }

    @Override
    public R get() {
        return getOptional().orElse(null);
    }
}
