package hud.app.event_management.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotBlank
@Pattern(regexp = "(^(([2]{1}[5]{2})|([0]{1}))[1-9]{2}[0-9]{7}$)")
@Constraint(validatedBy = {})
public @interface ValidPhoneNumber {
    String message() default "Please enter valid phone number eg. 255678900269";
}
