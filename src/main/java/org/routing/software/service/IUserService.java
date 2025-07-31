package org.routing.software.service;

import org.routing.software.dtos.UserReadOnlyDto;
import org.routing.software.dtos.UserRegisterDto;
import org.routing.software.jpos.UserJpo;
import org.routing.software.model.User;

import java.util.Optional;

public interface IUserService {

    Optional<User> registerUser(UserRegisterDto userRegisterDto);
    Optional<User> getUserByUsername(String username);
    boolean isUserValid(String username, String password);
    boolean isEmailExists(String username);
    boolean isUserExistsAndActive(String username);
    boolean isUserConfirmationTokenValid(String token);

}
