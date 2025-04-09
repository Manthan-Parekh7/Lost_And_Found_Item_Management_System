package com.ce.LostAndFoundManagement.LostAndFoundManagement.service.Impl;

import com.ce.LostAndFoundManagement.LostAndFoundManagement.dao.ClaimDAO;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.Claim;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.ClaimStatus;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.service.ClaimService;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClaimServiceImpl implements ClaimService {

    private final ClaimDAO claimDAO;

    private final EmailService emailService;

    @Autowired
    public ClaimServiceImpl(ClaimDAO claimDAO , EmailService emailService) {
        this.claimDAO = claimDAO;
        this.emailService = emailService;
    }

    @Override
    public Optional<Claim> findById(Long id) {
        return claimDAO.findById(id);
    }

    @Override
    public List<Claim> findByUserId(Long userId) {
        return claimDAO.findByUserId(userId);
    }

    @Override
    public List<Claim> findByLostItemId(Long lostItemId) {
        return claimDAO.findByLostItemId(lostItemId);
    }

    @Override
    @Transactional
    public boolean existsById(Long id) {
        return claimDAO.existsById(id);
    }

    @Override
    public List<Claim> findByFoundItemId(Long foundItemId) {
        return claimDAO.findByFoundItemId(foundItemId);
    }

    @Override
    public List<Claim> findByStatus(ClaimStatus status) {
        return claimDAO.findByStatus(status);
    }

    @Override
    public List<Claim> findByUserIdAndStatus(Long userId, ClaimStatus status) {
        return claimDAO.findByUserIdAndStatus(userId, status);
    }

    @Override
    public List<Claim> findMatchingClaimsByNameAndLocation() {
        return claimDAO.findMatchingClaimsByNameAndLocation();
    }

    @Override
    public List<Claim> findAll() {
        return claimDAO.findAll();
    }

    @Override
    @Transactional
    public Claim createClaim(Claim claim) {
        claim.setStatus(ClaimStatus.PENDING);
        return claimDAO.save(claim);
    }

    @Override
    @Transactional
    public Claim updateClaimStatus(Long id, ClaimStatus status) {
        Optional<Claim> claimOptional = claimDAO.findById(id);

        if (claimOptional.isEmpty()) {
            throw new RuntimeException("Claim not found with ID: " + id);
        }

        Claim claim = claimOptional.get();

        if (claim.getStatus() != ClaimStatus.PENDING) {
            throw new RuntimeException("Claim has already been reviewed. Further updates are not allowed.");
        }

        claim.setStatus(status);
        Claim updatedClaim = claimDAO.save(claim);

        String userEmail = updatedClaim.getUser().getEmail();
        String userName = updatedClaim.getUser().getName();

        if (status == ClaimStatus.APPROVED) {
            String subject = "Claim Approved - Retrieve Your Item";
            String body = "Dear " + userName + ",<br><br>" +
                    "Your claim for the lost item <b>" + updatedClaim.getLostItem().getName() + "</b> has been approved! " +
                    "You can collect your item from: <b>" + updatedClaim.getFoundItem().getLocation() + "</b>.<br><br>" +
                    "Best Regards,<br>Lost & Found Management Team";

            emailService.sendEmail(userEmail, subject, body);
        } else if (status == ClaimStatus.REJECTED) {
            String subject = "Claim Rejected";
            String body = "Dear " + userName + ",<br><br>" +
                    "We regret to inform you that your claim for the lost item <b>" + updatedClaim.getLostItem().getName() + "</b> has been rejected. " +
                    "For further inquiries, please contact the Lost & Found department.<br><br>" +
                    "Best Regards,<br>Lost & Found Management Team";

            emailService.sendEmail(userEmail, subject, body);
        }

        return updatedClaim;
    }

    @Override
    @Transactional
    public Claim approveClaim(Long id) {
        Optional<Claim> claimOptional = claimDAO.findById(id);

        if (claimOptional.isEmpty()) {
            throw new RuntimeException("Claim not found with ID: " + id);
        }

        Claim claim = claimOptional.get();

        if (claim.getStatus() != ClaimStatus.PENDING) {
            throw new RuntimeException("Claim has already been processed.");
        }

        if (claim.getLostItem() != null && claim.getFoundItem() != null) {
            if (claim.getLostItem().getName().equalsIgnoreCase(claim.getFoundItem().getName()) &&
                    claim.getLostItem().getLocation().equalsIgnoreCase(claim.getFoundItem().getLocation())) {
                claim.setStatus(ClaimStatus.APPROVED);
                return claimDAO.save(claim);
            }
        }

        claim.setStatus(ClaimStatus.REJECTED);
        return claimDAO.save(claim);
    }

    @Override
    @Transactional
    public void deleteClaim(Long id) {
        claimDAO.deleteById(id);
    }
}
