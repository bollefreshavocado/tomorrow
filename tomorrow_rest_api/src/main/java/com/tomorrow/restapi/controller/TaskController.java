package com.tomorrow.restapi.controller;

import com.tomorrow.restapi.exception.ResourcesNotFoundException;
import com.tomorrow.restapi.model.Task;
import com.tomorrow.restapi.repository.TaskRepository;
import com.tomorrow.restapi.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/user/{userId}/tasks")
    public Task createTask(@PathVariable (value = "userId") Long userId, 
                                            @Valid @RequestBody Task task) {
        return userRepository.findById(userId).map(user -> {
            task.setUser(user);
            return taskRepository.save(task);
        }).orElseThrow(() -> new ResourcesNotFoundException("User", "id", userId));
    }

    //TODO zuerst User-ID finden und dann diese Task durchsuchen (Performance)
    @CrossOrigin(origins = "http://localhost:8080")
    @PutMapping("/user/{userId}/{taskId}")
    public Task editTask(@PathVariable (value = "userId") Long userId, @PathVariable (value = "taskId") Long taskId, @Valid @RequestBody Task taskDetails){
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourcesNotFoundException("Task", "id", taskId));
        
        if(taskDetails.getContent() != null){
            task.setContent(taskDetails.getContent());
        }

        if(taskDetails.getPriority() != null){
            task.setPriority(taskDetails.getPriority());
        }

        if(taskDetails.getDate() != null){
            task.setDate(taskDetails.getDate());
        }
        
        Task updatedTask = taskRepository.save(task);
        return updatedTask;
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/user/{userId}/tasks")
    public List<Task> getTasks(@PathVariable (value = "userId") Long userId) {

        return taskRepository.findByUserId(userId);

    }

    //TODO zuerst User-ID finden und dann diese Task durchsuchen (Performance)
    @CrossOrigin(origins = "http://localhost:8080")
    @DeleteMapping("/user/{userId}/{taskId}")
    public void deleteTask(@PathVariable (value = "userId") Long userId, @PathVariable (value = "taskId") Long taskId){
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourcesNotFoundException("Task", "id", taskId));
        
        taskRepository.delete(task);
    }
}