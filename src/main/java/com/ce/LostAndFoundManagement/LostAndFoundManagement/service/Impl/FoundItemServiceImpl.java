package com.ce.LostAndFoundManagement.LostAndFoundManagement.service.Impl;

import com.ce.LostAndFoundManagement.LostAndFoundManagement.dao.FoundItemDAO;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.FoundItem;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.service.FoundItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FoundItemServiceImpl implements FoundItemService {

    private final FoundItemDAO foundItemDAO;

    @Autowired
    public FoundItemServiceImpl(FoundItemDAO foundItemDAO) {
        this.foundItemDAO = foundItemDAO;
    }

    @Override
    public Optional<FoundItem> findById(Long id) {
        return foundItemDAO.findById(id);
    }

    @Override
    public List<FoundItem> findByUserId(Long userId) {
        return foundItemDAO.findByUserId(userId);
    }

    @Override
    public List<FoundItem> findAll() {
        return foundItemDAO.findAll();
    }

    @Override
    public List<FoundItem> findByName(String name) {
        return foundItemDAO.findByName(name);
    }

    @Override
    public List<FoundItem> findByLocation(String location) {
        return foundItemDAO.findByLocation(location);
    }

    @Override
    @Transactional
    public FoundItem saveFoundItem(FoundItem foundItem) {
        Optional<FoundItem> existingItem = foundItemDAO.findByNameAndLocationAndFoundDateAndUserId(
                foundItem.getName(), foundItem.getLocation(), foundItem.getFoundDate(), foundItem.getUser().getId()
        );

        if (existingItem.isPresent()) {
            throw new RuntimeException("Found item already exists.");
        }

        return foundItemDAO.save(foundItem);
    }

    @Override
    @Transactional
    public FoundItem updateFoundItem(Long id, FoundItem updatedFoundItem) {
        FoundItem foundItem = foundItemDAO.findById(id).orElseThrow(() -> new RuntimeException("Found item not found."));
        return foundItemDAO.updateFoundItem(id, updatedFoundItem);
    }
    @Override
    @Transactional
    public void deleteFoundItem(Long id) {
        foundItemDAO.deleteById(id);
    }
}
