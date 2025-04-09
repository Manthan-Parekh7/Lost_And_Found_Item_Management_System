package com.ce.LostAndFoundManagement.LostAndFoundManagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "found_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FoundItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDate foundDate = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "foundItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Claim> claims;
}
