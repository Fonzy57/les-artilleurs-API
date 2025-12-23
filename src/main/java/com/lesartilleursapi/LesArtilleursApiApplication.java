package com.lesartilleursapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LesArtilleursApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(LesArtilleursApiApplication.class, args);

    //TODO SUPPRIMER QUAND TESTS FINIS
    System.out.println("Server started");
  }

}
