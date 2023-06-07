package com.soilwebapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameValidator implements ConstraintValidator<Username, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value.isEmpty() || value.isBlank()){
            return false;
        }
        Pattern pattern = Pattern.compile("[^a-z0-9 ]");
        Matcher matcher = pattern.matcher(value);
        boolean badCharacters = matcher.find();
        return !badCharacters;
    }
}
