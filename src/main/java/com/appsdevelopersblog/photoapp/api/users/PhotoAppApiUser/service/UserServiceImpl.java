package com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.service;

import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.data.UserEntity;
import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.feign.AlbumsServiceClient;
import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.model.response.AlbumResponseModel;
import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.repository.UserRepository;
import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.shared.UserDto;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    BCryptPasswordEncoder bCryptPasswordEncoder;
    UserRepository userRepository;
    RestTemplate restTemplate;
    Environment environment;

    @Autowired
    AlbumsServiceClient albumsServiceClient;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RestTemplate restTemplat, Environment environment) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.environment = environment;
        this.restTemplate = restTemplat;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userRepository.save(userEntity);
        UserDto returnValue = modelMapper.map(userEntity, UserDto.class);
        return returnValue;
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {

        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) throw new UsernameNotFoundException("User not found.");
        UserDto userDto = new ModelMapper().map(user, UserDto.class);

        // here the third argument is used to include like ResponseEntity<>.
        // restTemplate.getForObject() is used for only GET requests, and we cannot add custom headers or body. It is used when you just want the response body mapped to an object

        // commenting out the below code so that we can use Feign client instead of RestTemplate

        /*
        String albumUrl = String.format(environment.getProperty("albums.url"),userId);
        ResponseEntity<List<AlbumResponseModel>> albumResponse = restTemplate.exchange(albumUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {});
        List<AlbumResponseModel> albumsList = albumResponse.getBody();
         */

        // Below code is the way of handling errors while communicating with other microservices
        // But we are using FeignErrorDecoder instead of simple try catch block. So I am commenting out the below code

       /*
        List<AlbumResponseModel> albumsList = null;

        try{
           albumsList = albumsServiceClient.getAlbums(userId);
        }
        catch (FeignException e){
            logger.error(e.getLocalizedMessage());
        }
        */
        List<AlbumResponseModel> albumsList =  albumsServiceClient.getAlbums(userId);

        userDto.setAlbumsList(albumsList);
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // here username is email, not the first name and last name
        UserEntity userEntity = userRepository.findByEmail(username);
        if (userEntity == null) throw new UsernameNotFoundException(username);

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

/*
    RestTemplate is a synchronous client provided by Spring that is used to make HTTP calls from one microservice to another.
    It is Synchronous. So it blocks the execution until response is received (not async)

    As of Spring 5 and moving into Spring Boot 3, RestTemplate is in maintenance mode. It’s being replaced by:
    ✅ WebClient (part of Spring WebFlux) it is modern, non-blocking HTTP client (recommended)

    When you annotate a RestTemplate (or WebClient.Builder) bean with @LoadBalanced, Spring will:
        1.Intercept HTTP calls
        2.Replace the service name (like http://userservice) with an actual instance (like http://localhost:8081)
        3.Use a load balancing strategy (e.g., round-robin) to choose which instance to call

    In our program we used http://ALBUMS-WS/users/%s/albums. By using this annotation our spring boot will automatically convert it into http://localhost:8081/users/%s/albums
 */