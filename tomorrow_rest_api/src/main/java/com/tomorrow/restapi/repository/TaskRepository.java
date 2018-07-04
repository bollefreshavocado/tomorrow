package com.tomorrow.restapi.repository;

import java.util.List;

import com.tomorrow.restapi.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
    List<Task> deleteByUserId(Long userId);
}