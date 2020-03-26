package com.nwt.usercontrol.Exceptions;

public class UserNotFoundException  extends RuntimeException {
        public UserNotFoundException(long id) {
            super("EXCEPTION: User with id " + id + " does not exist!");
        }
    }

