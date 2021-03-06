package com.thoughtworks.turtles.demo.util;

import java.util.Optional;

import static com.thoughtworks.turtles.demo.util.FunctionalUtils.either;
import static java.util.Optional.ofNullable;

public class Test {
    public void doIt(){
        final Optional<String> maybeHello = ofNullable("hello");
        final Optional<String> maybeMaybeHello =
                either(maybeHello)
                .or(ofNullable("goodbye"));
        System.out.println(maybeMaybeHello);

    }
}
