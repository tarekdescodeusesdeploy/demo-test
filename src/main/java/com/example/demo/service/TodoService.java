package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.DTO.TodoDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.DCUser;
import com.example.demo.model.Todo;
import com.example.demo.repository.TodoRepository;
import com.example.demo.repository.UserRepository;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoDTO> getAllTodos() {
        List<Todo> todos = todoRepository.findAll(Sort.by(Sort.Direction.DESC, "dueDate"));
        List<TodoDTO> todoDTOs = new ArrayList<>();
        for (Todo todo : todos) {
            todoDTOs.add(convertToDTO(todo));
        }
        return todoDTOs;
        //return todoRepository.findAll(Sort.by(Sort.Direction.DESC, "dueDate")).stream()
        //        .map(this::convertToDTO)
        //        .collect(Collectors.toList());
    }

    public TodoDTO getTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));
        return convertToDTO(todo);
    }

    public TodoDTO createTodo(TodoDTO todoDTO) {
        Todo todo = convertToEntity(todoDTO);

        UserDetails userDetails = 
            (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        
        DCUser user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        todo.setUtilisateur(user);

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