package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    // You can add custom query methods here if needed
    // For example:
    // List<Todo> findByCompleted(boolean completed);
    // List<Todo> findByPriorite(String priorite);
    // List<Todo> findByDueDateBefore(LocalDate date);
}