package org.oakkar.chatapp.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "refresh_token", unique = true)
    private String refreshToken;

    @CreationTimestamp
    @Column(name = "created_date",updatable = false)
    private Instant createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private Instant updatedDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
