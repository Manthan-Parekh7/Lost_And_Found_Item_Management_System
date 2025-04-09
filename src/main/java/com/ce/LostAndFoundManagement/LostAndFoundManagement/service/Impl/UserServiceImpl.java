package com.ce.LostAndFoundManagement.LostAndFoundManagement.service.Impl;

import com.ce.LostAndFoundManagement.LostAndFoundManagement.dao.UserDAO;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.User;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userDAO.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public List<User> findUsersWithMostReports() {
        return userDAO.findUsersWithMostReports();
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        // Check if email already exists
        if (userDAO.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use.");
        }

        // Save the new user
        return userDAO.save(user);
    }


    @Override
    @Transactional
    public User updateUser(Long id, User updatedUser) {
        Optional<User> existingUserOptional = userDAO.findById(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            // Update user details
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setRole(updatedUser.getRole());
            // Update password only if it is provided
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
                existingUser.setPassword(updatedUser.getPassword()); // DO NOT encrypt here
            }

            return userDAO.save(existingUser);
        } else {
            throw new RuntimeException("User not found with ID: " + id);
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userDAO.deleteById(id);
    }
}
