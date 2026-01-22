package com.lesartilleursapi.auth.security.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PreAuthorize("hasAnyRole('COACH','ADMIN','SUPER_ADMIN')")
public @interface IsCoach {
}
