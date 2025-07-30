package org.routing.software.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.routing.software.dtos.UserLoginDto;
import org.routing.software.dtos.UserReadOnlyDto;
import org.routing.software.dtos.UserRegisterDto;
import org.routing.software.jpos.UserJpo;
import org.routing.software.model.User;

/**
 * A mapper for User Entity mapping from dto -> entity -> jpo and vice versa.
 * It also uses Role Mapper to map the role enum to string
 */
@Mapper(uses = RoleMapper.class) //uses RoleMapper for role enum to string conversion
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    //Case 1: Register
    //From Dto -> Jpo Converters in register
    @Mapping(target = "confirmPassword", ignore = true)
    User userRegisterDtoToUser(UserRegisterDto userRegisterDto);

    UserJpo useToUserJpo(User user);

    //In register it will return a readOnlyDto
    User userJpoToUser(UserJpo userJpo);

    UserReadOnlyDto userToUserDto(User user);

    //Case 2: Login
    //From Dto -> Jpo Converters in Login
    @Mapping(target = "roleType", ignore = true)
    User userLoginDtoToUser(UserLoginDto userLoginDto);

    UserJpo userToUserJpo(User user);

    //In login will return a readOnlyDto
    //convert userJpo To User with the method of case 1
    //convert user To UserReadOnlyDto with the method of case 1

}
