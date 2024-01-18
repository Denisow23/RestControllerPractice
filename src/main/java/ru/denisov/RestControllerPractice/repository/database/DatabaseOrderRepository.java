package ru.denisov.RestControllerPractice.repository.database;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.denisov.RestControllerPractice.model.Order;

import java.util.List;

public interface DatabaseOrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Page<Order> findAllByProduct(String product, Pageable pageable);

    @Query("SELECT o FROM ru.denisov.RestControllerPractice.model.Order o WHERE o.product = :productName")
    List<Order> getByProduct(String productName);// тестовый метод

    @Query(value = "SELECT * FROM orders o WHERE o.product = :productName", nativeQuery = true)
    List<Order> getByProductNativeQuery(String productName);// тестовый метод
}
