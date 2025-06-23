package com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.service;

import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.data.UserEntity;
import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.repository.UserRepository;
import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    BCryptPasswordEncoder bCryptPasswordEncoder;
    UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDto,UserEntity.class);
        userRepository.save(userEntity);
        UserDto returnValue = modelMapper.map(userEntity,UserDto.class);
        return returnValue;
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity==null) throw new UsernameNotFoundException(email);
        UserDto userDto = new ModelMapper().map(userEntity,UserDto.class);
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // here username is email, not the first name and last name
        UserEntity userEntity = userRepository.findByEmail(username);
        if(userEntity==null) throw new UsernameNotFoundException(username);

        return new User(
                userEntity.getEmail(),
                userEntity.getEncryptedPassword(),
                true,
                true,
                true,
                true,
                new ArrayList<>());
    }
}

/*

Defined BCryptPasswordEncoder bean definition in main class because....

Spring Boot does not automatically provide a BCryptPasswordEncoder bean,
unless you're using a specific Spring Security configuration that sets one up for authentication.

Since you're only using Spring Security manually for password encryption in your service,
and not through a WebSecurityConfigurerAdapter or AuthenticationManager, Spring doesn't create it by default.

So: if you don’t define it yourself as a @Bean,
Spring won’t know how to inject it, and you'll get an error like No qualifying bean of type 'BCryptPasswordEncoder' found.
 */