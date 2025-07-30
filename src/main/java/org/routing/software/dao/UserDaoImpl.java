package org.routing.software.dao;

import jakarta.persistence.NoResultException;
import org.routing.software.jpos.UserJpo;
import org.routing.software.security.SecUtil;

import java.util.Optional;

public class UserDaoImpl extends AbstractDao<UserJpo> implements IUserDao {

    @Override
    public Optional<UserJpo> getByUsername(String username) {
        String query = "SELECT u FROM UserJpo u WHERE username = :username";

        try {
            UserJpo user = getEntityManager()
                    .createQuery(query, UserJpo.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            //TODO LOGGER
            return Optional.empty();
        }
    }

    @Override
    public boolean isUserValid(String username, String password) {
        try {
            Optional<UserJpo> userJpo = getByUsername(username);
            return userJpo.filter(jpo -> SecUtil.checkPassword(password, jpo.getPassword())).isPresent();
        } catch (NoResultException e) {
            //TODO LOGGER
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isEmailExists(String username) {
        String query = "SELECT COUNT(u) FROM UserJpo u WHERE UserJpo.username= :username";
        try {
            Long count = getEntityManager().createQuery(query, Long.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return count > 0;
        } catch (NoResultException e) {
            //TODO LOGGER
            throw new RuntimeException(e);
        }
    }
}
