package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(length = 50)
    private String category;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;


    // Timestamps
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = updatedAt = new Date(); }
    @PreUpdate
    protected void onUpdate() { updatedAt = new Date(); }

    // Relationship
    @ManyToMany(mappedBy = "permissions")
    @JsonIgnore
    @Builder.Default
    @ToString.Exclude // stops recursion in toString
    @EqualsAndHashCode.Exclude // stops recursion in equals and hashCode
    private Set<Role> roles = new HashSet<>();
}