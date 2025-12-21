package org.vitalup.vitalup.entities.Auth;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode
public class Users implements UserDetails {

	// Auth

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(unique = true)
	private String email;

	private String password;

	@Enumerated(EnumType.STRING)
	private UserRole userRole;

	@Column(unique = true)
	private String username;

	private int passwordVersion;

	private String refreshToken;
	private LocalDateTime refreshTokenExpiry;

	private Boolean locked = false;
	private Boolean enabled = false;
	private Boolean isVerifiedRegistration = false;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@Column(name = "google_id", unique = true)
	private String googleId;

	// Security

	@Override
	public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(userRole.name()));
	}

	@Override
	public @NonNull String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	// Time Update

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

}
