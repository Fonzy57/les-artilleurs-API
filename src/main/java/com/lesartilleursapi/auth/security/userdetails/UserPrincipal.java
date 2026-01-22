package com.lesartilleursapi.auth.security.userdetails;

import com.lesartilleursapi.auth.user.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class UserPrincipal implements UserDetails {

  private final Long id;
  private final String email;

  @Getter(AccessLevel.NONE)
  private final String passwordHash;
  private final String firstname;
  private final String lastname;
  private final String roleCode;
  private final List<GrantedAuthority> authorities;

  private UserPrincipal(
      Long id,
      String email,
      String passwordHash,
      String firstname,
      String lastname,
      String roleCode
  ) {
    this.id = id;
    this.email = email;
    this.passwordHash = passwordHash;
    this.firstname = firstname;
    this.lastname = lastname;
    this.roleCode = roleCode;
    this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roleCode));
  }

  public static UserPrincipal from(User user) {
    return new UserPrincipal(
        user.getId(),
        user.getEmail(),
        user.getPasswordHash(),
        user.getFirstname(),
        user.getLastname(),
        user.getRole().getCode()
    );
  }

  // --- UserDetails contract ---

  @Override
  @NonNull
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  @NonNull
  public String getUsername() {
    return email;
  }

  @Override
  @NonNull
  public String getPassword() {
    return passwordHash;
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
    return true;
  }
}
