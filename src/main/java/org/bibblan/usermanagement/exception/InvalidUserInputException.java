package org.bibblan.usermanagement.exception;

import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class InvalidUserInputException extends MethodArgumentNotValidException {

    public InvalidUserInputException(MethodParameter parameter, BindingResult bindingResult) {
        super(parameter, bindingResult);
    }
}
