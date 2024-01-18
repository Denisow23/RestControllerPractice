package ru.denisov.RestControllerPractice.repository.database;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.denisov.RestControllerPractice.model.Client;

import java.util.List;

public interface DatabaseClientRepository extends JpaRepository<Client, Long> {
//    @Override
//    @EntityGraph(attributePaths = {"orders"})
//    List<Client> findAll();
}
