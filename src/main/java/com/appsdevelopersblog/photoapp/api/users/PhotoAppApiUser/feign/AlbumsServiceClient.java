package com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.feign;

import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.model.response.AlbumResponseModel;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "albums-ws") // here the name property denotes the name in the application.properties file in Albums microservice
public interface AlbumsServiceClient {

    @GetMapping("users/{id}/albums")
    @CircuitBreaker(name = "albums-ws",fallbackMethod = "getAlbumsFallback")
     List<AlbumResponseModel> getAlbums(@PathVariable String id);

/*
    Rules to follow for fallback methods

    1. The method name of fallback function should be same as "fallback" property in @CircuitBreaker(name = "albums-ws",fallbackMethod = "getAlbumsFallback")
    2. The below method should be declared in the same class or interface where the @CircuitBreaker is used
    3. The below method should have the same signature as getAlbums() method and we can add another argument for handling appropriate exception.
 */
     default List<AlbumResponseModel> getAlbumsFallback(String id , Throwable e){
        System.out.println("id : = " + id);
        System.out.println("Exception occurred " + e.getLocalizedMessage());

        return new ArrayList<>();
    }

}
