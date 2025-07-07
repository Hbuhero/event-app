package hud.app.event_management.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class PasswordValidator implements ConstraintValidator<ValidPassword, Object> {
    private String password;
    private String confirmPassword;


    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.password = constraintAnnotation.passwordField();
        this.confirmPassword = constraintAnnotation.confirmPasswordField();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Field passwordField = o.getClass().getDeclaredField(password);
            Field confirmPasswordField = o.getClass().getDeclaredField(confirmPassword);

            passwordField.setAccessible(true);
            confirmPasswordField.setAccessible(true);

            return passwordField.get(o).equals(confirmPasswordField.get(o));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
