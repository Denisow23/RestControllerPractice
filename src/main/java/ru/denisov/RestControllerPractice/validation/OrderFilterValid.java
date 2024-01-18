package ru.denisov.RestControllerPractice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OrderFilterValidValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderFilterValid {

    String message() default "Поля пагинации должны быть указаны! " +
            "Если вы указываете minCost или maxCost, то оба поля должны быть указаны!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
