package org.routing.software.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ext.Provider;
import org.routing.software.dao.IUserDao;
import org.routing.software.dtos.UserReadOnlyDto;
import org.routing.software.dtos.UserRegisterDto;
import org.routing.software.jpos.UserJpo;
import java.util.Optional;

@ApplicationScoped
public class UserServiceImpl implements IUserService {

    @Inject
    IUserDao userDao;

    @Override
    public Optional<UserReadOnlyDto> registerUser(UserRegisterDto userRegisterDto) {
            Optional<UserJpo> userJpo = userDao.getByUsername(userRegisterDto.getUsername());

            if (!userJpo.isPresent()) {
                //TODO LOGGER throw exception not found
            } else {

                //convert userjpo
                userDao.insert(userJpo.get());
//            User user = convert to user from jpo;
//            UserReadOnlyDto userReadOnlyDto = convert to userreadonlyjpo from user
//            return optional userReadOnlyDto
            }
            return Optional.empty();
        }


    @Override
    public Optional<UserReadOnlyDto> getUserByUsername(String userName) {

        Optional<UserJpo> userJpo = userDao.getByUsername(userName);

        if (userJpo.isPresent()) {
            //converter
//            User user = convert (userJpo);
//            UserReadOnlyDto userReadOnlyDto = convert user

        }

        return null;
    }

    @Override
    public boolean isUserValid(String username, String password) {
        return userDao.isUserValid(username, password);
    }

    @Override
    public boolean isEmailExists(String username) {
        return userDao.isUserExists(username);
    }

    @Override
    public boolean isUserExistsAndActive(String username) {
        return userDao.isUserExistsAndIsActive(username);
    }

    @Override
    public UserJpo isUserConfirmationTokenValid(String token) {
        UserJpo userJpo = userDao.userConfirmationTokenExists(token);
        return userJpo;
        //TODO mapper
    }
}
