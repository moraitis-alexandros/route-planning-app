package org.routing.software.service;

import org.routing.software.dtos.UserReadOnlyDto;
import org.routing.software.dtos.UserRegisterDto;

import java.util.Optional;

public interface IUserService {

    Optional<UserReadOnlyDto> registerUser(UserRegisterDto userRegisterDto);
    Optional<UserReadOnlyDto> getUserByUsername(String username);
    boolean isUserValid(String username, String password);
    boolean isEmailExists(String username);
    boolean isUserExistsAndActive(String username);
    boolean isUserConfirmationTokenValid(String token);

}
