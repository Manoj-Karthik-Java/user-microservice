package com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.service;

import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.data.UserEntity;
import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.repository.UserRepository;
import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {


    UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDto,UserEntity.class);
        userEntity.setEncryptedPassword("test");
        userRepository.save(userEntity);
        UserDto returnValue = modelMapper.map(userEntity,UserDto.class);
        return returnValue;
    }
}
