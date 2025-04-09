package com.ce.LostAndFoundManagement.LostAndFoundManagement.dao.Impl;

import com.ce.LostAndFoundManagement.LostAndFoundManagement.dao.ClaimDAO;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.Claim;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.ClaimStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClaimDAOImpl implements ClaimDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Claim> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Claim.class, id));
    }

    @Override
    public List<Claim> findByUserId(Long userId) {
        return entityManager.createQuery("SELECT c FROM Claim c WHERE c.user.id = :userId", Claim.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Claim> findByFoundItemId(Long foundItemId) {
        return entityManager.createQuery("SELECT c FROM Claim c WHERE c.foundItem.id = :foundItemId", Claim.class)
                .setParameter("foundItemId", foundItemId)
                .getResultList();
    }

    @Override
    public List<Claim> findByLostItemId(Long lostItemId) {
        return entityManager.createQuery("SELECT c FROM Claim c WHERE c.lostItem.id = :lostItemId", Claim.class)
                .setParameter("lostItemId", lostItemId)
                .getResultList();
    }

    @Override
    public List<Claim> findByStatus(ClaimStatus status) {
        return entityManager.createQuery("SELECT c FROM Claim c WHERE c.status = :status", Claim.class)
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public List<Claim> findByUserIdAndStatus(Long userId, ClaimStatus status) {
        return entityManager.createQuery("SELECT c FROM Claim c WHERE c.user.id = :userId AND c.status = :status", Claim.class)
                .setParameter("userId", userId)
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public List<Claim> findMatchingClaimsByNameAndLocation() {
        return entityManager.createQuery(
                "SELECT c FROM Claim c " +
                        "WHERE (LOWER(c.lostItem.name) LIKE LOWER(CONCAT('%', c.foundItem.name, '%')) " +
                        "OR LOWER(c.foundItem.name) LIKE LOWER(CONCAT('%', c.lostItem.name, '%'))) " +
                        "AND (LOWER(c.lostItem.location) LIKE LOWER(CONCAT('%', c.foundItem.location, '%')) " +
                        "OR LOWER(c.foundItem.location) LIKE LOWER(CONCAT('%', c.lostItem.location, '%')))"+
                        "AND c.status = 'PENDING'",
                Claim.class
        ).getResultList();
    }

    @Override
    public boolean existsById(Long id) {
        String query = "SELECT COUNT(c) FROM Claim c WHERE c.id = :id";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public List<Claim> findAll() {
        return entityManager.createQuery("SELECT c FROM Claim c", Claim.class).getResultList();
    }

    @Override
    public Claim save(Claim claim) {
        if (claim.getId() == null) {
            entityManager.persist(claim);
            return claim;
        } else {
            return entityManager.merge(claim);
        }
    }

    @Override
    public void deleteById(Long id) {
        Claim claim = entityManager.find(Claim.class, id);
        if (claim != null) {
            entityManager.remove(claim);
        }
    }
}
