package com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserResponseModel {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<AlbumResponseModel> albumsList;

}
