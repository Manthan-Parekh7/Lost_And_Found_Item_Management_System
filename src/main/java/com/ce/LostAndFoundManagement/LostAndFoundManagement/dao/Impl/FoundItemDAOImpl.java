package com.ce.LostAndFoundManagement.LostAndFoundManagement.dao.Impl;

import com.ce.LostAndFoundManagement.LostAndFoundManagement.dao.FoundItemDAO;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.FoundItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class FoundItemDAOImpl implements FoundItemDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<FoundItem> findById(Long id) {
        return Optional.ofNullable(entityManager.find(FoundItem.class, id));
    }

    @Override
    public List<FoundItem> findByUserId(Long userId) {
        return entityManager.createQuery("SELECT f FROM FoundItem f WHERE f.user.id = :userId", FoundItem.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<FoundItem> findByLocation(String location) {
        return entityManager.createQuery("SELECT f FROM FoundItem f WHERE f.location = :location", FoundItem.class)
                .setParameter("location", location)
                .getResultList();
    }

    @Override
    public List<FoundItem> findAll() {
        return entityManager.createQuery("SELECT f FROM FoundItem f", FoundItem.class).getResultList();
    }

    @Override
    public List<FoundItem> findByName(String name) {
        return entityManager.createQuery("SELECT f FROM FoundItem f WHERE f.name = :name", FoundItem.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public Optional<FoundItem> findByNameAndLocationAndFoundDateAndUserId(String name, String location, LocalDate foundDate, Long userId) {
        List<FoundItem> results = entityManager.createQuery(
                        "SELECT f FROM FoundItem f WHERE f.name = :name AND f.location = :location AND f.foundDate = :foundDate AND f.user.id = :userId",
                        FoundItem.class)
                .setParameter("name", name)
                .setParameter("location", location)
                .setParameter("foundDate", foundDate)
                .setParameter("userId", userId)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public FoundItem save(FoundItem foundItem) {
        if (foundItem.getId() == null) {
            entityManager.persist(foundItem);
            return foundItem;
        } else {
            return entityManager.merge(foundItem);
        }
    }

    @Override
    public FoundItem updateFoundItem(Long id, FoundItem foundItem) {
        FoundItem existingFoundItem = entityManager.find(FoundItem.class, id);
        if (existingFoundItem != null) {
            existingFoundItem.setName(foundItem.getName());
            existingFoundItem.setLocation(foundItem.getLocation());
            existingFoundItem.setFoundDate(foundItem.getFoundDate());
            existingFoundItem.setUser(foundItem.getUser());
            return entityManager.merge(existingFoundItem);
        } else {
            return null;
        }
    }

    @Override
    public void deleteById(Long id) {
        FoundItem foundItem = entityManager.find(FoundItem.class, id);
        if (foundItem != null) {
            entityManager.remove(foundItem);
        }
    }
}
