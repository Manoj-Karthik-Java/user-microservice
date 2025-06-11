package com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserResponseModel{
    private String firstName;
    private String lastName;
    private String userId;
    private String email;
}
