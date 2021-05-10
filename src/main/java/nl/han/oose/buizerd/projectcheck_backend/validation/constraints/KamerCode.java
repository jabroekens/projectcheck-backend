package nl.han.oose.buizerd.projectcheck_backend.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;

@Min(1)
@Max(Kamer.KAMER_CODE_MAX)
@Constraint(validatedBy = {})
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface KamerCode {

	String message() default "{nl.han.oose.buizerd.projectcheck_backend.KamerCode.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
