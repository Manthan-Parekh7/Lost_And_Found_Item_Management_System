package com.ce.LostAndFoundManagement.LostAndFoundManagement.service;

import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.LostItem;

import java.util.List;
import java.util.Optional;

public interface LostItemService {
    Optional<LostItem> findById(Long id);
    List<LostItem> findByUserId(Long userId);
    List<LostItem> findAll();
    List<LostItem> findByName(String name);
    List<LostItem> findByLocation(String location);
    LostItem saveLostItem(LostItem lostItem);
    LostItem updateLostItem(Long id, LostItem updatedLostItem);
    void deleteLostItem(Long id);
}
