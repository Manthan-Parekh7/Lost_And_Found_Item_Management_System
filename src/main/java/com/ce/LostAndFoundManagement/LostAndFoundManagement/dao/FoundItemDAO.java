package com.ce.LostAndFoundManagement.LostAndFoundManagement.dao;

import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.FoundItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FoundItemDAO {
    Optional<FoundItem> findById(Long id);
    List<FoundItem> findByUserId(Long userId);
    List<FoundItem> findAll();
    List<FoundItem> findByName(String name);

    List<FoundItem> findByLocation(String location);
    FoundItem updateFoundItem(Long id, FoundItem foundItem);
    FoundItem save(FoundItem foundItem);

    void deleteById(Long id);

    Optional<FoundItem> findByNameAndLocationAndFoundDateAndUserId(String name, String location, LocalDate foundDate, Long id);
}
