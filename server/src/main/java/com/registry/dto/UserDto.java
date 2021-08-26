package com.registry.dto;

import com.registry.repository.user.Role;
import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * 앱 리뷰
 */
public class UserDto {
    @ApiModel("UserDto.CREATE")
    public static class CREATE {
        public String username;
        public String password;
        public String email;
    }

    @ApiModel("UserDto.EDIT")
    public static class EDIT {
        public String id;
        public String password;
        public String name;
        public String username;
        public String email;
        public Boolean enabled;
    }

    @ApiModel("UserDto.VIEW")
    public static class VIEW {
        public Long id;
        public String username;
        public String name;
        public String email;
        public Boolean enabled;
        public Boolean superuser;
        public List<Role> roles;
    }

}
