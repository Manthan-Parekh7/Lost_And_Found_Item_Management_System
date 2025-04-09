package com.ce.LostAndFoundManagement.LostAndFoundManagement.service.Impl;

import com.ce.LostAndFoundManagement.LostAndFoundManagement.dao.LostItemDAO;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.LostItem;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.service.LostItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class LostItemServiceImpl implements LostItemService {

    private final LostItemDAO lostItemDAO;

    @Autowired
    public LostItemServiceImpl(LostItemDAO lostItemDAO) {
        this.lostItemDAO = lostItemDAO;
    }

    @Override
    public Optional<LostItem> findById(Long id) {
        return lostItemDAO.findById(id);
    }

    @Override
    public List<LostItem> findByUserId(Long userId) {
        return lostItemDAO.findByUserId(userId);
    }

    @Override
    public List<LostItem> findAll() {
        return lostItemDAO.findAll();
    }

    @Override
    public List<LostItem> findByName(String name) {
        return lostItemDAO.findByName(name);
    }

    @Override
    public List<LostItem> findByLocation(String location) {
        return lostItemDAO.findByLocation(location);
    }

    @Override
    @Transactional
    public LostItem saveLostItem(LostItem lostItem) {
        Optional<LostItem> existingItem = lostItemDAO.findByNameAndLocationAndLostDateAndUserId(
                lostItem.getName(), lostItem.getLocation(), lostItem.getLostDate(), lostItem.getUser().getId()
        );

        if (existingItem.isPresent()) {
            throw new RuntimeException("Lost item already exists.");
        }

        return lostItemDAO.save(lostItem);
    }

    @Override
    @Transactional
    public LostItem updateLostItem(Long id, LostItem updatedLostItem) {
        Optional<LostItem> existingItemOptional = lostItemDAO.findById(id);
        if (existingItemOptional.isPresent()) {
            LostItem existingItem = existingItemOptional.get();

            existingItem.setName(updatedLostItem.getName());
            existingItem.setDescription(updatedLostItem.getDescription());
            existingItem.setLocation(updatedLostItem.getLocation());
            existingItem.setLostDate(updatedLostItem.getLostDate());

            return lostItemDAO.save(existingItem);
        } else {
            throw new RuntimeException("Lost item not found with ID: " + id);
        }
    }

    @Override
    @Transactional
    public void deleteLostItem(Long id) {
        lostItemDAO.deleteById(id);
    }
}
