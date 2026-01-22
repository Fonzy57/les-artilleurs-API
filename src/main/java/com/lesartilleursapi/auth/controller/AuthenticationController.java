package com.lesartilleursapi.auth.controller;

import com.lesartilleursapi.auth.dto.AuthResponseDto;
import com.lesartilleursapi.auth.dto.LoginRequestDto;
import com.lesartilleursapi.auth.security.jwt.JwtService;
import com.lesartilleursapi.auth.security.userdetails.UserPrincipal;
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

  @Autowired
  public AuthenticationController(
      AuthenticationManager authenticationManager,
      JwtService jwtService
  ) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  @PostMapping("/login")
  public AuthResponseDto login(@RequestBody @Valid LoginRequestDto dto) {

    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
    );

    UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
    String token = jwtService.generateAccessToken(principal);

    AuthResponseDto responseDto = new AuthResponseDto();
    responseDto.setAccessToken(token);
    responseDto.setTokenType("Bearer");
    return responseDto;
  }

  //  TODO ICI PEUT ÊTRE FAIRE LES AJOUTS DE NOUVEAU UTILISATEUR OU PAS FORCEMENT
}
