package com.ce.LostAndFoundManagement.LostAndFoundManagement.dao;

import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.User;

import java.util.Optional;
import java.util.List;

public interface UserDAO {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    User save(User user);
    void deleteById(Long id);
    List<User> findUsersWithMostReports();
}
