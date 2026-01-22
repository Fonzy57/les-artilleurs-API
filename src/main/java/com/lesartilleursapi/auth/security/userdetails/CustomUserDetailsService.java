package com.lesartilleursapi.auth.security.userdetails;

import com.lesartilleursapi.auth.user.model.User;
import com.lesartilleursapi.auth.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<User> userOpt = userRepository.findByEmail(email);
    if (userOpt.isEmpty()) {
      throw new UsernameNotFoundException("User not found with email: " + email);
    }

    return UserPrincipal.from(userOpt.get());
  }
}
