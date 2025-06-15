package hud.app.event_management.annotations.loggedUser;

import hud.app.event_management.annotations.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {
    String message() default "Please re-confirm your password";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String passwordField() default "password";
    String confirmPasswordField() default "confirmPassword";
}
