package com.udemy.auth;


import com.google.common.base.Optional;
import com.udemy.core.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;


public class HelloAuthenticator implements Authenticator<BasicCredentials, User> {
    public HelloAuthenticator(String password) {
        this.password = password;
    }

    private String password;
    @Override
    public Optional<User> authenticate(BasicCredentials basicCredentials) throws AuthenticationException {
        if(password.equals(basicCredentials.getPassword())){
            return Optional.of(new User());
        }
        else{
            return Optional.absent();
        }
    }
}
