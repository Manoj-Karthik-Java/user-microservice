package com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.service;

import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    UserDto getUserDetailsByEmail(String email);
}
