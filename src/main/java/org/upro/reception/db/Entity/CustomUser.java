package org.upro.reception.db.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;


@Getter
@Setter
@Entity
@Table(name = "custom_user")
public class CustomUser  implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_user_id_gen")
    @SequenceGenerator(name = "custom_user_id_gen", sequenceName = "custom_user_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 128)
    @NotNull
    @Column(name = "password", nullable = false, length = 128)
    private String password;

    @Column(name = "last_login")
    private Instant lastLogin;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_superuser", nullable = false)
    private Boolean isSuperuser = false;

    @Size(max = 150)
    @NotNull
    @Column(name = "username", nullable = false, length = 150)
    private String username;

    @Size(max = 150)
    @NotNull
    @ColumnDefault("''")
    @Column(name = "first_name", nullable = false, length = 150)
    private String firstName;

    @Size(max = 150)
    @NotNull
    @ColumnDefault("''")
    @Column(name = "last_name", nullable = false, length = 150)
    private String lastName;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_staff", nullable = false)
    private Boolean isStaff = false;

    @NotNull
    @ColumnDefault("true")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "date_joined", nullable = false)
    private Instant dateJoined;


    // 🔑 Many-to-Many with Role via user_role table
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    @Size(max = 20)
    @Column(name = "type", length = 20)
    private String type;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "superviseur_id")
    private CustomUser superviseur;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}