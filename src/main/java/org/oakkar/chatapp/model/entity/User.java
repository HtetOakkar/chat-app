package org.oakkar.chatapp.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.oakkar.chatapp.model.enums.RoleName;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 45, nullable = false)
    private String username;

    @Column(unique = true, nullable = false, length = 60)
    private String email;

    @Column(name = "display_name", length = 60)
    private String displayName;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column
    private Instant createDate;

    @UpdateTimestamp
    @Column
    private Instant updateDate;

    @Version
    @Column
    private long version;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", columnDefinition = "varchar(32) default 'ROLE_USER'")
    private RoleName roleName = RoleName.ROLE_USER;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "user")
    private RefreshToken refreshToken;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "user")
    private List<Post> posts;

}
