package com.udemy.auth;

import com.google.common.base.Optional;
import com.udemy.core.User;
import com.udemy.db.UserDAO;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

public class DBAuthenticator implements Authenticator<BasicCredentials, User> {
    private UserDAO userDAO;

    public DBAuthenticator(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @Override
    public Optional<User> authenticate(BasicCredentials basicCredentials) throws AuthenticationException {
        return userDAO.findByUsernamePassword(basicCredentials.getUsername(), basicCredentials.getPassword());
    }
}
