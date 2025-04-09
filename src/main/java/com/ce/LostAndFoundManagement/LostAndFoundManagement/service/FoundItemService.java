package com.ce.LostAndFoundManagement.LostAndFoundManagement.service;

import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.FoundItem;

import java.util.List;
import java.util.Optional;

public interface FoundItemService {
    Optional<FoundItem> findById(Long id);
    List<FoundItem> findByUserId(Long userId);
    List<FoundItem> findAll();
    List<FoundItem> findByName(String name);
    List<FoundItem> findByLocation(String location);
    FoundItem saveFoundItem(FoundItem foundItem);
    FoundItem updateFoundItem(Long id, FoundItem updatedFoundItem);
    void deleteFoundItem(Long id);
}
