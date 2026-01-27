package com.lesartilleursapi.auth.controller;

import com.lesartilleursapi.auth.dto.AuthResponseDto;
import com.lesartilleursapi.auth.dto.LoginRequestDto;
import com.lesartilleursapi.auth.dto.LogoutRequestDto;
import com.lesartilleursapi.auth.dto.RefreshRequestDto;
import com.lesartilleursapi.auth.security.jwt.JwtService;
import com.lesartilleursapi.auth.security.refresh.service.RefreshTokenService;
import com.lesartilleursapi.auth.security.userdetails.UserPrincipal;
import com.lesartilleursapi.auth.user.model.User;
import com.lesartilleursapi.auth.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final RefreshTokenService refreshTokenService;
  private final UserRepository userRepository;

  @Autowired
  public AuthenticationController(
      AuthenticationManager authenticationManager,
      JwtService jwtService,
      RefreshTokenService refreshTokenService,
      UserRepository userRepository
  ) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.refreshTokenService = refreshTokenService;
    this.userRepository = userRepository;
  }

  @PostMapping("/login")
  public AuthResponseDto login(@RequestBody @Valid LoginRequestDto dto, HttpServletRequest request) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
    );

    Object principalObj = auth.getPrincipal();
    if (!(principalObj instanceof UserPrincipal principal)) {
      throw new IllegalStateException("Unexpected principal type: " + principalObj.getClass().getName());
    }

    String accessToken = jwtService.generateAccessToken(principal);

    // on recharge l'entité User (JPA) pour l'associer au refresh token
    User user = userRepository.findOneById(principal.getId())
        .orElseThrow(() -> new IllegalStateException("Authenticated user not found in DB"));

    RefreshTokenService.CreatedRefreshToken createdRefreshToken = refreshTokenService.create(
        user,
        dto.isRememberMe(),
        request
    );

    AuthResponseDto responseDto = new AuthResponseDto();
    responseDto.setAccessToken(accessToken);
    responseDto.setRefreshToken(createdRefreshToken.plainToken());
    responseDto.setTokenType("Bearer");
    return responseDto;
  }

  @PostMapping("/refresh")
  public AuthResponseDto refresh(@RequestBody @Valid RefreshRequestDto dto, HttpServletRequest request) {
    // rotation gérée entièrement en service (transactionnel)
    RefreshTokenService.CreatedRefreshToken rotated = refreshTokenService.rotate(
        dto.getRefreshToken(),
        dto.isRememberMe(),
        request
    );

    User user = userRepository.findOneById(rotated.entity().getUser().getId()).orElseThrow(() -> new IllegalStateException(
        "User not found in DB"));
    UserPrincipal principal = UserPrincipal.from(user);

    String newAccessToken = jwtService.generateAccessToken(principal);

    AuthResponseDto responseDto = new AuthResponseDto();
    responseDto.setAccessToken(newAccessToken);
    responseDto.setRefreshToken(rotated.plainToken());
    responseDto.setTokenType("Bearer");
    return responseDto;
  }

  @PostMapping("/logout")
  public void logout(@RequestBody @Valid LogoutRequestDto dto) {
    refreshTokenService.revoke(dto.getRefreshToken());
  }
}
