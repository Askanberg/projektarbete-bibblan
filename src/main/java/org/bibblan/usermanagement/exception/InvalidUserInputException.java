package org.bibblan.usermanagement.exception;

public class InvalidUserInputException extends IllegalArgumentException {

    public InvalidUserInputException(String msg){
        super(msg);
    }
}
