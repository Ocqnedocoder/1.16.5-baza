package ru.levelup.client.api.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleHendler {

    String name();
    String description() default "";
    Module.Category c();

}
