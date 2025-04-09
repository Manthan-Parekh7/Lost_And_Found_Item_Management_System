package com.ce.LostAndFoundManagement.LostAndFoundManagement.dao.Impl;

import com.ce.LostAndFoundManagement.LostAndFoundManagement.dao.LostItemDAO;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.LostItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class LostItemDAOImpl implements LostItemDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<LostItem> findById(Long id) {
        return Optional.ofNullable(entityManager.find(LostItem.class, id));
    }

    @Override
    public List<LostItem> findByUserId(Long userId) {
        return entityManager.createQuery("SELECT l FROM LostItem l WHERE l.user.id = :userId", LostItem.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<LostItem> findAll() {
        return entityManager.createQuery("SELECT l FROM LostItem l", LostItem.class).getResultList();
    }

    @Override
    public List<LostItem> findByName(String name) {
        return entityManager.createQuery("SELECT l FROM LostItem l WHERE l.name = :name", LostItem.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public List<LostItem> findByLocation(String location) {
        return entityManager.createQuery("SELECT l FROM LostItem l WHERE l.location = :location", LostItem.class)
                .setParameter("location", location)
                .getResultList();
    }

    @Override
    public Optional<LostItem> findByNameAndLocationAndLostDateAndUserId(String name, String location, LocalDate lostDate, Long userId) {
        List<LostItem> results = entityManager.createQuery(
                        "SELECT l FROM LostItem l WHERE l.name = :name AND l.location = :location AND l.lostDate = :lostDate AND l.user.id = :userId", LostItem.class)
                .setParameter("name", name)
                .setParameter("location", location)
                .setParameter("lostDate", lostDate)
                .setParameter("userId", userId)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public LostItem save(LostItem lostItem) {
        if (lostItem.getId() == null) {
            entityManager.persist(lostItem);
            return lostItem;
        } else {
            return entityManager.merge(lostItem);
        }
    }

    @Override
    public void deleteById(Long id) {
        LostItem lostItem = entityManager.find(LostItem.class, id);
        if (lostItem != null) {
            entityManager.remove(lostItem);
        }
    }
}
