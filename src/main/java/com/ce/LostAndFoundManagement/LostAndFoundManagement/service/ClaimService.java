package com.ce.LostAndFoundManagement.LostAndFoundManagement.service;

import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.Claim;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.ClaimStatus;

import java.util.List;
import java.util.Optional;

public interface ClaimService {
    Optional<Claim> findById(Long id);
    List<Claim> findByUserId(Long userId);
    List<Claim> findByLostItemId(Long lostItemId);
    List<Claim> findByFoundItemId(Long foundItemId);
    List<Claim> findByStatus(ClaimStatus status);
    List<Claim> findByUserIdAndStatus(Long userId, ClaimStatus status);
    List<Claim> findMatchingClaimsByNameAndLocation();
    List<Claim> findAll();
    Claim createClaim(Claim claim);
    Claim updateClaimStatus(Long id, ClaimStatus status);
    Claim approveClaim(Long id);
    void deleteClaim(Long id);

    boolean existsById(Long id);
}