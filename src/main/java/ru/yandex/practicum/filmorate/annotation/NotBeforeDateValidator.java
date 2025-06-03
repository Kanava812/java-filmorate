package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NotBeforeDateValidator implements ConstraintValidator<NotBeforeDate, LocalDate> {

    private LocalDate minDate;

    @Override
    public void initialize(NotBeforeDate constraintAnnotation) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        minDate = LocalDate.parse(constraintAnnotation.minValue(), formatter);
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        return date != null && !date.isBefore(minDate);
    }
}