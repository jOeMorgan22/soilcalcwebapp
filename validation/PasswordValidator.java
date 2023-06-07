package com.soilwebapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    /*   ^                                       start of string
      (?=.*[0-9])                                a digit must occur at least once
      (?=.*[a-z])                                a lower case letter must occur at least once
      (?=.*[A-Z])                                an upper case letter must occur at least once
      (?=.*[@#$%^&+=])                           a special character must occur at least once
      .{8,}                                      anything, at least eight places though
      $                                          end of string */

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value.isEmpty() || value.isBlank()){
            return false;
        }
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=]).{8,}$");
        Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }
}
