package ru.denisov.RestControllerPractice.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ObjectUtils;
import ru.denisov.RestControllerPractice.web.model.OrderFilter;

public class OrderFilterValidValidator implements ConstraintValidator<OrderFilterValid, OrderFilter> {
    @Override
    public boolean isValid(OrderFilter value, ConstraintValidatorContext context) {
        if (ObjectUtils.anyNull(value.getPageNumber(), value.getPageSize())) {
            return false;
        }

        if ((value.getMinCost() == null && value.getMaxCost() != null) ||
                (value.getMaxCost() == null && value.getMinCost() != null)) {
            return false;
        }

        return true;
    }
}
