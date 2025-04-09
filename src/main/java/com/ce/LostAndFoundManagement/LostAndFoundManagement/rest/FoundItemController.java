package com.ce.LostAndFoundManagement.LostAndFoundManagement.rest;

import com.ce.LostAndFoundManagement.LostAndFoundManagement.entity.FoundItem;
import com.ce.LostAndFoundManagement.LostAndFoundManagement.service.FoundItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/found-items")
public class FoundItemController {

    private final FoundItemService foundItemService;

    public FoundItemController(FoundItemService foundItemService) {
        this.foundItemService = foundItemService;
    }

    @GetMapping
    public ResponseEntity<List<FoundItem>> getAllFoundItems() {
        return ResponseEntity.ok(foundItemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoundItem> getFoundItemById(@PathVariable Long id) {
        return foundItemService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FoundItem>> getFoundItemsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(foundItemService.findByUserId(userId));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<FoundItem>> getFoundItemsByName(@PathVariable String name) {
        return ResponseEntity.ok(foundItemService.findByName(name));
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<FoundItem>> getFoundItemsByLocation(@PathVariable String location) {
        List<FoundItem> foundItems = foundItemService.findByLocation(location);
        return ResponseEntity.ok(foundItems);
    }

    @PostMapping
    public ResponseEntity<String> createFoundItem(@RequestBody FoundItem foundItem) {
        try {
            FoundItem savedFoundItem = foundItemService.saveFoundItem(foundItem);
            return ResponseEntity.status(201).body("Found item created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Found item already exists");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFoundItem(@PathVariable Long id, @RequestBody FoundItem updatedFoundItem, Authentication authentication) {
        String loggedInEmail = authentication.getName();
        Optional<FoundItem> foundItemOptional;
        foundItemOptional = foundItemService.findById(id);

        if (foundItemOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Found item not found"));
        }

        FoundItem foundItem = foundItemOptional.get();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !foundItem.getUser().getEmail().equals(loggedInEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied"));
        }

        return ResponseEntity.ok(foundItemService.updateFoundItem(id, updatedFoundItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoundItem(@PathVariable Long id) {
        foundItemService.deleteFoundItem(id);
        return ResponseEntity.noContent().build();
    }
}
