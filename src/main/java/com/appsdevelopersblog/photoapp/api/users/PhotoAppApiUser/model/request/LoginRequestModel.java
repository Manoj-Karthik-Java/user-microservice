package com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestModel {
    private String email;
    private String password;
}
