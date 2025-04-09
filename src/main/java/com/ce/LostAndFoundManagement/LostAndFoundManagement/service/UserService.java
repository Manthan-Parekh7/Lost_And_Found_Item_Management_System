package com.ce.LostAndFoundManagement.LostAndFoundManagement.service;

import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    User saveUser(User user);
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);
    List<User> findUsersWithMostReports();
}
