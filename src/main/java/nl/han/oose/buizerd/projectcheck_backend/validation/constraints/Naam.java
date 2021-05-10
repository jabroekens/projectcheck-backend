package nl.han.oose.buizerd.projectcheck_backend.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * TODO @Luka: regex voor naamvalidatie
 *  * Denk aan Unicode letters, spaties en andere tekens
 *  * Zie: https://docs.oracle.com/javaee/7/tutorial/bean-validation001.htm
 *  * Ook handig: https://regexr.com
 */
@NotBlank
@Constraint(validatedBy = {})
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Naam {

	String message() default "{nl.han.oose.buizerd.projectcheck_backend.Naam.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
