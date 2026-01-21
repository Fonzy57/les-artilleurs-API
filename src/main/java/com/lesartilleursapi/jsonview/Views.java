package com.lesartilleursapi.jsonview;

// TODO REFACTOR FOR ALL, NEXT STEP DON'T USE JSON VIEW BUT ONLY DTO

public class Views {
  public static class Public {
  }

  public static class Writer extends Public {
  }

  public static class Coach extends Public {
  }

  public static class Admin extends Coach {
  }

  public static class SuperAdmin extends Admin {
  }
}
