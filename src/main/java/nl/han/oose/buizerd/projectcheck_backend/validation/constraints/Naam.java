package nl.han.oose.buizerd.projectcheck_backend.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Pattern(regexp = "^[\\p{L}\\p{Zs}\\p{Mn}\\p{Pd}']{3,18}$")
@Constraint(validatedBy = {})
public @interface Naam {

	String message() default "{nl.han.oose.buizerd.projectcheck_backend.Naam.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
