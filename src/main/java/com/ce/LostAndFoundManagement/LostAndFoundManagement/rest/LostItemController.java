package com.ce.LostAndFoundManagement.LostAndFoundManagement.rest;

import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.LostItem;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.service.LostItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lost-items")
public class LostItemController {

    private final LostItemService lostItemService;

    public LostItemController(LostItemService lostItemService) {
        this.lostItemService = lostItemService;
    }

    @GetMapping
    public ResponseEntity<List<LostItem>> getAllLostItems() {
        return ResponseEntity.ok(lostItemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LostItem> getLostItemById(@PathVariable Long id) {
        return lostItemService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LostItem>> getLostItemsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(lostItemService.findByUserId(userId));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<LostItem>> getLostItemsByName(@PathVariable String name) {
        return ResponseEntity.ok(lostItemService.findByName(name));
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<LostItem>> getLostItemsByLocation(@PathVariable String location) {
        List<LostItem> lostItems = lostItemService.findByLocation(location);
        return ResponseEntity.ok(lostItems);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLostItem(@PathVariable Long id, @RequestBody LostItem updatedLostItem, Authentication authentication) {
        String loggedInEmail = authentication.getName();
        Optional<LostItem> lostItemOptional = lostItemService.findById(id);

        if (lostItemOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Lost item not found"));
        }

        LostItem lostItem = lostItemOptional.get();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !lostItem.getUser().getEmail().equals(loggedInEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied"));
        }

        return ResponseEntity.ok(lostItemService.updateLostItem(id, updatedLostItem));
    }

    @PostMapping
    public ResponseEntity<String> createLostItem(@RequestBody LostItem lostItem) {
        try {
            LostItem savedLostItem = lostItemService.saveLostItem(lostItem);
            return ResponseEntity.status(201).body("Lost item created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Lost item already exists");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLostItem(@PathVariable Long id) {
        lostItemService.deleteLostItem(id);
        return ResponseEntity.noContent().build();
    }
}
