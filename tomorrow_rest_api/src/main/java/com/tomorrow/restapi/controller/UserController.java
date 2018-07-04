package com.tomorrow.restapi.controller;

import com.tomorrow.restapi.exception.ResourcesNotFoundException;
import com.tomorrow.restapi.model.Task;
import com.tomorrow.restapi.model.User;
import com.tomorrow.restapi.repository.TaskRepository;
import com.tomorrow.restapi.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/users")
    public List<User> getAllusers() {
        return userRepository.findAll();
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/user")
    public User createUser(@Valid @RequestBody User User) {
        List<User> userCheck = userRepository.findByEmailadress(User.getEmailadress());
        if(userCheck.isEmpty()){
            userRepository.save(User);
            return User;
        } else {
            User.setEmailadress( "Email already in use by another account");
            return User;
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable(value = "id") Long UserId) {
        return userRepository.findById(UserId)
                .orElseThrow(() -> new ResourcesNotFoundException("User", "id", UserId));
    }

    @CrossOrigin(origins = "http_//localhost:8080")
    @PutMapping("/user/{id}")
    public User updateUser(@PathVariable(value = "id") Long UserId,
                                           @Valid @RequestBody User UserDetails) {

        User User = userRepository.findById(UserId)
                .orElseThrow(() -> new ResourcesNotFoundException("User", "id", UserId));

        User.setUserName(UserDetails.getUserName());
        User.setEmailadress(UserDetails.getEmailadress());
        User.setPassword(UserDetails.getPassword());

        User updatedUser = userRepository.save(User);
        return updatedUser;
    }

    @Transactional
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long UserId) {
        User User = userRepository.findById(UserId)
                .orElseThrow(() -> new ResourcesNotFoundException("User", "id", UserId));

        taskRepository.deleteByUserId(User.getId());
        userRepository.delete(User);
        
        return ResponseEntity.ok().build();
    }
}