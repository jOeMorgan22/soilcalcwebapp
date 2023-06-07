package com.soilwebapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)

public @interface Password{
    String message() default "Password must be a minimum of 8 characters and contain at least one upper case letter, one lower case letter and a special character: !@#$%^&+= ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
