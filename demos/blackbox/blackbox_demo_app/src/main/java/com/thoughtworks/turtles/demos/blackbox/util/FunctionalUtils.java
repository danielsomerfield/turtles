package com.thoughtworks.turtles.demos.blackbox.util;

import lombok.Value;

import java.util.Optional;
import java.util.function.Supplier;

public class FunctionalUtils {

    public static <T> OptionalPassThrough<T> either(Optional<T> value) {
        return new OptionalPassThrough<>(value);
    }

    @Value
    public static class OptionalPassThrough<T> {
        private final Optional<T> maybeValue;

        public Optional<T> or(Optional<T> defaultValue) {
            return maybeValue.map(Optional::of).orElse(defaultValue);
        }

        public Optional<T> orElse(Supplier<Optional<T>> defaultValueProvider) {
            return maybeValue.map(Optional::of).orElse(defaultValueProvider.get());
        }
    }
}
