package org.bibblan.usermanagement.exception;


public class UserAlreadyExistsException extends IllegalArgumentException{

    public UserAlreadyExistsException(String msg){
        super(msg);
    }
}
