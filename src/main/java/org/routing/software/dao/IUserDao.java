package org.routing.software.dao;

import org.routing.software.jpos.UserJpo;

import java.util.Optional;

public interface IUserDao extends IGenericDAO<UserJpo> {

    Optional<UserJpo> getByUsername(String username);
    boolean isUserValid(String username, String password);
    boolean isEmailExists(String username); //email is same as username

    }
}
