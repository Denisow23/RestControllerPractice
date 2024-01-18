package ru.denisov.RestControllerPractice.repository.database.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.denisov.RestControllerPractice.model.Order;
import ru.denisov.RestControllerPractice.web.model.OrderFilter;

import java.math.BigDecimal;
import java.time.Instant;

public interface OrderSpecification {

    static Specification<Order> withFilter(OrderFilter orderFilter) {
        return Specification.where(byProductName(orderFilter.getProductName()))
                .and(byCostRange(orderFilter.getMinCost(), orderFilter.getMaxCost()))
                .and(byClientId(orderFilter.getClientId()))
                .and(byCreatedAtBefore(orderFilter.getCreatedBefore()))
                .and(byUpdatedAtBefore(orderFilter.getUpdatedBefore()));
    }

    static Specification<Order> byProductName(String productName) {
        return (root, query, criteriaBuilder) -> {
            if (productName == null) {
                return null;
            }

            return criteriaBuilder.equal(root.get("product"), productName);
        };
    }

    static Specification<Order> byCostRange(BigDecimal minCost, BigDecimal maxCost) {
        return ((root, query, criteriaBuilder) -> {
            if (minCost == null && maxCost == null) {
                return null;
            }

            if (minCost == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("cost"), maxCost);
            }

            if (maxCost == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("cost"), minCost);
            }

            return criteriaBuilder.between(root.get("cost"), minCost, maxCost);
        });
    }

    static Specification<Order> byClientId(Long clientId) {
        return (root, query, criteriaBuilder) -> {
            if (clientId == null) {
                return null;
            }

            return criteriaBuilder.equal(root.get("client").get("id"), clientId);
        };
    }

    static Specification<Order> byCreatedAtBefore(Instant createdBefore) {
        return (root, query, criteriaBuilder) -> {
            if (createdBefore == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get("createAt"), createdBefore);
        };
    }

    static Specification<Order> byUpdatedAtBefore(Instant updatedBefore) {
        return (root, query, criteriaBuilder) -> {
            if (updatedBefore == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get("updateAt"), updatedBefore);
        };
    }
}
