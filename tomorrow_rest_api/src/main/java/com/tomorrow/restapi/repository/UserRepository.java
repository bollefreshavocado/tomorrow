package com.tomorrow.restapi.repository;

import java.util.List;

import com.tomorrow.restapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEmailadress(String emailadress);
}