package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.DTO.TodoDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Todo;
import com.example.demo.repository.TodoRepository;

@Service
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoDTO> getAllTodos() {
        return todoRepository.findAll(Sort.by(Sort.Direction.DESC, "dueDate")).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TodoDTO getTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));
        return convertToDTO(todo);
    }

    public TodoDTO createTodo(TodoDTO todoDTO) {
        Todo todo = convertToEntity(todoDTO);
        Todo savedTodo = todoRepository.save(todo);
        return convertToDTO(savedTodo);
    }

    public TodoDTO updateTodo(Long id, TodoDTO todoDTO) {
        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));
        
        existingTodo.setTitle(todoDTO.getTitle());
        existingTodo.setCompleted(todoDTO.isCompleted());
        existingTodo.setPriorite(todoDTO.getPriorite());
        existingTodo.setDueDate(todoDTO.getDueDate());
        
        Todo updatedTodo = todoRepository.save(existingTodo);
        return convertToDTO(updatedTodo);
    }

    public void deleteTodo(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Todo not found with id: " + id);
        }
        todoRepository.deleteById(id);
    }

    // Helper method to convert Entity to DTO
    private TodoDTO convertToDTO(Todo todo) {
        return new TodoDTO(
                todo.getId(),
                todo.getTitle(),
                todo.isCompleted(),
                todo.getPriorite(),
                todo.getDueDate()
        );
    }

    // Helper method to convert DTO to Entity
    private Todo convertToEntity(TodoDTO todoDTO) {
        Todo todo = new Todo();
        // Don't set ID for new entities
        if (todoDTO.getId() != null) {
            todo.setId(todoDTO.getId());
        }
        todo.setTitle(todoDTO.getTitle());
        todo.setCompleted(todoDTO.isCompleted());
        todo.setPriorite(todoDTO.getPriorite());
        todo.setDueDate(todoDTO.getDueDate());
        return todo;
    }
}