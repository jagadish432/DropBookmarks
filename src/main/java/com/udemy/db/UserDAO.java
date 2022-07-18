package com.udemy.db;

import com.google.common.base.Optional;
import com.udemy.core.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserDAO extends AbstractDAO<User> {

    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<User> findAll() {
        return list(namedQuery("com.udemy.core.User.findAll"));
    }

    public Optional<User> findByUsernamePassword(String username, String passsword) {
        return Optional.fromNullable(
                uniqueResult(
                        namedQuery("com.udemy.core.User.findByUsernamePassword")
                                .setParameter("username", username)
                                .setParameter("password", passsword)
                )
        );
    }
}
