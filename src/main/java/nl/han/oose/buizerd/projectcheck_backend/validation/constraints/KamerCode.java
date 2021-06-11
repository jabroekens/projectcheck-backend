package nl.han.oose.buizerd.projectcheck_backend.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Pattern(regexp = "^[a-zA-Z0-9]{1," + Kamer.KAMER_CODE_MAX_LENGTE + "}$")
@Constraint(validatedBy = {})
public @interface KamerCode {

	String message() default "{nl.han.oose.buizerd.projectcheck_backend.KamerCode.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
