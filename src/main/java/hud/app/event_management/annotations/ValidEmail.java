package hud.app.event_management.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotBlank
@Email(message = "invalid email", regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
@Constraint(validatedBy = {})
public @interface ValidEmail {
    String message() default "Invalid Email";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
