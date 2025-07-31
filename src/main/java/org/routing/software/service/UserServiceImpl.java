package org.routing.software.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ext.Provider;
import org.routing.software.dao.IUserDao;
import org.routing.software.dtos.UserReadOnlyDto;
import org.routing.software.dtos.UserRegisterDto;
import org.routing.software.jpos.UserJpo;
import org.routing.software.mappers.UserMapper;
import org.routing.software.model.User;
import org.routing.software.security.JwtService;

import java.util.Optional;

@ApplicationScoped
public class UserServiceImpl implements IUserService {

    @Inject
    IUserDao userDao;

    @Inject
    JwtService jwtService;

    @Inject
    UserMapper userMapper;

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
    public Optional<User> getUserByUsername(String username) {

        Optional<UserJpo> userJpo = userDao.getByUsername(username);
        User user = new User();
        if (userJpo.isPresent()) {
            user = userMapper.userJpoToUser(userJpo.get());
        }
        return Optional.of(user);

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
    public boolean isUserConfirmationTokenValid(String token) {
        UserJpo userJpo = userDao.userConfirmationTokenExists(token);
        User user = userMapper.userJpoToUser(userJpo);
        return jwtService.isTokenValid(token, user);
    }
}
