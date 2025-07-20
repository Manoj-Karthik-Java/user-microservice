package com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.repository;

import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.data.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);
}


/*
  we can use CrudRepository instead of JpaRepository but the key differences are listed below

          Feature                         CrudRepository   JpaRepository

        | Basic CRUD                    | ✅ Yes         | ✅ Yes         |
        | Pagination & Sorting          | ❌ No          | ✅ Yes         |
        | Batch Operations              | ❌ Limited     | ✅ Yes         |
        | JPA-specific functionality    | ❌ No          | ✅ Yes         |
        | Use for simple data access    | ✅ Recommended | 🚫 Overkill    |
        | Use for advanced JPA features | 🚫 Not enough  | ✅ Recommended |
*/