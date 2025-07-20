package com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.controller;

import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.model.request.CreateUserRequestModel;
import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.model.response.CreateUserResponseModel;
import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.model.response.UserResponseModel;
import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.service.UserService;
import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.shared.UserDto;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;
    Environment environmet;

    public UserController(UserService userService, Environment environment) {
        this.userService = userService;
        this.environmet = environment;
    }

    @GetMapping("/status/check")
    public String status() {
        return "working with token " + environmet.getProperty("token.secret");
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

    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId){
        UserDto userDto = userService.getUserByUserId(userId);
        System.out.println(userDto);

    //  When you are using the model mapper the field names should be same in both the classes
        UserResponseModel returnValue = new ModelMapper().map(userDto,UserResponseModel.class);

        return  ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }


}
/*
The ResponseEntity class in Spring Framework has several constructors that allow you to create instances with varying levels of customization. Here are the constructors:

## Constructors
1. ResponseEntity(HttpStatus status) - Creates a new ResponseEntity with the given status code.
2. ResponseEntity(T body, HttpStatus status) - Creates a new ResponseEntity with the given body and status code.
3. ResponseEntity(MultiValueMap<String, String> headers, HttpStatus status) - Creates a new ResponseEntity with the given headers and status code.
4. ResponseEntity(T body, MultiValueMap<String, String> headers, HttpStatus status) - Creates a new ResponseEntity with the given body, headers, and status code.



 */