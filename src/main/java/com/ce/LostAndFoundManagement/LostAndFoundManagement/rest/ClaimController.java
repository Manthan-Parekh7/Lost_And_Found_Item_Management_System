package com.ce.LostAndFoundManagement.LostAndFoundManagement.rest;

import com.ce.LostAndFoundManagement.LostAndFoundManagement.dao.LostItemDAO;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.*;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.service.ClaimService;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.service.LostItemService;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;
    private final UserService userService;
    private final StringToClaimStatusConverter stringToClaimStatusConverter;
    private final LostItemService lostItemService;

    public ClaimController(ClaimService claimService, UserService userService, StringToClaimStatusConverter stringToClaimStatusConverter, LostItemService lostItemService) {
        this.claimService = claimService;
        this.userService = userService;
        this.stringToClaimStatusConverter = stringToClaimStatusConverter;
        this.lostItemService = lostItemService;
    }

    @GetMapping
    public ResponseEntity<List<Claim>> getAllClaims() {
        return ResponseEntity.ok(claimService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Claim> getClaimById(@PathVariable Long id) {
        return claimService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Claim>> getClaimsByUserId(@PathVariable Long userId, Authentication authentication) {
        String loggedInEmail = authentication.getName();

        Optional<User> userOptional = userService.findById(userId);

        if (userOptional.isEmpty() || !userOptional.get().getEmail().equals(loggedInEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        return ResponseEntity.ok(claimService.findByUserId(userId));
    }

    @GetMapping("/lost-item/{lostItemId}")
    public ResponseEntity<List<Claim>> getClaimsByLostItemId(@PathVariable Long lostItemId) {
        return ResponseEntity.ok(claimService.findByLostItemId(lostItemId));
    }

    @GetMapping("/found-item/{foundItemId}")
    public ResponseEntity<List<Claim>> getClaimsByFoundItemId(@PathVariable Long foundItemId) {
        return ResponseEntity.ok(claimService.findByFoundItemId(foundItemId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Claim>> getClaimsByStatus(@PathVariable String status) {
        ClaimStatus claimStatus = stringToClaimStatusConverter.convert(status);
        return ResponseEntity.ok(claimService.findByStatus(claimStatus));
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<Claim>> getClaimsByUserIdAndStatus(@PathVariable Long userId, @PathVariable String status) {
        ClaimStatus claimStatus = stringToClaimStatusConverter.convert(status);
        List<Claim> claims = claimService.findByUserIdAndStatus(userId, claimStatus);
        return ResponseEntity.ok(claims);
    }

    @GetMapping("/matching")
    public ResponseEntity<List<Claim>> getMatchingClaimsByNameAndLocation() {
        List<Claim> matchingClaims = claimService.findMatchingClaimsByNameAndLocation();
        return ResponseEntity.ok(matchingClaims);
    }

    @PostMapping
    public ResponseEntity<String> createClaim(@RequestBody Claim claim, Authentication authentication) {
        String loggedInEmail = authentication.getName();

        Optional<User> loggedInUserOptional = userService.findByEmail(loggedInEmail);
        if (loggedInUserOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not found");
        }

        User loggedInUser = loggedInUserOptional.get();

        if (claim.getLostItem() == null || claim.getLostItem().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lost item must be specified");
        }

        Optional<LostItem> lostItemOptional = lostItemService.findById(claim.getLostItem().getId());
        if (lostItemOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lost item not found");
        }

        LostItem lostItem = lostItemOptional.get();

        if (lostItem.getUser() == null || lostItem.getUser().getId() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lost item has no associated user");
        }

        if (!lostItem.getUser().getId().equals(loggedInUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot claim others lost item");
        }

        if (claimService.existsById(claim.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Claim already exists");
        }

        claimService.createClaim(claim);
        return ResponseEntity.status(HttpStatus.CREATED).body("Claim created successfully");
    }

    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<Map<String, Object>> updateClaimStatus(
            @PathVariable Long id, @PathVariable String status) {

        ClaimStatus claimStatus;
        try {
            claimStatus = ClaimStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid status value"));
        }

        Claim updatedClaim = claimService.updateClaimStatus(id, claimStatus);

        if (updatedClaim == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Claim not found"));
        }

        return ResponseEntity.ok(Map.of(
                "message", "Claim status updated successfully",
                "claim", updatedClaim
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        claimService.deleteClaim(id);
        return ResponseEntity.noContent().build();
    }
}
