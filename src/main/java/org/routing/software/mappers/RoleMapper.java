package org.routing.software.mappers;

import org.routing.software.core.RoleType;

/**
 * A utility class for User Mapper interface in order to transform
 * role enum to string and vice verca
 */
public class RoleMapper {

        public String mapRoleTypeToString(RoleType roleType) {
            return roleType == null ? null : roleType.name();
        }

        public RoleType mapStringToRoleType(String role) {
            return role == null ? null : RoleType.valueOf(role);
        }

}
