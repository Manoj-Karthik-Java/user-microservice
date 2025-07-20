package com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.shared;

import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.model.response.AlbumResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class UserDto implements Serializable {

    /*
    When an object is serialized,
    Java writes this ID into the serialized file.
    Later, when deserializing, Java checks whether the class has the same serialVersionUID as the saved object.
    If the class has changed (e.g., adding or removing fields) and the serialVersionUID does not match,
    a InvalidClassException is thrown, preventing incompatible deserialization.
     */

    private static final long serialVersionUID = -953297098295050686L;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userId;
    private String encryptedPassword;
    private List<AlbumResponseModel> albumsList;
}
