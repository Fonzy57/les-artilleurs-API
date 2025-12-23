package com.lesartilleursapi;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
public class LesArtilleursApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(LesArtilleursApiApplication.class, args);

    //TODO SUPPRIMER QUAND TESTS FINIS
    System.out.println("Server started");
  }

  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

}
