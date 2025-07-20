package com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {
    // In this program's context below implemented method is used to handle any error we get when we make a Feign request to Albums microservice.

    // In the below method parameter String s represents the method key — which is essentially a string identifier for the Feign client method that triggered the error.
    // If the getAlbums method fails with a 404 (not found), Feign will call decode("AlbumsServiceClient#getAlbums", response So the value of s would be "AlbumsServiceClient#getAlbums".

    @Override
    public Exception decode(String s, Response response) {

        switch (response.status()){
            case 400:
                   break;
            case 404: {
                if(s.contains("getAlbums"))
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()),"Users albums are not found");
                break;
            }
            default:
                return new Exception(response.reason());
        }
        return null;
    }
}
