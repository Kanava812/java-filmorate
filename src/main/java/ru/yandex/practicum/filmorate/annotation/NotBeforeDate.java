package ru.yandex.practicum.filmorate.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBeforeDateValidator.class)
public @interface NotBeforeDate {
    String message() default "{ru.yandex.practicum.filmorate.MinDate.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String minValue() default "1895-12-28";
}