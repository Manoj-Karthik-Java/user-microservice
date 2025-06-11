package com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.controller;

import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.model.request.CreateUserRequestModel;
import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.model.response.CreateUserResponseModel;
import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.service.UserService;
import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.shared.UserDto;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/status/check")
    public String status() {
        return "working";
    }

    @PostMapping
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel createUserRequestModel) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = modelMapper.map(createUserRequestModel, UserDto.class);

        UserDto createdUserDto = userService.createUser(userDto);

        CreateUserResponseModel createUserResponseModel = modelMapper.map(createdUserDto, CreateUserResponseModel.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUserResponseModel);
    }
}
