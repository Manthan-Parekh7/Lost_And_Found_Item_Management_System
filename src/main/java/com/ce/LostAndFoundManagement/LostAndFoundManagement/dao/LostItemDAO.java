package com.ce.LostAndFoundManagement.LostAndFoundManagement.dao;

import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.LostItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LostItemDAO {
    Optional<LostItem> findById(Long id);
    List<LostItem> findByUserId(Long userId);
    List<LostItem> findAll();
    List<LostItem> findByName(String name);

    List<LostItem> findByLocation(String location);
    Optional<LostItem> findByNameAndLocationAndLostDateAndUserId(String name, String location, LocalDate lostDate, Long userId);
    LostItem save(LostItem lostItem);
    void deleteById(Long id);
}
